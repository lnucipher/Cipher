using Chat.Application.Common.DTOs;
using Chat.Domain.Abstractions.IServices;
using Chat.Infrastructure.Hubs;
using Microsoft.AspNetCore.SignalR;

namespace Chat.Infrastructure.Services;

public class MessageService(IHubContext<ChatHub> hubContext) : IMessageService
{
    public async Task SendMessageAsync(Message message, IEnumerable<string> connectionIds)
    {
        foreach (var connectionId in connectionIds)
        {
            await hubContext.Clients.Client(connectionId).SendAsync("ReceiveMessage",
                new ReceiveMessageEventDto(
                    message.Id.ToString().ToUpper(),
                    message.SenderId.ToString().ToUpper(), 
                    message.ReceiverId.ToString().ToUpper(),
                    message.Text, 
                    message.CreatedAt));
        }
    }
}