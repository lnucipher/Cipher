using System.Text;
using Carter;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;

namespace Chat.Api;

public static class DependencyInjection
{
    public static IServiceCollection AddApiServices(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddCarter();
        services.AddControllers();
        services.AddEndpointsApiExplorer();
        services.AddSwaggerGen();
        
        ConfigureJwtAuthentication(services, configuration);

        return services;
    }

    private static void ConfigureJwtAuthentication(IServiceCollection services, IConfiguration configuration)
    {
        var issuer = configuration["Jwt:Issuer"];
        var key = configuration["Jwt:Key"];

        services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            .AddJwtBearer(options =>
            {
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = true,
                    ValidIssuer = issuer,

                    ValidateLifetime = true,
                    
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key)),
                    
                    RequireSignedTokens = true,
                    ValidAlgorithms = new[] { SecurityAlgorithms.HmacSha512 }
                };

                options.Events = new JwtBearerEvents
                {
                    OnMessageReceived = ResolveTokenFromQuery
                };
            });
    }

    private static Task ResolveTokenFromQuery(MessageReceivedContext context)
    {
        var accessToken = context.Request.Query["access_token"];
        if (!string.IsNullOrEmpty(accessToken))
        {
            context.Token = accessToken;
        }
        
        return Task.CompletedTask;
    }
}