using Chat.Domain.Entities;

namespace Chat.Domain.Abstractions.IServices;

public interface IMessageService
{
    Task SendMessageAsync(Message message, string connectionId);
}