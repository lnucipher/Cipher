#include "ApiUtils.h"
#include <fstream>

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

const std::string setJwtSecretKey()
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

drogon::HttpResponsePtr badRequestResponse(drogon::HttpStatusCode statusCode)
{
    Json::Value jsonBody;
    jsonBody["error"] = "Invalid input";
    auto response = drogon::HttpResponse::newHttpJsonResponse(jsonBody);
    response->setStatusCode(statusCode);
    return response;
}

const std::shared_ptr<Json::Value> getRequestData(const drogon::HttpRequestPtr &request,
                                                  std::string *avatarPath /*=nullptr*/)
{
    if (request == nullptr)
    {
        return nullptr;
    }

    if (request->getContentType() != drogon::CT_MULTIPART_FORM_DATA)
    {
        return request->getJsonObject();
    }

    drogon::MultiPartParser requestParser;

    if (requestParser.parse(request) != 0 || requestParser.getFiles().size() > 1)
    {
        return nullptr;
    }

    const bool isFileAvailable = requestParser.getFiles().size() == 1;
    const bool isDirectoryAvailable = avatarPath != nullptr;
    if (isFileAvailable && isDirectoryAvailable)
    {
        auto &file = requestParser.getFiles()[0];
        *avatarPath = file.getFileName();
        file.save();
    }
    else if (isFileAvailable != isDirectoryAvailable)
    {
        return nullptr;
    }

    auto requestParams = requestParser.getParameters();
    return readMultiPartParams(requestParams["requestBody"]);
}