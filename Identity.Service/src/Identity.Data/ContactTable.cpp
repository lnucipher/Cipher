#include "ContactTable.h"

#include <drogon/HttpAppFramework.h>
#include <drogon/orm/DbClient.h>

using namespace drogon;
using namespace drogon::orm;

void ContactTable::createContactTable()
{
    auto dbClient = app().getDbClient();

    if (dbClient == nullptr)
    {
        LOG_FATAL << "No database connection. Aborting.";
        abort();
    }

    auto futureResult = dbClient->execSqlAsyncFuture(R"(
        CREATE TABLE IF NOT EXISTS "Contact" (
            id VARCHAR(40) PRIMARY KEY,
            userId1 VARCHAR(40) NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
            userId2 VARCHAR(40) NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
            lastInteraction TIMESTAMPTZ DEFAULT (TIMEZONE('UTC', NOW()))
        );)"
    );

    try
    {
        futureResult.get();
        LOG_INFO << "Contact table initialized successfully.";
    }
    catch (const DrogonDbException &e)
    {
        LOG_FATAL << "Failed to create Contact table: " << e.base().what();
        abort();
    }

    auto futureIndexResult = dbClient->execSqlAsyncFuture(
        "CREATE UNIQUE INDEX IF NOT EXISTS idx_contact_userId1_userId2 ON \"Contact\"(userId1, userId2);");

    try
    {
        futureIndexResult.get();
        LOG_INFO << "Contact table indexed successfully.";
    }
    catch (const DrogonDbException &e)
    {
        LOG_FATAL << "Failed to index Contact table: " << e.base().what();;
        abort();
    }
}

