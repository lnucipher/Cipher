using Chat.Domain.Abstractions.IProviders;
using Microsoft.AspNetCore.Http;

namespace Chat.Infrastructure.Providers
{
    public class UserContextProvider(IHttpContextAccessor httpContextAccessor) : IUserContextProvider
    {
        public string? GetTokenFromRequest()
        {
            var authorizationHeader = httpContextAccessor.HttpContext?.Request.Headers["Authorization"].ToString();

            if (!string.IsNullOrEmpty(authorizationHeader) && authorizationHeader.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase))
            {
                return authorizationHeader["Bearer ".Length..].Trim();
            }

            return null;
        }
    }
}