cmake_minimum_required(VERSION 3.5)
project(Identity_Service LANGUAGES CXX VERSION 1.0)

set(CMAKE_CXX_STANDARD 20)

include(FetchContent)

FetchContent_Declare(
    bcrypt
    GIT_REPOSITORY https://github.com/trusch/libbcrypt.git
    UPDATE_DISCONNECTED 1
)

FetchContent_Declare(
    jwt-cpp
    GIT_REPOSITORY https://github.com/Thalhammer/jwt-cpp.git
    UPDATE_DISCONNECTED 1
)

FetchContent_Declare(
    date
    GIT_REPOSITORY https://github.com/HowardHinnant/date.git
    UPDATE_DISCONNECTED 1
)

FetchContent_MakeAvailable(bcrypt)
FetchContent_MakeAvailable(jwt-cpp)
FetchContent_MakeAvailable(date)

add_executable(Identity_Service main.cpp)

include_directories(${bcrypt_SOURCE_DIR}/include)
include_directories(${jwt-cpp_SOURCE_DIR}/include)
include_directories(${date_SOURCE_DIR}/include)

add_subdirectory(Identity.Api)
add_subdirectory(Identity.Data)

add_dependencies(${PROJECT_NAME} bcrypt)
add_dependencies(${PROJECT_NAME} jwt-cpp)
add_dependencies(${PROJECT_NAME} date)
find_package(PostgreSQL REQUIRED)
find_package(Drogon CONFIG REQUIRED)

target_link_libraries(${PROJECT_NAME} PRIVATE Identity_Api Identity_Data)
target_link_libraries(${PROJECT_NAME} PRIVATE PostgreSQL::PostgreSQL Drogon::Drogon bcrypt jwt-cpp date)