using Chat.Application.Models.CQRSMessaging;

namespace Chat.Application.Messages.Get;

public sealed record GetMessagesQuery(
    Guid SenderId,
    Guid ReceiverId,
    int PageNumber,
    int PageSize) : IQuery<IPagedList<Message>>;

public sealed record GetMessagesRequest(
    Guid SenderId, 
    Guid ReceiverId,
    int Page,
    int PageSize);