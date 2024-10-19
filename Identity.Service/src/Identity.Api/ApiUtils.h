#pragma once
#include <drogon/drogon.h>

const std::shared_ptr<Json::Value> readMultiPartParams(const std::string &params);
const std::string setJwtSecretKey();