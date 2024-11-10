cmake_minimum_required(VERSION 3.5)
project(Identity_Api LANGUAGES CXX VERSION 1.0)

set(CMAKE_CXX_STANDARD 20)

set(srcs
    "AuthHandlers.cpp"
    "ApiUtils.cpp"
    "ContactHandlers.cpp"
    "UserHandlers.cpp")

add_library(${PROJECT_NAME} "${srcs}")

target_include_directories(${PROJECT_NAME} PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})

find_package(PostgreSQL REQUIRED)
find_package(Drogon CONFIG REQUIRED)

target_link_libraries(${PROJECT_NAME} PRIVATE Identity_Data PostgreSQL::PostgreSQL Drogon::Drogon)