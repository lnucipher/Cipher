#include "DataUtils.h"

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
    return difference > 0 && difference < 120;
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

    if (tmp.size() != 4)
    {
        std::string newDate = reformatDate(dateStr);
        if (newDate.empty())
        {
            return false;
        }

        in.clear();
        in.str(newDate);
    }
    else
    {
        in.clear();
        in.str(dateStr);
    }

    date::year_month_day ymd;
    in >> date::parse("%F", ymd);  // %F is the format specifier for "YYYY-MM-DD"

    if (in.fail() || !ymd.ok() || !isBirthYearValid(ymd))
    {
        return false;
    }

    return true;
}