cmake_minimum_required(VERSION 3.5)
project(Identity_Service)

set(CMAKE_CXX_STANDARD 20)

file(
        DOWNLOAD
        https://github.com/cpm-cmake/CPM.cmake/releases/download/v0.40.2/CPM.cmake
        ${CMAKE_CURRENT_BINARY_DIR}/cmake/CPM.cmake
        EXPECTED_HASH SHA256=c8cdc32c03816538ce22781ed72964dc864b2a34a310d3b7104812a5ca2d835d
)

include(${CMAKE_CURRENT_BINARY_DIR}/cmake/CPM.cmake)

CPMAddPackage(
        NAME drogon
        VERSION 1.9.7
        GITHUB_REPOSITORY drogonframework/drogon
        GIT_TAG v1.9.7
)

set(srcs
    "src/main.cpp"
    "src/Handlers.cpp")

add_executable(Identity_Service "${srcs}")

target_link_libraries(${PROJECT_NAME} PUBLIC drogon)