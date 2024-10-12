#include "Handlers.h"
#include "UserTable.h"

using namespace drogon;

static void serviceSetup();

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

static void setupDatabase()
{
    if (!isUserTableExists())
    {
        createUserTable();
    }
    else
    {
        LOG_INFO << "User table already exists.";
    }
}

static void setupEndpoints()
{
    app()
        .registerHandler("/", &indexHandler, {Get})
        .registerHandler("/", &nameHandler, {Post});
}

static void serviceSetup()
{
    if (!app().isRunning())
    {
        LOG_ERROR << "Service is not running. Aborting.";
        abort();
    }

    LOG_INFO << "Service started. Initializing data tables and APIs.";

    setupDatabase();
    setupEndpoints();

    LOG_INFO << "Identity Service is ready.";
}