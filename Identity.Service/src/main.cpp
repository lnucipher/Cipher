#include "Handlers.h"

#include <drogon/HttpAppFramework.h>
#include <drogon/orm/DbClient.h>

#include <chrono>
#include <thread>

using namespace drogon;
using namespace drogon::orm;

void serviceSetup();

int main()
{
    app()
        .loadConfigFile("./config.json")
        .setLogPath("./build/log")
        .registerBeginningAdvice(serviceSetup)
        .run();

    LOG_ERROR << "Service stopped.";

    return EXIT_SUCCESS;
}

void serviceSetup()
{
    if (!app().isRunning())
    {
        LOG_ERROR << "Service is not running. Aborting.";
        abort();
    }

    DbClientPtr dbClient = app().getDbClient();

    if (dbClient == nullptr)
    {
        LOG_ERROR << "No database connection. Aborting.";
        abort();
    }

    LOG_INFO << "Service started. Initializing data tables.";

    auto initUserTable = [dbClient]()
    {
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

    try
    {
        auto result = dbClient->execSqlSync(
            "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'User');"
        );

        bool tableExists = result[0]["exists"].as<bool>();
        if (!tableExists)
        {
            initUserTable();
        }
        else
        {
            LOG_INFO << "User table already exists.";
        }
    }
    catch (const drogon::orm::DrogonDbException &e)
    {
        LOG_ERROR << "Failed to check if User table exists: " << e.base().what();
        abort();
    }

    app()
        .registerHandler("/", &indexHandler, {Get})
        .registerHandler("/", &nameHandler, {Post});

    LOG_INFO << "Identity Service is ready.";
}