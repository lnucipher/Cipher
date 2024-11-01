#pragma once
#include "User.h"

class UserTable : public User
{
private:
    UserTable() = default;
    UserTable(const UserTable&) = delete;
    UserTable(UserTable&&) = delete;
    UserTable& operator=(const UserTable&) = delete;
    UserTable& operator=(UserTable&&) = delete;

public:
    UserTable(const std::shared_ptr<Json::Value> requestBody);

    static void createUserTable();
    static const std::shared_ptr<bool> isUsernameExist(const std::string& username);
    static const std::shared_ptr<bool> isUserExist(const std::string& userId);
    static const std::shared_ptr<std::string> getUserId(const std::string& username);
    static std::shared_ptr<Json::Value> getUserByUsername(const std::string& username);

    const std::shared_ptr<bool> isUsernameExist();
    std::shared_ptr<Json::Value> addNewUser();
};