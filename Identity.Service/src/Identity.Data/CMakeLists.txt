cmake_minimum_required(VERSION 3.5)
project(Identity_Data LANGUAGES CXX VERSION 1.0)

set(CMAKE_CXX_STANDARD 20)

set(srcs
    "ContactTable.cpp"
    "DataUtils.cpp"
    "User.cpp"
    "UserTable.cpp")

add_library(${PROJECT_NAME} "${srcs}")

target_include_directories(${PROJECT_NAME} PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})

find_package(PostgreSQL REQUIRED)
find_package(Drogon CONFIG REQUIRED)

target_link_libraries(${PROJECT_NAME} PRIVATE PostgreSQL::PostgreSQL Drogon::Drogon)