using System.Linq.Expressions;
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
    
    public async Task<IPagedList<T>> GetPagedListAsync(
        int pageNumber, 
        int pageSize, 
        Expression<Func<T, object>>? orderBy = null, 
        bool ascending = true)
    {
        var query = _dbSet.AsQueryable();
        
        if (orderBy != null)
        {
            query = ascending ? query.OrderBy(orderBy) : query.OrderByDescending(orderBy);
        }
        
        return await PagedList<T>.CreateAsync(query, pageNumber, pageSize);
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