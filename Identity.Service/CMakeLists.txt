cmake_minimum_required(VERSION 3.5)
project(Identity_Service CXX)

set(CMAKE_CXX_STANDARD 20)

set(srcs
    "src/main.cpp"
    "src/Handlers.cpp")

add_executable(Identity_Service "${srcs}")

find_package(PostgreSQL REQUIRED)
find_package(Drogon CONFIG REQUIRED)

target_link_libraries(${PROJECT_NAME} PRIVATE PostgreSQL::PostgreSQL Drogon::Drogon)