#include "ContactTable.h"
#include "Filters.h"
#include "Handlers.h"
#include "UserTable.h"

using namespace drogon;

static void serviceSetup();
static void setCorsPolicy(const HttpRequestPtr &req, const HttpResponsePtr &resp);

int main()
{
    // auto authFilter = std::make_shared<AuthFilter>();

    app()
        .loadConfigFile("./config.json")
        .setLogPath("./build/log")
        // .registerFilter(authFilter)
        .registerBeginningAdvice(serviceSetup)
        .registerPostHandlingAdvice(setCorsPolicy)
        .run();

    LOG_ERROR << "Service stopped.";

    return EXIT_SUCCESS;
}

static void setupEndpoints()
{
    app()
        .registerHandler("/api/auth/isUserExist?username={username}", &usernameCheck, {Get, "AuthFilter"})
        .registerHandler("/api/auth/signup", &signUpHandler, {Post, "AuthFilter"})
        .registerHandler("/api/auth/signin", &signInHandler, {Post, "AuthFilter"})
        .registerHandler("/api/contact/add", &addContactHandler, {Post})
        .registerHandler("/api/contact/updateTimestamp", &updateContactInteractHandler, {Post})
        .registerHandler("/api/contact/delete?primaryUserId={primaryUserId}&secondaryUserId={secondaryUserId}",
                         &deleteContactHandler,
                         {Delete})
        .registerHandler("/api/contact/getPage?userId={userId}&pageSize={pageSize}&pageNumber={pageNumber}",
                         &getContactsHandler,
                         {Get});
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
    ContactTable::createContactTable();
    setupEndpoints();

    LOG_INFO << "Identity Service is ready.";
    LOG_INFO << "Now listening on: http: //[::]:4000";
}

static void setCorsPolicy(const HttpRequestPtr &req, const HttpResponsePtr &resp)
{
    resp->addHeader("Access-Control-Allow-Origin", "*");
}