using Carter;
using Chat.Api;
using Chat.Application;
using Chat.Infrastructure;
using Chat.Infrastructure.Hubs;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services
    .AddApplicationServices()
    .AddInfrastructureServices(builder.Configuration)
    .AddApiServices(builder.Configuration);

var app = builder.Build();

// Configure the HTTP request pipeline.

app.UseSwagger();
app.UseSwaggerUI(options =>
{
    options.SwaggerEndpoint("/swagger/v1/swagger.json", "Chat API");
    options.RoutePrefix = string.Empty;
});

app.MapCarter();
app.UseHttpsRedirection();
app.UseAuthentication();

app.MapControllers();
app.MapHub<ChatHub>("/api/chat-hub");
app.Run();