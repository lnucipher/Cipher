#pragma once
#include <drogon/drogon.h>

bool isBirthDateValid(const std::string& dateStr);
const std::string formatToDatetime(const std::string& timestamp);
bool isValidTimestamp(const std::string& str);
const std::string formatToTimestamp(const std::string& datetime);
bool isStatusValid(const std::string& status);
std::string toUppercase(const std::string& str);
std::shared_ptr<Json::Value> isRealUser(const std::string &userId);