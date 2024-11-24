var builder = WebApplication.CreateBuilder(args);

builder.Services.AddEndpointsApiExplorer();
var config = builder.Configuration.GetSection("ReverseProxy");
builder.Services.AddReverseProxy()
    .LoadFromConfig(config);

var app = builder.Build();

app.UseHttpsRedirection();
app.MapReverseProxy();

app.Run();