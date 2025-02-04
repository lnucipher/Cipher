﻿using System.Linq.Expressions;
using Chat.Application.Common.Models.Pagination;
using Chat.Domain.Abstractions;

namespace Chat.Infrastructure.Repositories;

public class GenericRepository<T>(DbSet<T> dbSet) : IGenericRepository<T>
    where T : class
{
    public async Task<IPagedList<T>> GetPagedListAsync(
        int pageNumber, 
        int pageSize, 
        Expression<Func<T, object>>? orderBy = null, 
        bool ascending = true,
        Expression<Func<T, bool>>? filter = null) 
    {
        var query = dbSet.AsQueryable();
        
        if (filter != null)
        {
            query = query.Where(filter);
        }
    
        if (orderBy != null)
        {
            query = ascending ? query.OrderBy(orderBy) : query.OrderByDescending(orderBy);
        }
    
        return await PagedList<T>.CreateAsync(query, pageNumber, pageSize);
    }

    public void Add(T entity)
    {
        dbSet.Add(entity);
    }

    public void DeleteByFilter(Expression<Func<T, bool>> filter)
    {
        var entities = dbSet.Where(filter);
        dbSet.RemoveRange(entities);
    }

    public async Task<bool> AnyAsync(Expression<Func<T, bool>> filter)
    {
        return await dbSet.AnyAsync(filter);
    }

    public void Remove(T entity)
    {
        dbSet.Remove(entity);
    }
}