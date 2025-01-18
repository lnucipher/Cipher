using Chat.Domain.Entities;

namespace Chat.Domain.Abstractions.IServices;

public interface IPushNotificationService
{
    public Task SendPushNotificationToReceiverAsync(Message sentMessage);
}