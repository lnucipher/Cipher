using Chat.Domain.Abstractions;
using Chat.Domain.Abstractions.IServices;
using Chat.Infrastructure.Data;
using Chat.Infrastructure.Repositories;
using Chat.Infrastructure.Services;

namespace Chat.Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructureServices(this IServiceCollection services, 
        IConfiguration conf)
    {
        var connectionString = conf.GetConnectionString("DefaultConnection");

        services.AddScoped<IMessageService, MessageService>();
        services.AddScoped<IUnitOfWork, UnitOfWork>();
        services.AddSignalR();
        
        services.AddDbContext<ApplicationDbContext>(options =>
        {
            options.UseNpgsql(connectionString);
        });

        using var scope = services.BuildServiceProvider().CreateScope();
        var dbContext = scope.ServiceProvider.GetRequiredService<ApplicationDbContext>();
        dbContext.Database.Migrate();

        return services;
    }
}