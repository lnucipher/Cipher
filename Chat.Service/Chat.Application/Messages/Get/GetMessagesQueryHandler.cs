using System.Linq.Expressions;
using Chat.Application.Models.CQRSMessaging;
using Chat.Domain.Abstractions.IServices;

namespace Chat.Application.Messages.Get;

internal sealed class GetMessagesQueryHandler(IUnitOfWork unitOfWork, IEncryptionService encryptionService) : IQueryHandler<GetMessagesQuery, IPagedList<Message>>
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
        
        foreach (var message in messages)
        {
            message.Text = encryptionService.Decrypt(message.Text);
        }
        
        return messages;
    }
}