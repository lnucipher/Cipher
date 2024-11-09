using Chat.Application.Models.CQRSMessaging;
using Chat.Domain.Abstractions.IServices;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;

namespace Chat.Application.Messages.Create;

internal sealed class CreateMessageCommandHandler(
    IUnitOfWork unitOfWork,
    IMessageService messageService,
    IUserService userService,
    IHttpContextAccessor httpContextAccessor) : ICommandHandler<CreateMessageCommand>
{
    public async Task Handle(CreateMessageCommand request, CancellationToken cancellationToken)
    {
        var userClaims = httpContextAccessor.HttpContext?.User;
        try
        {
            await unitOfWork.BeginTransactionAsync(cancellationToken);

            var message = new Message()
            {
                Text = request.Text,
                SenderId = request.SenderId,
                ReceiverId = request.ReceiverId
            };

            unitOfWork.Messages.Add(message);

            await userService.UpdateLastInteractionAsync(request.SenderId, request.ReceiverId, message.CreatedAt);

            if (request.ConnectionIds.SenderConnectionId is not null)
            {
                await messageService.SendMessageAsync(message, request.ConnectionIds.SenderConnectionId);
            }

            if (request.ConnectionIds.ReceiverConnectionId is not null)
            {
                await messageService.SendMessageAsync(message, request.ConnectionIds.ReceiverConnectionId);
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