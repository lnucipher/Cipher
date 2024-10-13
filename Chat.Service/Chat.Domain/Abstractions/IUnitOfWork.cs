using Chat.Domain.Entities;

namespace Chat.Domain.Abstractions;

public interface IUnitOfWork
{
    IGenericRepository<Message> Messages { get; }
    
    Task SaveChangesAsync(CancellationToken cancellationToken = default);
}