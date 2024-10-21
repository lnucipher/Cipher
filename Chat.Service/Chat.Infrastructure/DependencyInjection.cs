using System.Reflection;
using Chat.Domain.Abstractions;
using Chat.Infrastructure.Data;
using Chat.Infrastructure.Repositories;

namespace Chat.Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructureServices(this IServiceCollection services, 
        IConfiguration conf)
    {
        var connectionString = conf.GetConnectionString("DefaultConnection");

        services.AddScoped<IUnitOfWork, UnitOfWork>();
        
        services.AddDbContext<ApplicationDbContext>(options =>
        {
            options.UseNpgsql(connectionString);
        });
        
        return services;
    }
}