std::shared_ptr<Json::Value> ContactTable::getLastContactsForUser(const std::string &userId,
                                                                  const unsigned int contactAmount,
                                                                  const unsigned int startAt)
{
    auto dbClient = drogon::app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(R"(
        SELECT C.id AS contactId,
                U1.id AS userId1, U1.username AS username1, U1.name AS name1, U1.bio AS bio1,
                U1.status AS status1, U1.lastSeen AS lastSeen1, U1.birthday AS birthday1, U1.avatarUrl AS avatarUrl1,
                U2.id AS userId2, U2.username AS username2, U2.name AS name2, U2.bio AS bio2,
                U2.status AS status2, U2.lastSeen AS lastSeen2, U2.birthday AS birthday2, U2.avatarUrl AS avatarUrl2
        FROM "Contact" AS C
        JOIN "User" AS U1 ON C.userId1 = U1.id
        JOIN "User" AS U2 ON C.userId2 = U2.id
        WHERE C.userId1 = $1 OR C.userId2 = $1
        ORDER BY C.lastInteraction DESC
        LIMIT $2 OFFSET $3
        )",
        userId, contactAmount, startAt
    );

    try
    {
        auto result = futureResult.get();

        Json::Value contacts(Json::arrayValue);

        for (const auto& row : result)
        {
            Json::Value contact;
            contact["contactId"] = row["contactid"].as<int64_t>();

            if (row["userid1"].as<std::string>() == userId)
            {
                contact["contactUserId"] = row["userid2"].as<int64_t>();
                contact["contactUsername"] = row["username2"].as<std::string>();
                contact["contactName"] = row["name2"].as<std::string>();
                contact["contactBio"] = row["bio2"].as<std::string>();
                contact["contactStatus"] = row["status2"].as<int>();
                contact["contactLastSeen"] = row["lastseen2"].as<std::string>();
                contact["contactBirthday"] = row["birthday2"].isNull() ? "" : row["birthday2"].as<std::string>();
                contact["contactAvatarUrl"] = row["avatarurl2"].as<std::string>();
            }
            else
            {
                contact["contactUserId"] = row["userid1"].as<int64_t>();
                contact["contactUsername"] = row["username1"].as<std::string>();
                contact["contactName"] = row["name1"].as<std::string>();
                contact["contactBio"] = row["bio1"].as<std::string>();
                contact["contactStatus"] = row["status1"].as<int>();
                contact["contactLastSeen"] = row["lastseen1"].as<std::string>();
                contact["contactBirthday"] = row["birthday1"].isNull() ? "" : row["birthday1"].as<std::string>();
                contact["contactAvatarUrl"] = row["avatarurl1"].as<std::string>();
            }

            contacts.append(contact);
        }

        auto response = std::make_shared<Json::Value>();
        (*response)["contacts"] = contacts;

        return response;
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

// std::shared_ptr<ContactList> ContactTable::getLastContactsForUser(const std::string &userId,
//                                                                   const unsigned int contactAmount,
//                                                                   const unsigned int startAt)
// {
//     auto dbClient = app().getDbClient();
//     auto futureResult = dbClient->execSqlAsyncFuture(
//         R"(
//             SELECT id, userId1, userId2
//             FROM "Contact"
//             WHERE userId1 = $1 OR userId1 = $1
//             ORDER BY lastInteraction DESC
//             LIMIT $2
//         )",
//         userId, contactAmount + startAt
//     );

//     try
//     {
//         auto result = futureResult.get();

//         ContactList contacts;

//         bool isFound = false;
//         for (const auto& row : result)
//         {
//             if (row["id"].as<std::string>() != result[startAt]["id"].as<std::string>() && !isFound)
//             {
//                 continue;
//             }

//             isFound = true;

//             if (row["userid1"].as<std::string>() != userId)
//             {
//                 contacts.push_back(row["userid1"].as<std::string>());
//             }
//             else
//             {
//                 contacts.push_back(row["userid2"].as<std::string>());
//             }
//         }

//         return std::make_shared<ContactList>(contacts);
//     }
//     catch (const DrogonDbException &e)
//     {
//         LOG_ERROR << "Database error: " << e.base().what();
//         return nullptr;
//     }
// }

std::shared_ptr<Json::Value> ContactTable::addNewContact(const std::string &primaryUser,
                                                         const std::string &secondaryUser)
{
    auto dbClient = app().getDbClient();

    if (primaryUser == secondaryUser)
    {
        Json::Value response;
        response["error"] = "Cannot add a contact with the same user ID.";
        return std::make_shared<Json::Value>(response);
    }

    const std::string contactId = utils::getUuid(false);

    auto futureResult = dbClient->execSqlAsyncFuture(
        "INSERT INTO \"Contact\" (id, userId1, userId2) VALUES ($1, $2, $3) RETURNING lastInteraction",
        contactId, primaryUser, secondaryUser
    );

    try
    {
        auto result = futureResult.get();

        Json::Value response;
        response["id"] = contactId;
        response["primaryUser"] = primaryUser;
        response["secondaryUser"] = secondaryUser;
        response["lastInteraction"] = result[0]["lastinteraction"].as<std::string>();

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

const std::shared_ptr<std::string> ContactTable::updateLastInteract(const std::string &contactId)
{
    auto dbClient = app().getDbClient();

    auto futureResult = dbClient->execSqlAsyncFuture(
        "UPDATE Contact SET lastInteraction = TIMEZONE('UTC', NOW()) WHERE Id = $1 RETURNING lastInteraction",
        contactId
    );

    try
    {
        auto result = futureResult.get();

        if (result.size() == 0)
        {
            return std::make_shared<std::string>("");
        }

        return std::make_shared<std::string>(result[0]["lastinteraction"].as<std::string>());
    }
    catch (const DrogonDbException &e)
    {
        LOG_ERROR << "Database error: " << e.base().what();
        return nullptr;
    }
}

const std::shared_ptr<std::string> ContactTable::updateLastInteract(const std::string &primaryUser,
                                                                    const std::string &secondaryUser)
{
    return updateLastInteract(*getIdByContact(primaryUser, secondaryUser));
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