namespace Chat.Domain.Abstractions;

public interface IPagedList<T> : IEnumerable<T>
{
    int PageNumber { get; }
    int PageSize { get; }
    int TotalCount { get; }
    int TotalPages { get; }
    bool HasPreviousPage { get; }
    bool HasNextPage { get; }
    List<T> Items { get; }
}