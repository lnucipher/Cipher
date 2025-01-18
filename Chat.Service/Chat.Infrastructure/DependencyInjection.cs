using Chat.Domain.Abstractions;
using Chat.Domain.Abstractions.IServices;
using Chat.Infrastructure.Data;
using Chat.Infrastructure.Repositories;
using Chat.Infrastructure.Services;
using FirebaseAdmin;
using Google.Apis.Auth.OAuth2;

namespace Chat.Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructureServices(this IServiceCollection services, 
        IConfiguration conf)
    {
        var connectionString = conf.GetConnectionString("DefaultConnection");

        AddServices(services);
        AddDbContext(services, connectionString);
        MigrateDatabase(services);
        CreateFirebaseApp();
        
        return services;
    }

    private static void AddServices(IServiceCollection services)
    {
        services.AddScoped<IMessageService, MessageService>();
        services.AddScoped<IUnitOfWork, UnitOfWork>();
        services.AddScoped<IUserService, UserService>();
        services.AddScoped<IPushNotificationService, PushNotificationService>();
        services.AddSingleton<IEncryptionService, SymmetricEncryptionService>();
        services.AddSignalR();
    }

    private static void AddDbContext(IServiceCollection services, string? connectionString)
    {
        services.AddDbContext<ApplicationDbContext>(options =>
        {
            options.UseNpgsql(connectionString);
        });
    }

    private static void MigrateDatabase(IServiceCollection services)
    {
        using var scope = services.BuildServiceProvider().CreateScope();
        var dbContext = scope.ServiceProvider.GetRequiredService<ApplicationDbContext>();
        dbContext.Database.Migrate();
    }

    private static void CreateFirebaseApp()
    {
        FirebaseApp.Create(new AppOptions()
        {
            Credential = GoogleCredential.FromFile(Environment.GetEnvironmentVariable("GOOGLE_APPLICATION_CREDENTIALS")),
        });
    }
}