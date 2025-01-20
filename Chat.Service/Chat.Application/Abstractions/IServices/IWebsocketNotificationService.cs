using Chat.Application.Messages.Create;

namespace Chat.Application.Abstractions.IServices;

public interface IWebsocketNotificationService
{
    Task SendNotificationAsync(MessageCreatedEvent message, IEnumerable<string> connectionIds);
}