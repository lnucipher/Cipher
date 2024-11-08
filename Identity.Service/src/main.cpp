#if !defined(NDEBUG)
#include "ApiUtils.h"
#endif

static void serviceSetup(); // Declare friend function as static

#include "ContactTable.h"
#include "Filters.h"
#include "Handlers.h"
#include "UserTable.h"

#include <semaphore>

using namespace drogon;
static void setDefaultAvatar();
static void setCorsPolicy(const HttpRequestPtr &req, const HttpResponsePtr &resp);
#if !defined(NDEBUG)
static void addTestData();
#endif

std::binary_semaphore tableModSem(1);  //!< Table modification semaphore

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
        .registerHandler("/api/contacts", &addContactHandler, {Post})
        .registerHandler("/api/contacts?primaryUserId={primaryUserId}&secondaryUserId={secondaryUserId}",
                         &deleteContactHandler,
                         {Delete})
        .registerHandler("/api/contacts?userId={userId}&pageSize={pageSize}&page={page}",
                         &getContactsHandler,
                         {Get})
        .registerHandler("/api/contacts/lastInteraction", &updateContactInteractHandler, {Patch})
        .registerHandler("/api/userSearch?requestorId={requestorId}&searchedUsername={searchedUsername}",
                         &findUsersWithContactCheck,
                         {Get})
        .registerHandler("/api/users/status", &updateUserStatusHandler, {Patch});
        // TODO: PATCH: update user data
        // TODO: PATCH: update user password
        // TODO: PATCH: update user avatar
        // TODO: DELETE: remove user avatar - return default
        // TODO: DELETE: delete user
}

static void serviceSetup()
{
    if (!app().isRunning())
    {
        LOG_FATAL << "Service is not running. Aborting.";
        #if defined(NDEBUG)
        abort();
        #endif
    }

    LOG_INFO << "Service started. Initializing data tables and APIs.";

    setDefaultAvatar();
    UserTable::create();
    ContactTable::create();
    setupEndpoints();

    #if !defined(NDEBUG)
    addTestData();
    #endif

    LOG_INFO << "Identity Service is ready.";
    LOG_INFO << "Now listening on: http: //[::]:4000";
}

static void setDefaultAvatar()
{
    const std::string defaultAvatar = "./uploads/defaultAvatar.png";
    if (!std::filesystem::exists(defaultAvatar)
        && std::rename("./defaultAvatar.png", defaultAvatar.c_str()))
    {
        LOG_FATAL << "Failed to create default avatar file. Aborting.";
        #if defined(NDEBUG)
        abort();
        #endif
    }
}

static void setCorsPolicy(const HttpRequestPtr &req, const HttpResponsePtr &resp)
{
    resp->addHeader("Access-Control-Allow-Origin", "*");
}

#if !defined(NDEBUG)
static void addTestData()
{
    LOG_INFO << "Debug mode enabled. Using test data.";

    const std::string rootName = "root_user";
    LOG_WARN << "DO NOT DELETE ROOT USER \"" << rootName << "\". OTHERWISE TABLES WILL BE RECREATED.";

    if (auto isTestDataExist = UserTable::isUsernameExist(rootName);
        isTestDataExist != nullptr && (*isTestDataExist))
    {
        LOG_INFO << "Test data already exists.";

        auto dbClient = drogon::app().getDbClient();

        auto futureResult = dbClient->execSqlAsyncFuture(
            "SELECT id, username FROM \"User\"");

        try
        {
            auto result = futureResult.get();

            Json::Value userIds(Json::arrayValue);

            for (const auto& row : result)
            {
                const std::string id = row["id"].as<std::string>();
                LOG_DEBUG << row["username"].as<std::string>() << ": "
                          << id << ", token: " << genJwtToken(id);
            }
        }
        catch (const drogon::orm::DrogonDbException &e)
        {
            LOG_ERROR << "Database error: " << e.base().what();
        }

        return;
    }
    else
    {
        LOG_WARN << "TEST DATA IS CORRUPTED. CLEARING TABLES.";
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
    }

    Json::Value testData;
    testData["name"] = rootName;
    testData["username"]  = rootName;
    testData["password"] = "test_password";

    UserTable rootUser(std::make_shared<Json::Value>(testData));
    auto rootUserData = rootUser.addNewUser();

    if (rootUserData == nullptr)
    {
        LOG_ERROR << "Failed to add root user";
        return;
    }

    const std::string testName = "test_name";
    const std::string testUsername = "test_username";

    testData["name"] = testName;
    testData["username"]  = testUsername;

    UserTable primaryUser(std::make_shared<Json::Value>(testData));
    auto primaryUserData = primaryUser.addNewUser();

    if (primaryUserData == nullptr)
    {
        LOG_ERROR << "Failed to add test user";
        return;
    }

    const std::string primaryUserId = (*primaryUserData)["id"].asString();

    LOG_DEBUG << "test_username id: " << primaryUserId
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

        const std::string testUserId = (*testUserData)["id"].asString();

        LOG_DEBUG << testData["username"].asString() << " id: " << testUserId
                  << " token: " << genJwtToken(testUserId);

        if (i <= 12)
        {
            ContactTable::addNewContact(primaryUserId, testUserId);
        }
    }

    auto fakeUserId = utils::getUuid(false);
    LOG_DEBUG << "fake_user id: " << fakeUserId << " token: " << genJwtToken(fakeUserId);

    LOG_INFO << "Adding test data completed.";
}
#endif // !NDEBUG