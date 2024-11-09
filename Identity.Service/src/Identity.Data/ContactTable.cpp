#include "ContactTable.h"
#include "DataUtils.h"
#include "UserTable.h"

#include <drogon/HttpAppFramework.h>
#include <drogon/orm/DbClient.h>

#include <semaphore>

using namespace drogon;
using namespace drogon::orm;

extern std::binary_semaphore tableModSem;

/// @note Transaction should be enclosed in scope to maintain DB connection
///       only for the required time.
void ContactTable::create()
{
    // Prevent race condition
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

    auto futureCreateResult = dbTransaction->execSqlAsyncFuture(R"(
        CREATE TABLE IF NOT EXISTS "Contact" (
            id VARCHAR(40) PRIMARY KEY,
            userId1 VARCHAR(40) NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
            userId2 VARCHAR(40) NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
            lastInteraction TIMESTAMPTZ DEFAULT (TIMEZONE('UTC', NOW()))
        );)"
    );

    auto futureIndexResult = dbTransaction->execSqlAsyncFuture(
        "CREATE UNIQUE INDEX IF NOT EXISTS idx_contact_userId1_userId2 ON \"Contact\"(userId1, userId2);");

    try
    {
        // Get correct result or error
        futureCreateResult.get();
        futureIndexResult.get();
        LOG_INFO << "Contact table initialized and indexed successfully.";
    }
    catch (const DrogonDbException &e)
    {
        LOG_FATAL << "Failed to initialize Contact table: " << e.base().what();
        #if defined(NDEBUG)
        abort();
        #endif
    }
}

