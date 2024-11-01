#pragma once
#include "UserTable.h"

bool isBirthDateValid(const std::string& dateStr);
std::shared_ptr<Json::Value> isRealUser(const std::string &userId);