#include <drogon/HttpFilter.h>

class CorsFilter : public drogon::HttpFilter<CorsFilter, false>
{
public:
    CorsFilter() { }

    void doFilter(const drogon::HttpRequestPtr &req,
                        drogon::FilterCallback &&fcb,
                        drogon::FilterChainCallback &&fccb) override
    {
        if (req->method() == drogon::Options)
        {
            auto resp = drogon::HttpResponse::newHttpResponse();
            auto &origin = req->getHeader("Origin");

            resp->addHeader("Access-Control-Allow-Origin", origin);
            resp->addHeader("Access-Control-Allow-Methods", "OPTIONS,POST,GET,DELETE,PATCH");
            resp->addHeader("Access-Control-Allow-Headers", "authorization,x-requested-with,content-type");
            resp->addHeader("Access-Control-Allow-Credentials","true");

            fcb(resp);
            return;
        }

        fccb();
    }
};