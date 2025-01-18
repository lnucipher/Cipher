using Chat.Application.Models.CQRSMessaging;
using Chat.Domain.Abstractions.IServices;

namespace Chat.Application.Messages.Create;

internal sealed class CreateMessageCommandHandler(
    IUnitOfWork unitOfWork,
    IMessageService messageService,
    IUserService userService,
    IEncryptionService encryptionService,
    IPushNotificationService pushNotificationService) : ICommandHandler<CreateMessageCommand>
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
            
            await unitOfWork.CommitTransactionAsync(cancellationToken);

            message.Text = decryptedText;
            
            if (request.ConnectionIds.SenderConnectionIds.Any())
            {
                await messageService.SendMessageAsync(message, request.ConnectionIds.SenderConnectionIds);
            }

            if (request.ConnectionIds.ReceiverConnectionIds.Any())
            {
                await messageService.SendMessageAsync(message, request.ConnectionIds.ReceiverConnectionIds);
            }
            
            await pushNotificationService.SendPushNotificationToReceiverAsync(message);
        }
        catch (Exception)
        {
            await unitOfWork.RollbackTransactionAsync(cancellationToken);
            throw;
        }
    }
}
