#include "ApiUtils.h"
#include <fstream>

inline const std::string jwtSecret = setJwtSecretKey();
inline constexpr unsigned int tokenDuration = 7 * 24 * 60 * 60;
const std::regex uuidRegex(
    R"([a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[1-5][a-fA-F0-9]{3}-[89abAB][a-fA-F0-9]{3}-[a-fA-F0-9]{12})");


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
                                                  std::shared_ptr<std::string[]> avatarPath /*=nullptr*/)
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
        avatarPath[0] = file.getFileName();
        avatarPath[1] = std::string(file.getFileExtension());

        if (avatarPath[0].empty() || avatarPath[1].empty())
        {
            return nullptr;
        }

        file.save();
    }
    else if (isFileAvailable && !isDirectoryAvailable)
    {
        return nullptr;
    }

    auto requestParams = requestParser.getParameters();
    return readMultiPartParams(requestParams["requestBody"]);
}

void rmAvatar(const std::string &filePath)
{
    if (filePath.empty())
    {
        return;
    }

    if (std::regex_search(filePath, uuidRegex))
    {
        std::remove(std::string("." + filePath).c_str());
    }
}

/// Response
drogon::HttpResponsePtr errorResponse(drogon::HttpStatusCode statusCode, const std::string &errorMessage)
{
    Json::Value jsonBody;
    jsonBody["error"] = errorMessage;
    auto response = drogon::HttpResponse::newHttpJsonResponse(jsonBody);
    response->setStatusCode(statusCode);
    return response;
}

drogon::HttpResponsePtr internalErrorResponse()
{
    return errorResponse(drogon::k500InternalServerError, "Internal error.");
}


/// JWT
const std::string setJwtSecretKey()
{
    std::ifstream jwtCreds("/run/secrets/jwt-secret-key");

    if (!jwtCreds.is_open())
    {
        LOG_FATAL << "Failed to open .jwt-secret file";
        #if defined(NDEBUG)
        abort();
        #endif
    }

    std::string secret;
    std::getline(jwtCreds, secret);
    jwtCreds.close();

    return secret;
}

const jwt::traits::kazuho_picojson::string_type genJwtToken(const std::string &userIdClaim)
{
    return jwt::create()
        .set_type("JWT")
        .set_issuer("identity.service")
        .set_payload_claim("userId", jwt::claim(userIdClaim))
        .set_issued_now()
        .set_expires_in(std::chrono::seconds{tokenDuration}) // 7 days
        .sign(jwt::algorithm::hs512{jwtSecret});
}

const std::string stripAuthToken(const std::string &token)
{
    size_t pos = token.find(' ');
    if (pos != std::string::npos)
    {
        return token.substr(pos + 1);
    }
    else
    {
        return "";
    }
}

void verifyJwt(const std::string &token, const std::string &userId)
{
    auto verifier = jwt::verify()
        .with_issuer("identity.service")
        .with_claim("userId", jwt::claim(userId))
        .allow_algorithm(jwt::algorithm::hs512{jwtSecret});

    auto decoded_token = jwt::decode(token);
    verifier.verify(decoded_token);
}