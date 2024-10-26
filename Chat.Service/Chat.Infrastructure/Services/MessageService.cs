using Chat.Domain.Abstractions.IServices;
using Chat.Infrastructure.Hubs;
using Microsoft.AspNetCore.SignalR;

namespace Chat.Infrastructure.Services;

public class MessageService(IHubContext<ChatHub> hubContext) : IMessageService
{
    public async Task SendMessageAsync(Message message, string connectionId)
    {
        await hubContext.Clients.Client(connectionId).SendAsync("ReceiveMessage", message);
    }
}