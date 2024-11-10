using System.Text.Json;
using Chat.Application.Common.DTOs;
using Chat.Application.Converters;
using Chat.Domain.Abstractions.IServices;
using Chat.Infrastructure.Hubs;
using Microsoft.AspNetCore.SignalR;
using JsonSerializer = System.Text.Json.JsonSerializer;

namespace Chat.Infrastructure.Services;

public class MessageService(IHubContext<ChatHub> hubContext) : IMessageService
{
    public async Task SendMessageAsync(Message message, string connectionId)
    {
        await hubContext.Clients.Client(connectionId).SendAsync("ReceiveMessage",
            new ReceiveMessageEventDto(
                message.SenderId.ToString().ToUpper(), 
                message.ReceiverId.ToString().ToUpper(),
                message.Text, 
                message.CreatedAt));
    }
}