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