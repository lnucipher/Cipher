using Chat.Application.Abstractions.IServices;
using Chat.Application.Common.DTOs;
using Chat.Application.Messages.Create;
using Chat.Infrastructure.Hubs;
using Microsoft.AspNetCore.SignalR;

namespace Chat.Infrastructure.Services;

public class WebsocketNotificationService(IHubContext<ChatHub> hubContext) : IWebsocketNotificationService
{
    public async Task SendNotificationAsync(MessageCreatedEvent message, IEnumerable<string> connectionIds)
    {
        var sendTasks = connectionIds.Select(connectionId =>
            hubContext.Clients.Client(connectionId).SendAsync("ReceiveMessage",
                new ReceiveMessageEventDto(
                    message.Id.ToString().ToUpper(),
                    message.SenderId.ToString().ToUpper(),
                    message.ReceiverId.ToString().ToUpper(),
                    message.Text,
                    message.CreatedAt)));

        await Task.WhenAll(sendTasks);
    }
}