#pragma once
#include <drogon/drogon.h>

class User
{
private:
    static inline constexpr int fieldsNumber = 6;

protected:
    User() = default;
    User(const User&) = delete;
    User(User&&) = delete;
    User& operator=(const User&) = delete;
    User& operator=(User&&) = delete;

    static inline const std::array<std::string, fieldsNumber> requestFields =
        {"username", "name", "bio", "password", "birthDate", "avatarUrl"};
    std::unordered_map<std::string, std::string> fieldMap;

public:
    // Use struct instead of enum to get already stringified status
    // as reference required by db client.
    struct Status
    {
        static inline const std::string &OFFLINE = "OFFLINE";
        static inline const std::string &ONLINE = "ONLINE";
    };

    static inline std::shared_ptr<std::string> isFieldValid(const std::shared_ptr<Json::Value> requestBody,
                                                            const std::string &fieldName);

    static std::shared_ptr<std::string> areFieldsValid(const std::shared_ptr<Json::Value> requestBody);

    const inline std::string &getId() { return fieldMap["id"]; }
    const inline std::string &getUsername() { return fieldMap["username"]; }
    const inline std::string &getName() { return fieldMap["name"]; }
    const inline std::string &getBio() { return fieldMap["bio"]; }
    const inline std::string &getPassword() { return fieldMap["password"]; }
    const inline std::string &getBirthDate() { return fieldMap["birthDate"]; }
    const inline std::string &getAvatarUrl() { return fieldMap["avatarUrl"]; }
};