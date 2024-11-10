using Chat.Domain.Entities;

namespace Chat.Domain.Abstractions;

public interface IUnitOfWork
{
    IGenericRepository<Message> Messages { get; }
    
    Task BeginTransactionAsync(CancellationToken cancellationToken = default);
    Task CommitTransactionAsync(CancellationToken cancellationToken = default);
    Task RollbackTransactionAsync(CancellationToken cancellationToken = default);
    
    Task SaveChangesAsync(CancellationToken cancellationToken = default);
}