using System.Linq.Expressions;

namespace Chat.Domain.Abstractions;

public interface IGenericRepository<T> where T : class
{
    Task<IPagedList<T>> GetPagedListAsync(
        int pageNumber,
        int pageSize,
        Expression<Func<T, object>>? orderBy = null,
        bool ascending = true,
        Expression<Func<T, bool>>? filter = null);
    void Add(T entity);
    void DeleteByFilter(Expression<Func<T, bool>> filter);
    Task<bool> AnyAsync(Expression<Func<T, bool>> filter);
}