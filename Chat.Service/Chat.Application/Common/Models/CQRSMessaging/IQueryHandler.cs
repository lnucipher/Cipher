using MediatR;

namespace Chat.Application.Models.CQRSMessaging;

public interface IQueryHandler<in TQuery, TResponse>
    : IRequestHandler<TQuery, TResponse>
    where TQuery : IQuery<TResponse>;