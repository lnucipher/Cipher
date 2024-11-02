#include "ApiUtils.h"
#include "Handlers.h"
#include "UserTable.h"

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