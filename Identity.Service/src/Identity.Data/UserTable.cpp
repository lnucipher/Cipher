#include "UserTable.h"

#include "DataUtils.h"

#include "bcrypt/BCrypt.hpp"
#include <drogon/HttpAppFramework.h>
#include <drogon/orm/DbClient.h>

#include <semaphore>

using namespace drogon;
using namespace drogon::orm;

extern std::binary_semaphore tableModSem;

UserTable::UserTable(const std::shared_ptr<Json::Value> requestBody)
{
    for (const auto field : requestFields)
    {
        std::string fieldValue = requestBody->get(field, "").asString();

        const bool isMandatory = (field == "username") || (field == "name") || (field == "password");
        if (isMandatory && fieldValue.empty())
        {
            throw std::invalid_argument(field + " must not be empty.");
        }

        if (field == "birthDate" && !fieldValue.empty() && !isBirthDateValid(fieldValue))
        {
            throw std::invalid_argument("Birth date bad format.");
        }

        if(field == "password")
        {
            fieldMap[field] = BCrypt::generateHash(fieldValue);
            continue;
        }

        fieldMap[field] = fieldValue;
    }

    fieldMap["id"] = utils::getUuid(false);
}

/// @note Transaction should be enclosed in scope to maintain DB connection
///       only for the required time.
void UserTable::create()
{
    // Prevent race condition for async transactions
    tableModSem.acquire();
    auto dbClient = app().getDbClient();

    if (dbClient == nullptr)
    {
        LOG_FATAL << "No database connection. Aborting.";
        #if defined(NDEBUG)
        abort();
        #endif
    }

    auto dbTransaction = dbClient->newTransaction();
    dbTransaction->setCommitCallback([](bool) { tableModSem.release(); });

    auto futureEnumResult = dbTransaction->execSqlAsyncFuture(R"(
        DO $$
        BEGIN
            IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_status') THEN
                CREATE TYPE user_status AS ENUM ('ONLINE', 'OFFLINE');
            END IF;
        END $$;)");

    auto futureCreateResult = dbTransaction->execSqlAsyncFuture(R"(
        CREATE TABLE IF NOT EXISTS "User" (
            id VARCHAR(40) PRIMARY KEY,
            username VARCHAR(50) NOT NULL UNIQUE,
            name VARCHAR(255) NOT NULL,
            bio VARCHAR(70),
            passwordHash VARCHAR(500) NOT NULL,
            status user_status DEFAULT 'OFFLINE',
            lastSeen TIMESTAMPTZ DEFAULT (TIMEZONE('UTC', NOW())),
            birthday DATE,
            avatarUrl TEXT
        );)"
    );

    auto futureIndexResult = dbTransaction->execSqlAsyncFuture(
        "CREATE INDEX IF NOT EXISTS idx_user_username ON \"User\"(username);");

    try
    {
        // Get correct result or error
        futureEnumResult.get();
        futureCreateResult.get();
        futureIndexResult.get();
        LOG_INFO << "User table initialized and indexed successfully.";
    }
    catch (const DrogonDbException &e)
    {
        LOG_FATAL << "Failed to initialize User table: " << e.base().what();
        #if defined(NDEBUG)
        abort();
        #endif
    }
}

