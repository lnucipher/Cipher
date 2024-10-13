#include <drogon/HttpAppFramework.h>
#include <drogon/orm/DbClient.h>

#include "UserTable.h"

using namespace drogon;
using namespace drogon::orm;

void createUserTable()
{
    DbClientPtr dbClient = app().getDbClient();

    if (dbClient == nullptr)
    {
        LOG_ERROR << "No database connection. Aborting.";
        abort();
    }

    try
    {
        dbClient->execSqlSync(R"(
            CREATE TABLE "User" (
                Id SERIAL PRIMARY KEY,
                Username VARCHAR(50) NOT NULL UNIQUE,
                Name VARCHAR(255) NOT NULL,
                Bio VARCHAR(70),
                PasswordHash VARCHAR(500) NOT NULL,
                Status INT CHECK (Status IN (0, 1)) DEFAULT 0,
                AvatarUrl TEXT,
                LastSeen TIMESTAMPTZ DEFAULT NOW(),
                Birthday DATE
            );
        )");

        LOG_INFO << "User table created successfully.";
    }
    catch(const DrogonDbException &e)
    {
        std::string errorMessage = e.base().what();
        if (errorMessage != "ERROR:  relation \"User\" already exists\n")
        {
            LOG_ERROR << "Failed to create User table: " << errorMessage;
            abort();
        }
        else
        {
            LOG_INFO << "User table already exists.";
        }
    }
}