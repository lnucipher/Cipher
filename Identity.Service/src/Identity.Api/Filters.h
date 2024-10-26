#include <drogon/HttpFilter.h>

class AuthFilter : public drogon::HttpFilter<AuthFilter, false>
{
public:
    AuthFilter() { }

    void doFilter(const drogon::HttpRequestPtr &req,
                        drogon::FilterCallback &&fcb,
                        drogon::FilterChainCallback &&fccb) override
    {
        auto resp = drogon::HttpResponse::newHttpResponse();

        resp->addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        // resp->addHeader("Access-Control-Allow-Methods", "GET, POST");
        // resp->addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        // resp->addHeader("Access-Control-Allow-Credentials", "true");
        abort();

        fcb(resp);
    }
};