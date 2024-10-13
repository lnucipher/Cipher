#include "Handlers.h"
#include "User.h"
#include "UserTable.h"

#include <jwt-cpp/jwt.h>

#include <fstream>

using namespace drogon;

static const std::string setJwtSecretKey();
const std::string jwtSecret = setJwtSecretKey();

const std::shared_ptr<Json::Value> readMultiPartParams(const std::string &params)
{
    auto result = std::make_shared<Json::Value>();
    Json::Reader reader;

    if (reader.parse(params, *result))
    {
        return result;
    }

    return nullptr;
}

void signUpHandler(const HttpRequestPtr &request, Callback &&callback)
{
    MultiPartParser requestParser;
    requestParser.parse(request);
    auto requestParams = requestParser.getParameters();
    const auto requestBody = readMultiPartParams(requestParams["requestBody"]);

    if (requestBody == nullptr || !requestBody->isMember("username"))
    {
        Json::Value jsonBody;
        jsonBody["error"] = "Invalid input";
        auto response = HttpResponse::newHttpJsonResponse(jsonBody);
        response->setStatusCode(k400BadRequest);
        callback(response);
        return;
    }

    // handle avatar upload
    (*requestBody)["avatarUrl"] = "";

    auto errorMessage = User::areFieldsValid(requestBody);
    if (errorMessage != nullptr)
    {
        Json::Value jsonBody;
        jsonBody["error"] = *errorMessage;
        auto response = HttpResponse::newHttpJsonResponse(jsonBody);
        response->setStatusCode(k400BadRequest);
        callback(response);
        return;
    }

    try
    {
        UserTable userData(requestBody);

        if (*userData.isUsernameExist())
        {
            Json::Value jsonBody;
            jsonBody["error"] = "Username already taken";
            auto response = HttpResponse::newHttpJsonResponse(jsonBody);
            response->setStatusCode(k409Conflict);
            callback(response);
            return;
        }

        auto responseJson = userData.addNewUser();

        if (!responseJson->isMember("Id"))
        {
            Json::Value jsonBody;
            jsonBody["error"] = "Failed to create user";
            auto response = HttpResponse::newHttpJsonResponse(jsonBody);
            response->setStatusCode(k500InternalServerError);
            callback(response);
            return;
        }

        auto token = jwt::create()
            .set_type("JWT")
            .set_issuer("Identity")
            .set_payload_claim("username", jwt::claim(userData.getUsername()))
            .set_issued_now()
            .set_expires_in(std::chrono::seconds{7 * 24 * 60 * 60})
            .sign(jwt::algorithm::hs512{jwtSecret});

        (*responseJson)["Token"] = token;

        callback(HttpResponse::newHttpJsonResponse(*responseJson));
        return;
    }
    catch (const std::exception& e)
    {
        Json::Value jsonBody;
        jsonBody["error"] = e.what();
        auto response = HttpResponse::newHttpJsonResponse(jsonBody);
        response->setStatusCode(k500InternalServerError);

        callback(response);
        return;
    }
}

void indexHandler(const HttpRequestPtr &request, Callback &&callback)
{
    Json::Value jsonBody;
    jsonBody["message"] = "Hello, Cipher!";
    auto response = HttpResponse::newHttpJsonResponse(jsonBody);
    callback(response);
}

void nameHandler(const HttpRequestPtr &request, Callback &&callback)
{
    Json::Value jsonBody;

    auto requestBody = request->getJsonObject();

    if (requestBody == nullptr)
    {
        jsonBody["status"] = "error";
        jsonBody["message"] = "body is required";

        auto response = HttpResponse::newHttpJsonResponse(jsonBody);
        response->setStatusCode(HttpStatusCode::k400BadRequest);

        callback(response);
        return;
    }

    if (!requestBody->isMember("name"))
    {
        jsonBody["status"] = "error";
        jsonBody["message"] = "field 'name' is required";

        auto response = HttpResponse::newHttpJsonResponse(jsonBody);
        response->setStatusCode(HttpStatusCode::k400BadRequest);

        callback(response);
        return;
    }

    auto name = requestBody->get("name", "guest").asString();
    jsonBody["message"] = "Hello, " + name + "!";
    auto response = HttpResponse::newHttpJsonResponse(jsonBody);

    callback(response);
}

static const std::string setJwtSecretKey()
{
    std::ifstream jwtCreds("/run/secrets/jwt-secret-key");

    if (!jwtCreds.is_open())
    {
        LOG_FATAL << "Failed to open .jwt-secret file";
        abort();
    }

    std::string secret;
    std::getline(jwtCreds, secret);
    jwtCreds.close();

    return secret;
}