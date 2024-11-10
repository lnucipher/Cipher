using MediatR;

namespace Chat.Application.Models.CQRSMessaging;

public interface ICommand : IRequest;
public interface ICommand<TResponse> : IRequest<TResponse>;