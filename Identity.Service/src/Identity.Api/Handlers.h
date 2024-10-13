#pragma once
#include <drogon/drogon.h>
#include <stdlib.h>

using Callback = std::function<void (const drogon::HttpResponsePtr &)>;

void signUpHandler(const drogon::HttpRequestPtr &request, Callback &&callback);

// Test handlers
void indexHandler(const drogon::HttpRequestPtr &request, Callback &&callback);
void nameHandler(const drogon::HttpRequestPtr &request, Callback &&callback);