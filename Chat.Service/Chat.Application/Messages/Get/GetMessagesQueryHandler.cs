using System.Linq.Expressions;
using Chat.Application.Models.CQRSMessaging;

namespace Chat.Application.Messages.Get;

internal sealed class GetMessagesQueryHandler(IUnitOfWork unitOfWork) : IQueryHandler<GetMessagesQuery, IPagedList<Message>>
{
    public async Task<IPagedList<Message>> Handle(GetMessagesQuery request, CancellationToken cancellationToken)
    {
        Expression<Func<Message, bool>> filter = x => x.SenderId == request.SenderId && x.ReceiverId == request.ReceiverId;

        var messages = await unitOfWork
            .Messages
            .GetPagedListAsync(
                request.PageNumber,
                request.PageSize,
                x => x.CreatedAt, ascending: false,
                filter);
        
        return messages;
    }
}