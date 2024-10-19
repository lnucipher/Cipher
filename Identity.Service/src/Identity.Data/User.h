#pragma once
#include <drogon/drogon.h>

class User
{
public:
    // User() = delete;

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

private:
    static inline constexpr int fieldsNumber = 6;

protected:
    static inline const std::array<std::string, fieldsNumber> requestFields =
        {"username", "name", "bio", "password", "birthDate", "avatarUrl"};
    std::unordered_map<std::string, std::string> fieldMap;
};