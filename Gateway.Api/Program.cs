using System.Net.Security;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddEndpointsApiExplorer();
var config = builder.Configuration.GetSection("ReverseProxy");
builder.Services.AddReverseProxy()
    .ConfigureHttpClient((serviceProvider, proxyClientConfig) =>
    {
        proxyClientConfig.SslOptions = new SslClientAuthenticationOptions
        {
            RemoteCertificateValidationCallback = (sender, certificate, chain, sslPolicyErrors) => true
        };
    })
    .LoadFromConfig(config);

var app = builder.Build();

app.UseHttpsRedirection();
app.MapReverseProxy();

app.Run();