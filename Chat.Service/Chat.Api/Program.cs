using Carter;
using Chat.Api;
using Chat.Api.Middleware;
using Chat.Application;
using Chat.Infrastructure;
using Chat.Infrastructure.Hubs;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services
    .AddApiServices(builder.Configuration)
    .AddInfrastructureServices(builder.Configuration)
    .AddApplicationServices();

var app = builder.Build();

// Configure the HTTP request pipeline.

app.UseSwagger();
app.UseSwaggerUI(options =>
{
    options.SwaggerEndpoint("/swagger/v1/swagger.json", "Chat API");
    options.RoutePrefix = string.Empty;
});

app.UseHttpLogging();
app.UseCors();

app.UseAuthentication();
app.UseAuthorization();

app.UseMiddleware<ExceptionHandlingMiddleware>();

app.MapCarter();
app.MapHub<ChatHub>("/api/chat-hub");
app.Run();