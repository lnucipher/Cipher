#include "User.h"

using namespace drogon;

inline std::shared_ptr<std::string> User::isFieldValid(const std::shared_ptr<Json::Value> requestBody,
                                                       const std::string &fieldName)
{
    if (!requestBody->isMember(fieldName))
    {
        auto errorResponse = std::make_shared<std::string>("field " + fieldName + " is required");
        return errorResponse;
    }

    return nullptr;
}

std::shared_ptr<std::string> User::areFieldsValid(const std::shared_ptr<Json::Value> requestBody)
{
    for (const auto fieldName : requestFields)
    {
        auto result = isFieldValid(requestBody, fieldName);
        if (result != nullptr)
        {
            return result;
        }
    }

    return nullptr;
}
