using Chat.Application.Messages.Create;

namespace Chat.Application.Abstractions;

public interface IPushNotificationService
{
    public Task SendPushNotificationToReceiverAsync(MessageCreatedEvent sentMessage);
}