using MediatR;

namespace Chat.Application.Models.CQRSMessaging;

public interface IQuery<TResponse> : IRequest<TResponse>;