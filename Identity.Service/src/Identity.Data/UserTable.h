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

    static void create();

public:
    UserTable(const std::shared_ptr<Json::Value> requestBody);

    static const std::shared_ptr<bool> isUsernameExist(const std::string& username);
    static const std::shared_ptr<bool> isUserExist(const std::string& userId);
    static const std::shared_ptr<std::string> getUserId(const std::string& username);
    static std::shared_ptr<Json::Value> getUserByUsername(const std::string& username);
    static std::shared_ptr<Json::Value> searchUsersWithContactCheck(const std::string &requestorUserId,
                                                                    const std::string &searchUsername);

    const std::shared_ptr<bool> isUsernameExist();
    std::shared_ptr<Json::Value> addNewUser();

    friend void serviceSetup();
};