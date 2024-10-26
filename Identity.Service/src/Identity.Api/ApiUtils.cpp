#include "ApiUtils.h"
#include <fstream>

inline const std::string jwtSecret = setJwtSecretKey();
inline constexpr unsigned int tokenDuration = 7 * 24 * 60 * 60;

/// Request
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

/// Response
drogon::HttpResponsePtr badRequestResponse(drogon::HttpStatusCode statusCode)
{
    Json::Value jsonBody;
    jsonBody["error"] = "Invalid input";
    auto response = drogon::HttpResponse::newHttpJsonResponse(jsonBody);
    response->setStatusCode(statusCode);
    return response;
}

drogon::HttpResponsePtr internalErrorResponse()
{
    Json::Value jsonBody;
    jsonBody["error"] = "Internal error.";
    auto response = drogon::HttpResponse::newHttpJsonResponse(jsonBody);
    response->setStatusCode(drogon::k500InternalServerError);
    return response;
}


/// JWT
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

const jwt::traits::kazuho_picojson::string_type genJwtToken(std::string usernameClaim)
{
    return jwt::create()
        .set_type("JWT")
        .set_issuer("identity.service")
        .set_payload_claim("username", jwt::claim(usernameClaim))
        .set_issued_now()
        .set_expires_in(std::chrono::seconds{tokenDuration})
        .sign(jwt::algorithm::hs512{jwtSecret});
}