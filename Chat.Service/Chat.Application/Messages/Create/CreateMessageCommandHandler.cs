using Chat.Application.Models.CQRSMessaging;
using Chat.Domain.Abstractions.IServices;

namespace Chat.Application.Messages.Create;

internal sealed class CreateMessageCommandHandler(
    IUnitOfWork unitOfWork,
    IMessageService messageService,
    IUserService userService,
    IEncryptionService encryptionService) : ICommandHandler<CreateMessageCommand>
{
    public async Task Handle(CreateMessageCommand request, CancellationToken cancellationToken)
    {
        try
        {
            await unitOfWork.BeginTransactionAsync(cancellationToken);

            var decryptedText = request.Text;

            var encryptedText = encryptionService.Encrypt(request.Text);

            var message = new Message
            {
                Text = encryptedText,
                SenderId = request.SenderId,
                ReceiverId = request.ReceiverId
            };

            unitOfWork.Messages.Add(message);

            await userService.UpdateLastInteractionAsync(request.SenderId, request.ReceiverId, message.CreatedAt);
            
            if (request.ConnectionIds.SenderConnectionId is not null)
            {
                await messageService.SendMessageAsync(new Message
                {
                    Text = decryptedText,
                    SenderId = message.SenderId,
                    ReceiverId = message.ReceiverId,
                    CreatedAt = message.CreatedAt
                }, request.ConnectionIds.SenderConnectionId);
            }

            if (request.ConnectionIds.ReceiverConnectionId is not null)
            {
                await messageService.SendMessageAsync(new Message
                {
                    Text = decryptedText,
                    SenderId = message.SenderId,
                    ReceiverId = message.ReceiverId,
                    CreatedAt = message.CreatedAt
                }, request.ConnectionIds.ReceiverConnectionId);
            }

            await unitOfWork.CommitTransactionAsync(cancellationToken);
        }
        catch (Exception)
        {
            await unitOfWork.RollbackTransactionAsync(cancellationToken);
            throw;
        }
    }
}
