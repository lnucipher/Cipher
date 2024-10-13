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

static void setupEndpoints()
{
    app()
        .registerHandler("/", &indexHandler, {Get})
        .registerHandler("/", &nameHandler, {Post})
        .registerHandler("/auth/signup", &signUpHandler, {Post});
}

static void serviceSetup()
{
    if (!app().isRunning())
    {
        LOG_FATAL << "Service is not running. Aborting.";
        abort();
    }

    LOG_INFO << "Service started. Initializing data tables and APIs.";

    UserTable::createUserTable();
    setupEndpoints();

    LOG_INFO << "Identity Service is ready.";
}