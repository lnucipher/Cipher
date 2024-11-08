#include "ApiUtils.h"
#include "Handlers.h"
#include "UserTable.h"

#include "bcrypt/BCrypt.hpp"

using namespace drogon;

void findUsersWithContactCheck(const drogon::HttpRequestPtr &request,
                               Callback &&callback,
                               std::string &&requestorId,
                               std::string &&searchedUsername)
{
    if (requestorId.empty() || searchedUsername.empty())
    {
        callback(errorResponse(k400BadRequest));
        return;
    }

    try
    {
        verifyJwt(stripAuthToken(request->getHeader("authorization")), requestorId);
    }
    catch (const std::exception& e)
    {
        callback(errorResponse(k401Unauthorized, e.what()));
        return;
    }

    auto result = UserTable::searchUsersWithContactCheck(requestorId, searchedUsername);

    if (result == nullptr || !result->isMember("items"))
    {
        callback(internalErrorResponse());
        return;
    }

    auto response = HttpResponse::newHttpJsonResponse(*result);
    callback(response);
}

void updateUserStatusHandler(const drogon::HttpRequestPtr &request, Callback &&callback)
{
    auto requestBody = getRequestData(request);

    if (requestBody == nullptr
        || !requestBody->isMember("userId")
        || !requestBody->isMember("status"))
    {
        callback(errorResponse(k400BadRequest));
        return;
    }

    auto userId = (*requestBody)["userId"].asString();
    auto status = (*requestBody)["status"].asString();

    try
    {
        verifyJwt(stripAuthToken(request->getHeader("authorization")), userId);
    }
    catch (const std::exception& e)
    {
        callback(errorResponse(k401Unauthorized, e.what()));
        return;
    }

    auto result = UserTable::updateUserStatus(userId, status);

    if (result == nullptr)
    {
        callback(internalErrorResponse());
        return;
    }

    if (result->isMember("error"))
    {
        callback(errorResponse(k400BadRequest, (*result)["error"].asString()));
        return;
    }

    callback(HttpResponse::newHttpResponse());
    return;
}

void updateUserPasswordHandler(const drogon::HttpRequestPtr &request, Callback &&callback)
{
    auto requestBody = getRequestData(request);

    if (requestBody == nullptr
        || !requestBody->isMember("userId")
        || !requestBody->isMember("currentPassword")
        || !requestBody->isMember("newPassword"))
    {
        callback(errorResponse(k400BadRequest));
        return;
    }

    const auto userId = (*requestBody)["userId"].asString();

    try
    {
        verifyJwt(stripAuthToken(request->getHeader("authorization")), userId);
    }
    catch (const std::exception& e)
    {
        callback(errorResponse(k401Unauthorized, e.what()));
        return;
    }

    auto userData = UserTable::getUserByUserId(userId);

    if (userData == nullptr)
    {
        callback(internalErrorResponse());
        return;
    }

    if (userData->isMember("error"))
    {
        callback(errorResponse(k400BadRequest, (*userData)["error"].asString()));
        return;
    }

    if (!BCrypt::validatePassword((*requestBody)["currentPassword"].asString(),
                                  (*userData)["passwordHash"].asString()))
    {
        callback(errorResponse(k401Unauthorized, "Password is not correct."));
        return;
    }

    auto result = UserTable::updateUserPassword(userId,
                                                BCrypt::generateHash(
                                                    (*requestBody)["newPassword"].asString()));

    if (result == nullptr)
    {
        callback(internalErrorResponse());
        return;
    }

    if (result->isMember("error"))
    {
        callback(errorResponse(k400BadRequest, (*result)["error"].asString()));
        return;
    }

    callback(HttpResponse::newHttpResponse());
    return;
}