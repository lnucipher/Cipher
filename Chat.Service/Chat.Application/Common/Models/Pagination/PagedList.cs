using System.Collections;
using Microsoft.EntityFrameworkCore;

namespace Chat.Application.Common.Models.Pagination;

public class PagedList<T>(List<T> items, int count, int pageNumber, int pageSize)
    : IPagedList<T>
{
    public int PageNumber { get; } = pageNumber;
    public int PageSize { get; } = pageSize;
    public int TotalCount { get; } = count;
    public int TotalPages { get; } = (int)Math.Ceiling(count / (double)pageSize);
    public bool HasPreviousPage => PageNumber > 1;
    public bool HasNextPage => PageNumber < TotalPages;
    public List<T> Items { get; } = items;

    public static async Task<PagedList<T>> CreateAsync(IQueryable<T> source, int pageIndex, int pageSize)
    {
        var count = await source.CountAsync();
        var items = await source.Skip((pageIndex - 1) * pageSize).Take(pageSize).ToListAsync();
        
        return new PagedList<T>(items, count, pageIndex, pageSize);
    }
}
