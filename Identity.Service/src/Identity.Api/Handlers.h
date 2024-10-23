#pragma once
#include <drogon/drogon.h>
#include <stdlib.h>

using Callback = std::function<void (const drogon::HttpResponsePtr &)>;

void signUpHandler(const drogon::HttpRequestPtr &request, Callback &&callback);
void signInHandler(const drogon::HttpRequestPtr &request, Callback &&callback);
void usernameCheck(const drogon::HttpRequestPtr &request, Callback &&callback, std::string &&username);

class AccessFilter : public drogon::HttpFilter<AccessFilter>
{
public:
    void doFilter(const drogon::HttpRequestPtr &req,
                  drogon::FilterCallback &&fcb,
                  drogon::FilterChainCallback &&fccb) override
    {
        auto resp = drogon::HttpResponse::newHttpResponse();
        resp->removeHeader("Access-Control-Allow-Origin");
        fccb();
    }
};