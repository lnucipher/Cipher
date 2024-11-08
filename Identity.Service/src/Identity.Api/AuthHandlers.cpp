#include "ApiUtils.h"
#include "Handlers.h"
#include "UserTable.h"

#include "bcrypt/BCrypt.hpp"

using namespace drogon;

void signUpHandler(const HttpRequestPtr &request, Callback &&callback)
{
    std::shared_ptr<std::string[]> avatarFile(new std::string[2]);
    const auto requestBody = getRequestData(request, avatarFile);

    if (requestBody == nullptr || !requestBody->isMember("username"))
    {
        callback(errorResponse(k400BadRequest));
        return;
    }

    std::string avatarPath = "";
    if (!avatarFile[0].empty())
    {
        avatarPath = "/uploads/" + utils::getUuid() + "." + avatarFile[1];
        std::rename(std::string("./uploads/" + avatarFile[0]).c_str(), std::string("." + avatarPath).c_str());
    }

    (*requestBody)["avatarUrl"] = avatarPath;

    auto errorMessage = User::areFieldsValid(requestBody);
    if (errorMessage != nullptr)
    {
        rmAvatar((*requestBody)["avatarUrl"].asString());
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
            rmAvatar((*requestBody)["avatarUrl"].asString());
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
            rmAvatar((*requestBody)["avatarUrl"].asString());
            callback(internalErrorResponse());
            return;
        }

        (*responseJson)["token"] = genJwtToken(userData.getId());

        auto response = HttpResponse::newHttpJsonResponse(*responseJson);
        callback(response);
        return;
    }
    catch (const std::exception& e)
    {
        rmAvatar((*requestBody)["avatarUrl"].asString());
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
        callback(errorResponse(k400BadRequest));
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
    const auto requestBody = getRequestData(request);

    if (requestBody == nullptr || !requestBody->isMember("username") || !requestBody->isMember("password"))
    {
        callback(errorResponse(k400BadRequest));
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
        callback(errorResponse(k401Unauthorized, "Password is not correct."));
        return;
    }

    userData["token"] = genJwtToken(userData["id"].asString());

    userData.removeMember("passwordHash");
    userData.removeMember("lastSeen");
    userData.removeMember("status");

    auto response = HttpResponse::newHttpJsonResponse(userData);
    callback(response);
}