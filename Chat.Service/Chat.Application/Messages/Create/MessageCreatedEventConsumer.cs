using Chat.Application.Abstractions;
using Chat.Application.Abstractions.IServices;
using Chat.Domain.Abstractions.IServices;
using MassTransit;
using Microsoft.Extensions.Logging;

namespace Chat.Application.Messages.Create;

public sealed class MessageCreatedEventConsumer(
    ILogger<MessageCreatedEventConsumer> logger,
    IUserService userService,
    IPushNotificationService pushNotificationService,
    IConnectionManager connectionManager,
    IWebsocketNotificationService websocketNotificationService) : IConsumer<MessageCreatedEvent>
{
    public async Task Consume(ConsumeContext<MessageCreatedEvent> context)
    {
        logger.LogInformation("Message created: {MessageId}", context.Message.Id);
        
        userService.SetAuthToken(context.Message.UserAuthToken);
        await userService.UpdateLastInteractionAsync(context.Message.SenderId, context.Message.ReceiverId, DateTime.UtcNow);
        
        await pushNotificationService.SendPushNotificationToReceiverAsync(context.Message);
        
        var receiverConnectionIds = connectionManager.GetConnectionIdsByUserId(context.Message.ReceiverId).ToList();
        var senderConnectionIds = connectionManager.GetConnectionIdsByUserId(context.Message.SenderId).ToList();
        
        if (senderConnectionIds.Count != 0)
        {
            await websocketNotificationService.SendNotificationAsync(context.Message, senderConnectionIds);
        }

        if (receiverConnectionIds.Count != 0)
        {
            await websocketNotificationService.SendNotificationAsync(context.Message, receiverConnectionIds);
        }
    }
}