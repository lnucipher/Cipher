#pragma once
#include <drogon/drogon.h>
#include <jwt-cpp/jwt.h>

const std::shared_ptr<Json::Value> readMultiPartParams(const std::string &params);
const std::shared_ptr<Json::Value> getRequestData(const drogon::HttpRequestPtr &request,
                                                  std::string *avatarPath = nullptr);
drogon::HttpResponsePtr badRequestResponse(drogon::HttpStatusCode statusCode);
drogon::HttpResponsePtr internalErrorResponse();
const std::string setJwtSecretKey();
const jwt::traits::kazuho_picojson::string_type genJwtToken(std::string usernameClaim);