#include "ApiUtils.h"
#include "Handlers.h"
#include "UserTable.h"

#include <jwt-cpp/jwt.h>

using namespace drogon;

inline const std::string jwtSecret = setJwtSecretKey();

HttpResponsePtr badRequestResponse(HttpStatusCode statusCode)
{
    Json::Value jsonBody;
    jsonBody["error"] = "Invalid input";
    auto response = HttpResponse::newHttpJsonResponse(jsonBody);
    response->setStatusCode(statusCode);
    return response;
}

void signUpHandler(const HttpRequestPtr &request, Callback &&callback)
{
    MultiPartParser requestParser;

    if (requestParser.parse(request) != 0 || requestParser.getFiles().size() > 1)
    {
        callback(badRequestResponse(k403Forbidden));
        return;
    }

    auto requestParams = requestParser.getParameters();
    const auto requestBody = readMultiPartParams(requestParams["requestBody"]);

    if (requestBody == nullptr || !requestBody->isMember("username"))
    {
        callback(badRequestResponse(k400BadRequest));
        return;
    }

    std::string fileMd5 = "";
    if (requestParser.getFiles().size() == 1)
    {
        auto &file = requestParser.getFiles()[0];
        fileMd5 = file.getMd5();
        file.save();
    }

    (*requestBody)["avatarUrl"] = fileMd5;

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

void usernameCheck(const HttpRequestPtr &request, Callback &&callback)
{
    auto requestBody = request->getJsonObject();

    if (requestBody == nullptr || !requestBody->isMember("username"))
    {
        callback(badRequestResponse(k400BadRequest));
        return;
    }

    auto result = UserTable::isUsernameExist(requestBody->get("username", "").asString());

    Json::Value jsonBody;
    if (result == nullptr)
    {
        jsonBody["error"] = "Internal error.";
        auto response = HttpResponse::newHttpJsonResponse(jsonBody);
        response->setStatusCode(k500InternalServerError);

        callback(response);
        return;
    }

    jsonBody["value"] = *result;
    auto response = HttpResponse::newHttpJsonResponse(jsonBody);

    callback(response);
}

void signInHandler(const HttpRequestPtr &request, Callback &&callback)
{
    auto requestBody = request->getJsonObject();

    Json::Value jsonBody;
    if (requestBody == nullptr || !requestBody->isMember("username") || !requestBody->isMember("password"))
    {
        callback(badRequestResponse(k400BadRequest));
        return;
    }

    auto response = HttpResponse::newHttpJsonResponse(jsonBody);

    callback(response);
}