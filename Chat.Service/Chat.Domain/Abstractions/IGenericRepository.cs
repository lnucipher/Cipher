namespace Chat.Domain.Abstractions;

public interface IGenericRepository<T> where T : class
{
    Task<IPagedList<T>> GetPagedListAsync(int pageNumber, int pageSize);
    void Add(T entity);
}