const std::shared_ptr<bool> UserTable::isUsernameExist(const std::string& username)
{
    if (username.empty())
    {
        return std::make_shared<bool>(false);
    }

    auto dbClient = app().getDbClient();
    auto futureResult = dbClient->execSqlAsyncFuture(
        "SELECT EXISTS (SELECT 1 FROM \"User\" WHERE username = $1);",
        username
    );

    try
    {
        auto res = futureResult.get();
        return std::make_shared<bool>(res[0]["exists"].as<bool>());
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

const std::shared_ptr<bool> UserTable::isUserExist(const std::string& userId)
{
    if (userId.empty())
    {
        return std::make_shared<bool>(false);
    }

    auto dbClient = app().getDbClient();
    auto futureResult = dbClient->execSqlAsyncFuture(
        "SELECT EXISTS (SELECT 1 FROM \"User\" WHERE id = $1);",
        userId
    );

    try
    {
        auto res = futureResult.get();
        return std::make_shared<bool>(res[0]["exists"].as<bool>());
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

const std::shared_ptr<bool> UserTable::isUsernameExist()
{
    return isUsernameExist(getUsername());
}


std::shared_ptr<Json::Value> UserTable::addNewUser()
{
    auto response = std::make_shared<Json::Value>();

    if (fieldMap.size() == 0)
    {
        LOG_ERROR << "No user requestFields are found.";
        return response = nullptr;
    }

    const auto birthDate = [this]() -> std::optional<std::string>
    {
        if(getBirthDate().empty())
        {
            return std::nullopt;
        }
        else
        {
            return getBirthDate();
        }
    }();

    auto dbClient = app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        "INSERT INTO \"User\" (id, username, name, bio, passwordHash, status, birthday, avatarUrl) \
        VALUES ($1, $2, $3, $4, $5, $6, $7, $8);",
        getId(),
        getUsername(),
        getName(),
        getBio(),
        getPassword(),
        Status::OFFLINE,
        birthDate,
        getAvatarUrl()
    );

    try
    {
        (*response)["id"] = getId();
        (*response)["username"] = getUsername();
        (*response)["name"] = getName();
        (*response)["bio"] = getBio();
        (*response)["avatarUrl"] = getAvatarUrl();
        (*response)["birthDate"] = getBirthDate();

        futureResult.get();
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        response = nullptr;
    }

    return response;
}

const std::shared_ptr<std::string> UserTable::getUserId(const std::string& username)
{
    auto result = std::shared_ptr<std::string>(nullptr);

    auto dbClient = app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        "SELECT id FROM \"User\" WHERE username = $1;",
        username
    );

    try
    {
        auto dbResult = futureResult.get();
        *result = dbResult[0]["id"].as<std::string>();
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
    }

    return result;
}

std::shared_ptr<Json::Value> UserTable::getUserByUsername(const std::string& username)
{
    auto dbClient = drogon::app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        "SELECT id, name, passwordHash, bio, status, lastSeen, birthday, avatarUrl FROM \"User\" WHERE username = $1",
        username
    );

    Json::Value userJson;

    try
    {
        auto result = futureResult.get();

        if (result.size() == 1)
        {
            userJson["id"] = result[0]["id"].as<std::string>();
            userJson["username"] = username;
            userJson["name"] = result[0]["name"].as<std::string>();
            userJson["bio"] = result[0]["bio"].as<std::string>();
            userJson["passwordHash"] = result[0]["passwordhash"].as<std::string>();
            userJson["status"] = result[0]["status"].as<std::string>();
            userJson["lastSeen"] = formatToDatetime(result[0]["lastseen"].as<std::string>());
            userJson["birthDate"] = result[0]["birthday"].isNull()
                ? "" : result[0]["birthday"].as<std::string>();
            userJson["avatarUrl"] = result[0]["avatarurl"].as<std::string>();
        } else
        {
            userJson["error"] = "User not found.";
        }
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();

        userJson["error"] = "Internal error.";
    }

    return std::make_shared<Json::Value>(userJson);
}

std::shared_ptr<Json::Value> UserTable::searchUsersWithContactCheck(const std::string &requestorUserId,
                                                                    const std::string &searchUsername)
{
    auto dbClient = drogon::app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        R"(
            SELECT U.id, U.username, U.name, U.bio, U.status, U.lastSeen, U.birthday, U.avatarUrl,
                   CASE WHEN C.id IS NOT NULL THEN TRUE ELSE FALSE END AS isContact
            FROM "User" AS U
            LEFT JOIN "Contact" AS C
              ON (C.userId1 = $1 AND C.userId2 = U.id) OR (C.userId2 = $1 AND C.userId1 = U.id)
            WHERE U.username ILIKE '%' || $2 || '%'
            ORDER BY U.username
        )",
        requestorUserId, searchUsername
    );

    try
    {
        auto result = futureResult.get();

        Json::Value users(Json::arrayValue);

        for (const auto& row : result)
        {
           Json::Value userInfo;
            userInfo["id"] = row["id"].as<std::string>();
            userInfo["username"] = row["username"].as<std::string>();
            userInfo["name"] = row["name"].as<std::string>();
            userInfo["bio"] = row["bio"].as<std::string>();
            userInfo["status"] = row["status"].as<std::string>();
            userInfo["lastSeen"] = formatToDatetime(row["lastseen"].as<std::string>());
            userInfo["birthDate"] = row["birthday"].isNull() ? "" : row["birthday"].as<std::string>();
            userInfo["avatarUrl"] = row["avatarurl"].as<std::string>();

            Json::Value user;
            user["user"] = userInfo;
            user["isContact"] = row["iscontact"].as<bool>();

            users.append(user);
        }

        auto response = std::make_shared<Json::Value>();

        (*response)["items"] = users;

        return response;
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

std::shared_ptr<Json::Value> UserTable::updateUserStatus(const std::string &userId, const std::string &status)
{
    auto uppercaseStatus = toUppercase(status);
    if (!isStatusValid(uppercaseStatus))
    {
        Json::Value response;
        response["error"] = "Status value is not valid.";
        return std::make_shared<Json::Value>(response);
    }

    auto dbClient = drogon::app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        R"(
            UPDATE "User"
            SET status = $1
            WHERE id = $2
            RETURNING id, status
        )",
        uppercaseStatus, userId
    );

    try
    {
        auto result = futureResult.get();

        if (result.empty())
        {
            Json::Value response;
            response["error"] = "User not found.";
            return std::make_shared<Json::Value>(response);
        }

        Json::Value response;
        response["id"] = result[0]["id"].as<std::string>();
        response["status"] = result[0]["status"].as<std::string>();

        return std::make_shared<Json::Value>(response);
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}