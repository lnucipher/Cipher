using System.Text;
using System.Text.Json;
using Carter;
using Chat.Api.DelegatingHandlers;
using Chat.Application.Converters;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Http.Json;
using Microsoft.AspNetCore.HttpLogging;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;

namespace Chat.Api;

public static class DependencyInjection
{
    public static IServiceCollection AddApiServices(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddCarter();
        services.AddEndpointsApiExplorer();
        services.AddSwaggerGen(options =>
        {
            options.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme()
            {
                Name = "Authorization",
                In = ParameterLocation.Header,
                Type = SecuritySchemeType.Http,
                Scheme = "Bearer"
            });

            options.AddSecurityRequirement(new OpenApiSecurityRequirement
            {
                {
                    new OpenApiSecurityScheme
                    {
                        Reference = new OpenApiReference
                        {
                            Type = ReferenceType.SecurityScheme,
                            Id = "Bearer"
                        }
                    },
                    Array.Empty<string>()
                }
            });
        });
        
        services.AddHttpContextAccessor();

        AddDefaultCorsPolicy(services);
        ConfigureJwtAuthentication(services, configuration);

        services.AddTransient<JwtAuthorizationHandler>();
        services.AddTransient<LoggingHandler>();

        services.AddHttpClient("Identity.Service",
                client =>
                {
                    client.BaseAddress = new Uri(configuration.GetConnectionString("IdentityConnection") ??
                                                 throw new ArgumentException("IdentityService URL is not found."));
                })
            .AddHttpMessageHandler<JwtAuthorizationHandler>()
            .AddHttpMessageHandler<LoggingHandler>();

        services.AddHttpLogging(options =>
        {
            options.RequestHeaders.Add("Authorization");
            options.RequestBodyLogLimit = 4096;
            options.ResponseBodyLogLimit = 4096;
            options.LoggingFields = HttpLoggingFields.RequestHeaders | HttpLoggingFields.ResponseHeaders |
                                    HttpLoggingFields.RequestBody | HttpLoggingFields.ResponseBody;
        });
        
        services.Configure<JsonOptions>(options =>
        {
            options.SerializerOptions.Converters.Add(new DateTimeOffsetConverter());
            options.SerializerOptions.Converters.Add(new UppercaseGuidConverter());
            options.SerializerOptions.PropertyNamingPolicy = JsonNamingPolicy.CamelCase;
        });

        return services;
    }

    private static void AddDefaultCorsPolicy(IServiceCollection services)
    {
        services.AddCors(options =>
        {
            options.AddDefaultPolicy(builder =>
            {
                builder.AllowAnyOrigin()
                    .AllowAnyHeader()
                    .AllowAnyMethod();
            });
        });
    }

    private static void ConfigureJwtAuthentication(IServiceCollection services, IConfiguration configuration)
    {
        var issuer = configuration["Jwt:Issuer"];
        var key = GetJwtSecretKey(configuration);

        services.AddAuthentication(options =>
            {
                options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
                options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
            })
            .AddJwtBearer(options =>
            {
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = true,
                    ValidateAudience = false,
                    ValidateLifetime = true,
                    ValidateIssuerSigningKey = true,
                    ValidIssuer = issuer,
                    IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key) ??
                                                                throw new ArgumentException(
                                                                    "JWT Secret Key is not found."))
                };

                options.Events = new JwtBearerEvents
                {
                    OnMessageReceived = context =>
                    {
                        var accessToken = context.Request.Query["access_token"];

                        var path = context.HttpContext.Request.Path;
                        if (!string.IsNullOrEmpty(accessToken) &&
                            (path.StartsWithSegments("/api")))
                        {
                            context.Token = accessToken;
                        }

                        return Task.CompletedTask;
                    }
                };
            });

        services.AddAuthorization();
    }

    private static string GetJwtSecretKey(IConfiguration configuration)
    {
        var secretKeyPath = configuration["Jwt:Key"];
        return File.ReadAllText(secretKeyPath);
    }
}