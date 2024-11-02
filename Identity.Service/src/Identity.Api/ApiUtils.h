#pragma once
#include <drogon/drogon.h>
#include <jwt-cpp/jwt.h>

const std::shared_ptr<Json::Value> readMultiPartParams(const std::string &params);
const std::shared_ptr<Json::Value> getRequestData(const drogon::HttpRequestPtr &request,
                                                  std::shared_ptr<std::string[]> avatarPath = nullptr);
void rmAvatar(const std::string &filePath);
drogon::HttpResponsePtr errorResponse(drogon::HttpStatusCode statusCode,
                                      const std::string &errorMessage = "Invalid input.");
drogon::HttpResponsePtr internalErrorResponse();
const std::string setJwtSecretKey();
const jwt::traits::kazuho_picojson::string_type genJwtToken(const std::string &userIdClaim);
const std::string stripAuthToken(const std::string &token);
void verifyJwt(const std::string &token, const std::string &userId);