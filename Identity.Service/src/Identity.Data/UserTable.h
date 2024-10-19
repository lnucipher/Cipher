#pragma once
#include "User.h"

class UserTable : public User
{
public:
    // UserTable() = delete;
    UserTable(const std::shared_ptr<Json::Value> requestBody);

    static void createUserTable();
    static std::shared_ptr<bool> isUsernameExist(const std::string& username);
    static std::shared_ptr<std::string> getUserId(const std::string& username);
    static std::shared_ptr<Json::Value> getUserByUsername(const std::string& username);

    std::shared_ptr<bool> isUsernameExist();
    std::shared_ptr<Json::Value> addNewUser();
};