using MediatR;

namespace Chat.Application.Messaging;

public interface ICommand : IRequest;
public interface ICommand<TResponse> : IRequest<TResponse>;