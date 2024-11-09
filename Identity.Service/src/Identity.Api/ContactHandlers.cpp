#include "ApiUtils.h"
#include "ContactTable.h"
#include "Handlers.h"

using namespace drogon;

void addContactHandler(const HttpRequestPtr &request, Callback &&callback)
{
    const auto requestBody = getRequestData(request);

    if (requestBody == nullptr
        || !requestBody->isMember("primaryUserId")
        || !requestBody->isMember("secondaryUserId"))
    {
        callback(errorResponse(k400BadRequest));
        return;
    }

    try
    {
        verifyJwt(stripAuthToken(request->getHeader("authorization")),
                  (*requestBody)["primaryUserId"].asString());
    }
    catch (const std::exception& e)
    {
        callback(errorResponse(k401Unauthorized, e.what()));
        return;
    }

    auto result = ContactTable::addNewContact((*requestBody)["primaryUserId"].asString(),
                                              (*requestBody)["secondaryUserId"].asString());

    if (result == nullptr)
    {
        callback(internalErrorResponse());
        return;
    }

    if (result->isMember("error"))
    {
        callback(errorResponse(k403Forbidden, (*result)["error"].asString()));
        return;
    }

    auto response = HttpResponse::newHttpJsonResponse(*result);
    callback(response);
}

void updateContactInteractHandler(const drogon::HttpRequestPtr &request, Callback &&callback)
{
    const auto requestBody = getRequestData(request);

    if (requestBody == nullptr
        || !requestBody->isMember("primaryUserId")
        || !requestBody->isMember("secondaryUserId")
        || !requestBody->isMember("timestamp"))
    {
        callback(errorResponse(k400BadRequest));
        return;
    }

    try
    {
        verifyJwt(stripAuthToken(request->getHeader("authorization")),
                  (*requestBody)["primaryUserId"].asString());
    }
    catch (const std::exception& e)
    {
        callback(errorResponse(k401Unauthorized, e.what()));
        return;
    }

    auto result = ContactTable::updateLastInteract((*requestBody)["primaryUserId"].asString(),
                                                   (*requestBody)["secondaryUserId"].asString(),
                                                   (*requestBody)["timestamp"].asString());

    if (result == nullptr || result->empty())
    {
        callback(internalErrorResponse());
        return;
    }

    Json::Value jsonBody;
    jsonBody["timestamp"] = *result;
    auto response = HttpResponse::newHttpJsonResponse(jsonBody);
    callback(response);
    return;
}

void getContactsHandler(const drogon::HttpRequestPtr &request,
                        Callback &&callback,
                        std::string &&userId,
                        unsigned int &&pageSize,
                        unsigned int &&page)
{
    if (userId.empty() || pageSize <= 0 || page <= 0)
    {
        callback(errorResponse(k400BadRequest));
        return;
    }

    try
    {
        verifyJwt(stripAuthToken(request->getHeader("authorization")), userId);
    }
    catch (const std::exception& e)
    {
        callback(errorResponse(k401Unauthorized, e.what()));
        return;
    }

    auto result = ContactTable::getLastContactsForUser(userId, pageSize, (page - 1) * pageSize);

    if (result == nullptr || !result->isMember("items"))
    {
        callback(internalErrorResponse());
        return;
    }

    auto response = HttpResponse::newHttpJsonResponse(*result);
    callback(response);
}

void deleteContactHandler(const drogon::HttpRequestPtr &request,
                          Callback &&callback,
                          std::string &&primaryUserId,
                          std::string &&secondaryUserId)
{
    if (primaryUserId.empty() || secondaryUserId.empty())
    {
        callback(errorResponse(k400BadRequest));
        return;
    }

    try
    {
        verifyJwt(stripAuthToken(request->getHeader("authorization")), primaryUserId);
    }
    catch (const std::exception& e)
    {
        callback(errorResponse(k401Unauthorized, e.what()));
        return;
    }

    auto contactId = ContactTable::getIdByContact(primaryUserId, secondaryUserId);

    if (contactId == nullptr)
    {
        callback(internalErrorResponse());
        return;
    }
    else if (contactId->empty())
    {
        callback(errorResponse(k404NotFound, "Contact not found."));
        return;
    }

    auto result = ContactTable::deleteContact(*contactId);

    if (result == nullptr || !(*result))
    {
        callback(internalErrorResponse());
        return;
    }

    Json::Value jsonBody;
    jsonBody["value"] = *result;
    auto response = HttpResponse::newHttpJsonResponse(jsonBody);
    callback(response);
}