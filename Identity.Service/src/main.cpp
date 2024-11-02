#if !defined(NDEBUG)
#include "ApiUtils.h"
#endif

#include "ContactTable.h"
#include "Filters.h"
#include "Handlers.h"
#include "UserTable.h"

using namespace drogon;

static void serviceSetup();
static void setCorsPolicy(const HttpRequestPtr &req, const HttpResponsePtr &resp);

#if !defined(NDEBUG)
static void addTestData();
#endif

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
                         {Get})
        .registerHandler("/api/user/searchUsers?requestorId={requestorId}&searchedUsername={searchedUsername}",
                         &findUsersWithContactCheck,
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

    #if !defined(NDEBUG)
    addTestData();
    #endif

    LOG_INFO << "Identity Service is ready.";
    LOG_INFO << "Now listening on: http: //[::]:4000";
}

static void setCorsPolicy(const HttpRequestPtr &req, const HttpResponsePtr &resp)
{
    resp->addHeader("Access-Control-Allow-Origin", "*");
}

#if !defined(NDEBUG)
static void addTestData()
{
    LOG_INFO << "Debug mode enabled. Clearing tables. Adding test data.";

    auto dbClient = app().getDbClient();
    auto futureContact = dbClient->execSqlAsyncFuture("TRUNCATE TABLE \"Contact\" CASCADE;");
    auto futureUser= dbClient->execSqlAsyncFuture("TRUNCATE TABLE \"User\" CASCADE;");

    try
    {
        futureContact.get();
        futureUser.get();
    }
    catch (const orm::DrogonDbException &e)
    {
        LOG_ERROR << "Debug setup: Database error: " << e.base().what();
        return;
    }

    std::string testName = "test_name";
    std::string testUsername = "test_username";

    Json::Value testData;
    testData["name"] = testName;
    testData["username"]  = testUsername;
    testData["password"] = "test_password";

    UserTable primaryUser(std::make_shared<Json::Value>(testData));
    auto primaryUserData = primaryUser.addNewUser();

    if (primaryUserData == nullptr)
    {
        LOG_ERROR << "Failed to add test user";
        return;
    }

    std::string primaryUserId = (*primaryUserData)["id"].asString();

    LOG_INFO << "test_username id: " << primaryUserId
             << " token: " << genJwtToken(primaryUserId);


    for (int i = 1; i <= 15; i++)
    {
        testData["name"] = testName + std::to_string(i);
        testData["username"] = testUsername + std::to_string(i);

        UserTable testUser(std::make_shared<Json::Value>(testData));
        auto testUserData = testUser.addNewUser();

        if (testUserData == nullptr)
        {
            LOG_ERROR << "Failed to add test user";
            continue;
        }

        std::string testUserId = (*testUserData)["id"].asString();

        LOG_INFO << testData["name"].asString() << " id: " << testUserId
                 << " token: " << genJwtToken(testUserId);

        if (i <= 12)
        {
            ContactTable::addNewContact(primaryUserId, testUserId);
        }
    }

    auto fakeUserId = utils::getUuid(false);
    LOG_INFO << "fake_user id: " << fakeUserId << " token: " << genJwtToken(fakeUserId);

    LOG_INFO << "Adding test data completed.";
}
#endif