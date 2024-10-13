using MediatR;

namespace Chat.Application.Messaging;

public interface IQuery<TResponse> : IRequest<Result<TResponse>>;