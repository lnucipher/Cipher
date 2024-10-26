#pragma once
#include <drogon/drogon.h>

const std::shared_ptr<Json::Value> readMultiPartParams(const std::string &params);
const std::string setJwtSecretKey();
drogon::HttpResponsePtr badRequestResponse(drogon::HttpStatusCode statusCode);
const std::shared_ptr<Json::Value> getRequestData(const drogon::HttpRequestPtr &request,
                                                  std::string *avatarPath = nullptr);