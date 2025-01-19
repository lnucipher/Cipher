using Chat.Application.Abstractions.EventBus;
using Chat.Application.Models.CQRSMessaging;
using Chat.Domain.Abstractions.IProviders;
using Chat.Domain.Abstractions.IServices;

namespace Chat.Application.Messages.Create;

internal sealed class CreateMessageCommandHandler(
    IUnitOfWork unitOfWork,
    IEncryptionService encryptionService,
    IEventBus eventBus,
    IUserContextProvider userContextProvider) : ICommandHandler<CreateMessageCommand>
{
    public async Task Handle(CreateMessageCommand request, CancellationToken cancellationToken)
    {
        var decryptedText = request.Text;
        var encryptedText = encryptionService.Encrypt(request.Text);

        var message = new Message
        {
            Text = encryptedText,
            SenderId = request.SenderId,
            ReceiverId = request.ReceiverId
        };

        unitOfWork.Messages.Add(message);
        
        await unitOfWork.SaveChangesAsync(cancellationToken);

        await eventBus.PublishAsync(new MessageCreatedEvent()
        {
            Id = message.Id,
            SenderId = message.SenderId,
            ReceiverId = message.ReceiverId,
            Text = decryptedText,
            CreatedAt = message.CreatedAt,
            UserAuthToken = userContextProvider.GetTokenFromRequest()
        }, cancellationToken);
    }
}