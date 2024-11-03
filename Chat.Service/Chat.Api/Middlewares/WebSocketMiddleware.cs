namespace Chat.Api.Middlewares
{
    public class WebSocketsMiddleware(RequestDelegate next)
    {
        public async Task Invoke(HttpContext httpContext)
        {
            var request = httpContext.Request;
            
            if (request.Path.StartsWithSegments("/api/chat-hub", StringComparison.OrdinalIgnoreCase) &&
                request.Query.TryGetValue("access_token", out var accessToken))
            {
                request.Headers.Append("Authorization", $"Bearer {accessToken}");
            }

            await next(httpContext);
        }
    }
}