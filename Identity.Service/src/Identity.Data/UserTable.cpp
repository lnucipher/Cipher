#include "UserTable.h"

#include "DataUtils.h"

#include "bcrypt/BCrypt.hpp"
#include <drogon/HttpAppFramework.h>
#include <drogon/orm/DbClient.h>

using namespace drogon;
using namespace drogon::orm;

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

void UserTable::createUserTable()
{
    auto dbClient = app().getDbClient();

    if (dbClient == nullptr)
    {
        LOG_FATAL << "No database connection. Aborting.";
        abort();
    }

    auto futureResult = dbClient->execSqlAsyncFuture(R"(
        CREATE TABLE "User" (
            id VARCHAR(40) PRIMARY KEY,
            username VARCHAR(50) NOT NULL UNIQUE,
            name VARCHAR(255) NOT NULL,
            bio VARCHAR(70),
            passwordHash VARCHAR(500) NOT NULL,
            status INT CHECK (Status IN (0, 1)) DEFAULT 0,
            lastSeen TIMESTAMPTZ DEFAULT (TIMEZONE('UTC', NOW())),
            birthday DATE,
            avatarUrl TEXT
        );)"
    );

    try
    {
        futureResult.get();
        LOG_INFO << "User table created successfully.";
    }
    catch (const DrogonDbException &e)
    {
        std::string errorMessage = e.base().what();
        if (errorMessage != "ERROR:  relation \"User\" already exists\n")
        {
            LOG_FATAL << "Failed to create User table: " << errorMessage;
            abort();
        }
        else
        {
            LOG_INFO << "User table already exists.";
        }
    }
}

std::shared_ptr<bool> UserTable::isUsernameExist(const std::string& username)
{
    auto result = std::make_shared<bool>(nullptr);

    if (username.empty())
    {
        *result = false;
        return result;
    }

    auto dbClient = app().getDbClient();
    auto futureResult = dbClient->execSqlAsyncFuture(
        "SELECT EXISTS (SELECT 1 FROM \"User\" WHERE username = $1);",
        username
    );

    try
    {
        auto res = futureResult.get();
        *result = res[0]["exists"].as<bool>();
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
    }

    return result;
}

std::shared_ptr<bool> UserTable::isUsernameExist()
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
        "INSERT INTO \"User\" (id, username, name, bio, passwordHash, status, birthday, avatarUrl) "
        "VALUES ($1, $2, $3, $4, $5, 0, $6, $7);",
        getId(),
        getUsername(),
        getName(),
        getBio(),
        getPassword(),
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

std::shared_ptr<std::string> UserTable::getUserId(const std::string& username)
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
        LOG_DEBUG << "Database error: " << e.base().what();
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
            userJson["status"] = result[0]["status"].as<int>();
            userJson["lastSeen"] = result[0]["lastseen"].as<std::string>();
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
        LOG_DEBUG << "Database error: " << e.base().what();

        userJson["error"] = "Internal error.";
    }

    return std::make_shared<Json::Value>(userJson);
}