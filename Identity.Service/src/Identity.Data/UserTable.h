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
    static std::shared_ptr<Json::Value> getUser(const std::string& query,
                                                const std::string& argument,
                                                std::shared_ptr<drogon::orm::Transaction>
                                                    dbTransaction = nullptr);

public:
    UserTable(const std::shared_ptr<Json::Value> requestBody, const bool newUser = true);

    static const std::shared_ptr<bool> isUsernameExist(const std::string& username);
    static const std::shared_ptr<bool> isUserExist(const std::string& userId);
    static const std::shared_ptr<std::string> getUserId(const std::string& username);
    static std::shared_ptr<Json::Value> getUserByUsername(const std::string& username);
    static std::shared_ptr<Json::Value> getUserByUserId(const std::string& userId,
                                                        std::shared_ptr<drogon::orm::Transaction>
                                                            dbTransaction = nullptr);
    static std::shared_ptr<Json::Value> searchUsersWithContactCheck(const std::string &requestorUserId,
                                                                    const std::string &searchUsername);
    static const std::shared_ptr<Json::Value> updateUserStatus(const std::string &userId,
                                                               const std::string &status,
                                                               const std::string &timestamp);
    static const std::shared_ptr<Json::Value> updateUserPassword(const std::string &userId,
                                                                 const std::string &newPasswordHash);
    static const std::shared_ptr<Json::Value> updateUserAvatarUrl(const std::string &userId,
                                                                  const std::string &avatarUrl);
    static const std::shared_ptr<Json::Value> deleteUser(const std::string &userId);

    const std::shared_ptr<bool> isUsernameExist();
    std::shared_ptr<Json::Value> addNewUser();
    std::shared_ptr<Json::Value> updateUser();

    friend void serviceSetup();
};