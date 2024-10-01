#include "Handlers.h"

using namespace drogon;

void indexHandler(const HttpRequestPtr &request, Callback &&callback)
{
    Json::Value jsonBody;
    jsonBody["message"] = "Hello, drogon!";
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
        jsonBody["message"] = "field `name` is required";

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