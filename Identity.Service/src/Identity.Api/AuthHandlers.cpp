#include "ApiUtils.h"
#include "Handlers.h"
#include "UserTable.h"

#include "bcrypt/BCrypt.hpp"
#include <jwt-cpp/jwt.h>

using namespace drogon;

inline constexpr unsigned int tokenDuration = 7 * 24 * 60 * 60;

inline const std::string jwtSecret = setJwtSecretKey();

void signUpHandler(const HttpRequestPtr &request, Callback &&callback)
{
    std::string avatarPath = "";
    const auto requestBody = getRequestData(request, &avatarPath);

    if (requestBody == nullptr || !requestBody->isMember("username"))
    {
        callback(badRequestResponse(k400BadRequest));
        return;
    }

    (*requestBody)["avatarUrl"] = avatarPath;

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

        if (!responseJson->isMember("id"))
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
            .set_expires_in(std::chrono::seconds{tokenDuration})
            .sign(jwt::algorithm::hs512{jwtSecret});

        (*responseJson)["token"] = token;

        auto response = HttpResponse::newHttpJsonResponse(*responseJson);
        callback(response);
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

void usernameCheck(const HttpRequestPtr &request, Callback &&callback, std::string &&username)
{
    if (username.empty())
    {
        callback(badRequestResponse(k400BadRequest));
        return;
    }

    auto result = UserTable::isUsernameExist(username);

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
    auto requestBody = getRequestData(request);

    if (requestBody == nullptr || !requestBody->isMember("username") || !requestBody->isMember("password"))
    {
        callback(badRequestResponse(k400BadRequest));
        return;
    }

    auto userData = *UserTable::getUserByUsername((*requestBody)["username"].asString());

    if (userData.isMember("error"))
    {
        // Prevent partial select of user data
        Json::Value jsonBody;
        jsonBody["error"] = userData["error"].asString();

        auto response = HttpResponse::newHttpJsonResponse(jsonBody);

        if (jsonBody["error"].asString() == "Internal error.")
        {
            response->setStatusCode(k500InternalServerError);
        }
        else
        {
            response->setStatusCode(k403Forbidden);
        }

        callback(response);
        return;
    }

    if (!BCrypt::validatePassword((*requestBody)["password"].asString(), userData["passwordHash"].asString()))
    {
        Json::Value jsonBody;
        jsonBody["error"] = "Password is not correct.";
        auto response = HttpResponse::newHttpJsonResponse(jsonBody);
        response->setStatusCode(k401Unauthorized);
        callback(response);
        return;
    }

    auto token = jwt::create()
        .set_type("JWT")
        .set_issuer("Identity")
        .set_payload_claim("username", jwt::claim((*requestBody)["username"].asString()))
        .set_issued_now()
        .set_expires_in(std::chrono::seconds{tokenDuration})
        .sign(jwt::algorithm::hs512{jwtSecret});

    userData["token"] = token;

    userData.removeMember("passwordHash");
    userData.removeMember("status");

    auto response = HttpResponse::newHttpJsonResponse(userData);
    callback(response);
}