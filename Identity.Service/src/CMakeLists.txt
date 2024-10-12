cmake_minimum_required(VERSION 3.5)
project(Identity_Service LANGUAGES CXX VERSION 1.0)

set(CMAKE_CXX_STANDARD 20)

add_executable(Identity_Service main.cpp)

add_subdirectory(Identity.Api)
add_subdirectory(Identity.Data)

find_package(PostgreSQL REQUIRED)
find_package(Drogon CONFIG REQUIRED)

target_link_libraries(${PROJECT_NAME} PRIVATE Identity_Api Identity_Data)
target_link_libraries(${PROJECT_NAME} PRIVATE PostgreSQL::PostgreSQL Drogon::Drogon)