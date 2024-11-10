namespace Chat.Api.DelegatingHandlers;

public class JwtAuthorizationHandler(IHttpContextAccessor httpContextAccessor) : DelegatingHandler
{
    protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
    {
        var token = httpContextAccessor.HttpContext?.Request.Headers.Authorization.ToString();

        if (string.IsNullOrEmpty(token))
        {
            token = httpContextAccessor.HttpContext?.Request.Query["access_token"].ToString();
            if (!string.IsNullOrEmpty(token))
            {
                token = $"Bearer {token}";
            }
        }

        if (!string.IsNullOrEmpty(token))
        {
            request.Headers.Add("Authorization", token);
        }

        return base.SendAsync(request, cancellationToken);
    }
}
