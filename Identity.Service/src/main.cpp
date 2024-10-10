#include "Handlers.h"

using namespace drogon;

int main()
{
    LOG_INFO << "Init";

    app()
        .loadConfigFile("./config.json")
        .registerHandler("/", &indexHandler, {Get})
        .registerHandler("/", &nameHandler, {Post})
        .setLogPath("./build/log")
        .run();

    LOG_ERROR << "End";

    return EXIT_SUCCESS;
}