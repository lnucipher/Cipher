#include "Handlers.h"

#include <drogon/HttpAppFramework.h>
#include <drogon/orm/DbClient.h>

using namespace drogon;
using namespace drogon::orm;

void indexHandler(const HttpRequestPtr &request, Callback &&callback)
{
    auto client = app().getDbClient();

    if (client != nullptr)
    {
        client->execSqlAsync("SELECT version()",
                             [](const Result &r)
                             {
                                LOG_INFO << "PostgreSQL version: " << r[0]["version"].as<std::string>();
                             },
                             [](const DrogonDbException &e)
                             {
                                LOG_ERROR << "Query failed: " << e.base().what();
                             });
    }
    else
    {
        LOG_ERROR << "Client not found";
    }

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