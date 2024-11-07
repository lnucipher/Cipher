using Chat.Domain.Abstractions.IServices;

namespace Chat.Application.Messages.Create;

internal sealed class CreateMessageCommandHandler(IUnitOfWork unitOfWork, IMessageService messageService) : ICommandHandler<CreateMessageCommand>
{
    public async Task Handle(CreateMessageCommand request, CancellationToken cancellationToken)
    {
        var message = new Message()
        {
            Text = request.Text,
            SenderId = request.SenderId,
            ReceiverId = request.ReceiverId
        };
        
        unitOfWork.Messages.Add(message);
        await unitOfWork.SaveChangesAsync(cancellationToken);

        if (request.ConnectionIds.SenderConnectionId is not null)
        {
            await messageService.SendMessageAsync(message, request.ConnectionIds.SenderConnectionId);
        }
        
        if (request.ConnectionIds.ReceiverConnectionId is not null)
        {
            await messageService.SendMessageAsync(message, request.ConnectionIds.ReceiverConnectionId);
        }
    }
}