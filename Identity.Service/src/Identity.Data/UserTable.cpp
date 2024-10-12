#include <drogon/HttpAppFramework.h>
#include <drogon/orm/DbClient.h>

using namespace drogon;
using namespace drogon::orm;

bool isUserTableExists()
{
    DbClientPtr dbClient = app().getDbClient();

    if (dbClient == nullptr)
    {
        LOG_ERROR << "No database connection. Aborting.";
        abort();
    }

    try
    {
        return dbClient->execSqlSync(
            "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'User');"
        )[0]["exists"].as<bool>();
    }
    catch (const drogon::orm::DrogonDbException &e)
    {
        LOG_ERROR << "Failed to check if User table exists: " << e.base().what();
        abort();
    }
}

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
    catch(const drogon::orm::DrogonDbException &e)
    {
        LOG_ERROR << "Failed to create User table: " << e.base().what();
        abort();
    }
};