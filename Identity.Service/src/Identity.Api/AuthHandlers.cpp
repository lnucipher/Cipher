#include "ApiUtils.h"
#include "Handlers.h"
#include "UserTable.h"

#include "bcrypt/BCrypt.hpp"

using namespace drogon;

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
            callback(internalErrorResponse());
            return;
        }

        (*responseJson)["token"] = genJwtToken(userData.getUsername());

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


    if (result == nullptr)
    {
        callback(internalErrorResponse());
        return;
    }

    Json::Value jsonBody;
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

    userData["token"] = genJwtToken((*requestBody)["username"].asString());

    userData.removeMember("passwordHash");
    userData.removeMember("lastSeen");
    userData.removeMember("status");

    auto response = HttpResponse::newHttpJsonResponse(userData);
    callback(response);
}