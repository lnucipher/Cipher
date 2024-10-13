using Chat.Application.Models.Pagination;
using Chat.Domain.Abstractions;

namespace Chat.Infrastructure.Repositories;

public class GenericRepository<T> : IGenericRepository<T> where T : class
{
    private readonly DbSet<T> _dbSet;

    public GenericRepository(DbSet<T> dbSet)
    {
        _dbSet = dbSet;
    }
    
    public async Task<IPagedList<T>> GetPagedListAsync(int pageNumber, int pageSize)
    {
        return await PagedList<T>.CreateAsync(_dbSet.AsQueryable(), pageNumber, pageSize);
    }

    public void Add(T entity)
    {
        _dbSet.Add(entity);
    }

    public void Remove(T entity)
    {
        _dbSet.Remove(entity);
    }
}