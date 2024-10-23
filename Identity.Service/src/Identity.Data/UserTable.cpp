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
            Id VARCHAR(40) PRIMARY KEY,
            Username VARCHAR(50) NOT NULL UNIQUE,
            Name VARCHAR(255) NOT NULL,
            Bio VARCHAR(70),
            PasswordHash VARCHAR(500) NOT NULL,
            Status INT CHECK (Status IN (0, 1)) DEFAULT 0,
            LastSeen TIMESTAMPTZ DEFAULT (TIMEZONE('UTC', NOW())),
            Birthday DATE,
            AvatarUrl TEXT
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
        "SELECT EXISTS (SELECT 1 FROM \"User\" WHERE Username = $1);",
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
            return std::nullopt;
        else
            return getBirthDate();
    }();

    auto dbClient = app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        "INSERT INTO \"User\" (Id, Username, Name, Bio, PasswordHash, Status, Birthday, AvatarUrl) "
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
        (*response)["Id"] = getId();
        (*response)["Username"] = getUsername();
        (*response)["Name"] = getName();
        (*response)["Bio"] = getBio();
        (*response)["AvatarUrl"] = getAvatarUrl();
        (*response)["BirthDate"] = getBirthDate();

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
        "SELECT Id FROM \"User\" WHERE Username = $1;",
        username
    );

    try
    {
        auto dbResult = futureResult.get();
        *result = dbResult[0]["Id"].as<std::string>();
    }
    catch (const DrogonDbException &e)
    {
        LOG_DEBUG << "Database error: " << e.base().what();
    }

    return result;
}

// static std::shared_ptr<Json::Value> getUserByUsername(const std::string& username)
// {
//     auto result = UserTable::isUsernameExist(username);

//     Json::Value jsonBody;
//     if (result == nullptr)
//     {
//         jsonBody["error"] = "Internal error.";
//         return result;
//     }

//     jsonBody["value"] = *result;
//     return result;
// }