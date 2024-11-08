#pragma once
#include "UserTable.h"

bool isBirthDateValid(const std::string& dateStr);
const std::string formatToDatetime(const std::string& timestamp);
bool isStatusValid(const std::string& status);
std::string toUppercase(const std::string& str);
std::shared_ptr<Json::Value> isRealUser(const std::string &userId);