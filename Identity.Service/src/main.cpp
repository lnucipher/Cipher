#include "Handlers.h"

using namespace drogon;

int main()
{
    app()
        .loadConfigFile("./Identity.Service/config.json")
        .registerHandler("/", &indexHandler, {Get})
        .registerHandler("/", &nameHandler, {Post})
        .run();

    return EXIT_SUCCESS;
}