std::shared_ptr<Json::Value> ContactTable::getLastContactsForUser(const std::string &userId,
                                                                  const unsigned int contactAmount,
                                                                  const unsigned int startAt)
{
    if (auto userCheck = isRealUser(userId);
        userCheck == nullptr || userCheck->isMember("error"))
    {
        return userCheck;
    }

    auto dbClient = drogon::app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(R"(
        SELECT C.id AS contactId,
                U1.id AS userId1, U1.username AS username1, U1.name AS name1, U1.bio AS bio1,
                U1.status AS status1, U1.lastSeen AS lastSeen1, U1.birthdate AS birthday1, U1.avatarUrl AS avatarUrl1,
                U2.id AS userId2, U2.username AS username2, U2.name AS name2, U2.bio AS bio2,
                U2.status AS status2, U2.lastSeen AS lastSeen2, U2.birthdate AS birthday2, U2.avatarUrl AS avatarUrl2
        FROM "Contact" AS C
        JOIN "User" AS U1 ON C.userId1 = U1.id
        JOIN "User" AS U2 ON C.userId2 = U2.id
        WHERE C.userId1 = $1 OR C.userId2 = $1
        ORDER BY C.lastInteraction DESC
        LIMIT $2 OFFSET $3
        )",
        userId, std::to_string(contactAmount + 1), std::to_string(startAt)
    );

    try
    {
        auto result = futureResult.get();

        Json::Value contacts(Json::arrayValue);
        bool hasNextPage = false;

        for (const auto& row : result)
        {
            if (contacts.size() >= contactAmount)
            {
                hasNextPage = true;
                break;
            }

            Json::Value contact;
            if (row["userid1"].as<std::string>() == userId)
            {
                contact["id"] = row["userid2"].as<std::string>();
                contact["username"] = row["username2"].as<std::string>();
                contact["name"] = row["name2"].as<std::string>();
                contact["bio"] = row["bio2"].as<std::string>();
                contact["status"] = row["status2"].as<std::string>();
                contact["lastSeen"] = formatToDatetime(row["lastseen2"].as<std::string>());
                contact["birthDate"] = row["birthday2"].isNull() ? "" : row["birthday2"].as<std::string>();
                contact["avatarUrl"] = row["avatarurl2"].as<std::string>();
            }
            else
            {
                contact["id"] = row["userid1"].as<std::string>();
                contact["username"] = row["username1"].as<std::string>();
                contact["name"] = row["name1"].as<std::string>();
                contact["bio"] = row["bio1"].as<std::string>();
                contact["status"] = row["status1"].as<std::string>();
                contact["lastSeen"] = formatToDatetime(row["lastseen1"].as<std::string>());
                contact["birthDate"] = row["birthday1"].isNull() ? "" : row["birthday1"].as<std::string>();
                contact["avatarUrl"] = row["avatarurl1"].as<std::string>();
            }

            contacts.append(contact);
        }

        auto response = std::make_shared<Json::Value>();
        (*response)["items"] = contacts;
        (*response)["hasNextPage"] = hasNextPage;
        (*response)["hasPreviousPage"] = (contacts.size() > 0 && startAt >= contactAmount) ? true : false;
        (*response)["pageNumber"] = (startAt / contactAmount) + 1;

        return response;
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

const std::shared_ptr<Json::Value> ContactTable::getUserContactIds(const std::string &userId)
{
    auto dbClient = app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        R"(
            SELECT id
            FROM "Contact"
            WHERE userId1 = $1 OR userId2 = $1
            ORDER BY lastInteraction DESC
        )",
        userId
    );

    try
    {
        auto result = futureResult.get();

        Json::Value contacts(Json::arrayValue);

        for (const auto& row : result)
        {
            contacts.append(row["id"].as<std::string>());
        }

        return std::make_shared<Json::Value>(contacts);
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

std::shared_ptr<Json::Value> ContactTable::addNewContact(const std::string &primaryUserId,
                                                         const std::string &secondaryUserId)
{
    if (auto primaryUserCheck = isRealUser(primaryUserId);
        primaryUserCheck == nullptr || primaryUserCheck->isMember("error"))
    {
        return primaryUserCheck;
    }

    if (auto secondaryUserCheck = isRealUser(secondaryUserId);
        secondaryUserCheck == nullptr || secondaryUserCheck->isMember("error"))
    {
        return secondaryUserCheck;
    }

    auto dbClient = app().getDbClient();

    if (primaryUserId == secondaryUserId)
    {
        Json::Value response;
        response["error"] = "Cannot add a contact with the same user ID.";
        return std::make_shared<Json::Value>(response);
    }

    auto contactIdCheck = getIdByContact(primaryUserId, secondaryUserId);
    if (contactIdCheck == nullptr)
    {
        return nullptr;
    }
    else if (!contactIdCheck->empty())
    {
        Json::Value response;
        response["error"] = "Contact already exists.";
        return std::make_shared<Json::Value>(response);
    }

    const std::string contactId = utils::getUuid(false);

    auto futureResult = dbClient->execSqlAsyncFuture(
        "INSERT INTO \"Contact\" (id, userId1, userId2) VALUES ($1, $2, $3) RETURNING lastInteraction",
        contactId, primaryUserId, secondaryUserId
    );

    try
    {
        auto result = futureResult.get();

        Json::Value response;
        response["id"] = contactId;
        response["primaryUser"] = primaryUserId;
        response["secondaryUser"] = secondaryUserId;
        response["lastInteraction"] = formatToDatetime(result[0]["lastinteraction"].as<std::string>());

        return std::make_shared<Json::Value>(response);
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

const std::shared_ptr<std::string> ContactTable::getIdByContact(const std::string &userId1,
                                                                const std::string &userId2)
{
    auto dbClient = app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        "SELECT id FROM \"Contact\" WHERE (userId1 = $1 AND userId2 = $2) OR (userId1 = $2 AND userId2 = $1)",
        userId1, userId2
    );

    try
    {
        auto result = futureResult.get();

        if (result.size() == 0)
        {
            return std::make_shared<std::string>("");
        }

        return std::make_shared<std::string>(result[0]["id"].as<std::string>());
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

const std::shared_ptr<Json::Value> ContactTable::getContactById(const std::string &contactId)
{
    auto dbClient = app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        "SELECT userId1, userId2 FROM \"Contact\" WHERE id = $1",
        contactId
    );

    try
    {
        auto result = futureResult.get();

        if (result.size() == 0)
        {
            Json::Value response;
            response["error"] = "Contact not found.";
            return std::make_shared<Json::Value>(response);
        }

        Json::Value response;
        response["userId1"] = result[0]["userid1"].as<std::string>();
        response["userId2"] = result[0]["userid2"].as<std::string>();

        return std::make_shared<Json::Value>(response);
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

const std::shared_ptr<std::string> ContactTable::updateLastInteract(const std::string &contactId,
                                                                    const std::string &timestamp)
{
    const auto timestampz = formatToTimestamp(timestamp);
    if (timestampz.empty() || !isValidTimestamp(timestampz))
    {
        return std::make_shared<std::string>("");
    }

    auto dbClient = app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        "UPDATE \"Contact\" SET lastInteraction = $1 WHERE Id = $2 RETURNING lastInteraction",
        timestampz, contactId
    );

    try
    {
        auto result = futureResult.get();

        if (result.size() == 0)
        {
            return std::make_shared<std::string>("");
        }

        return std::make_shared<std::string>(
            formatToDatetime(result[0]["lastinteraction"].as<std::string>()));
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

const std::shared_ptr<std::string> ContactTable::updateLastInteract(const std::string &primaryUser,
                                                                    const std::string &secondaryUser,
                                                                    const std::string &timestamp)
{
    return updateLastInteract(*getIdByContact(primaryUser, secondaryUser), timestamp);
}

const std::shared_ptr<bool> ContactTable::deleteContact(const std::string &contactId)
{
    auto dbClient = app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        "DELETE FROM \"Contact\" WHERE id = $1 RETURNING id",
        contactId
    );

    try
    {
        auto result = futureResult.get();

        if (result.size() == 0)
        {
            // Contact not found or could not be deleted
            return std::make_shared<bool>(false);
        }

        return std::make_shared<bool>(true);
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}