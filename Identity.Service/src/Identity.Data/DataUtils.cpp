#include "DataUtils.h"
#include "UserTable.h"

#include "date/date.h"

#include <iomanip>

/// @brief Reformat date to "YYYY-MM-DD" from "MM-DD-YYYY"
std::string reformatDate(const std::string& dateStr)
{
    int month, day, year;

    std::istringstream dateStream(dateStr);
    char delimiter1, delimiter2;

    if (dateStream >> month >> delimiter1 >> day >> delimiter2 >> year)
    {
        if (delimiter1 == '-' && delimiter2 == '-')
        {
            std::ostringstream formattedDate;
            formattedDate << std::setw(4) << std::setfill('0') << year << "-"
                          << std::setw(2) << std::setfill('0') << month << "-"
                          << std::setw(2) << std::setfill('0') << day;
            return formattedDate.str();
        }
    }

    return "";
}

bool isBirthYearValid(const date::year_month_day& ymd)
{
    const auto now = std::chrono::system_clock::now();
    const auto today = date::year_month_day{std::chrono::floor<date::days>(now)};
    const int difference = static_cast<int>(today.year()) - static_cast<int>(ymd.year());
    return difference >= 0 && difference <= 120;
}

bool isBirthDateValid(const std::string& dateStr)
{
    if (dateStr.empty())
    {
        return false;
    }

    std::istringstream in{dateStr};

    std::string tmp;
    if (!std::getline(in, tmp, '-'))
    {
        return false;
    }

    if (tmp.size() == 2)
    {
        std::string newDate = reformatDate(dateStr);
        if (newDate.empty())
        {
            return false;
        }

        in.clear();
        in.str(newDate);
    }
    else if (tmp.size() == 4)
    {
        in.clear();
        in.str(dateStr);
    }
    else
    {
        return false;
    }

    date::year_month_day ymd;
    in >> date::parse("%F", ymd);  // %F is the format specifier for "YYYY-MM-DD"

    if (in.fail() || !ymd.ok() || !isBirthYearValid(ymd))
    {
        return false;
    }

    return true;
}

const std::string formatToDatetime(const std::string& timestamp)
{
    // Expected format: "2024-11-05 10:20:00.666104+00"
    size_t spacePos = timestamp.find(' ');

    std::string datePart = timestamp.substr(0, spacePos);
    std::string timePart = timestamp.substr(spacePos + 1, 12);
    std::string formattedTime = datePart + "T" + timePart + "Z";

    return formattedTime;
}

bool isStatusValid(const std::string& status)
{
    return status == User::Status::OFFLINE || status == User::Status::ONLINE;
}

int charUppercase(unsigned char c)
{
    return std::toupper(c);
}

std::string toUppercase(const std::string& str)
{
    auto result = str;
    std::transform(result.begin(), result.end(), result.begin(), &charUppercase);
    return result;
}

std::shared_ptr<Json::Value> isRealUser(const std::string &userId)
{
    auto userCheck = UserTable::isUserExist(userId);
    if (userCheck == nullptr)
    {
        return nullptr;
    }

    if (!userCheck)
    {
        Json::Value response;
        response["error"] = "User " + userId + " does not exist.";
        return std::make_shared<Json::Value>(response);
    }

    Json::Value response;
    response["value"] = true;
    return std::make_shared<Json::Value>(response);
}