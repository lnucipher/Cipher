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