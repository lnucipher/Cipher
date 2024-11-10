using System.Text.Json;
using Chat.Application.Converters;
using Chat.Domain.Abstractions.IServices;
using Chat.Infrastructure.Hubs;
using Microsoft.AspNetCore.SignalR;
using JsonSerializer = System.Text.Json.JsonSerializer;

namespace Chat.Infrastructure.Services;

public class MessageService(IHubContext<ChatHub> hubContext) : IMessageService
{
    private readonly JsonSerializerOptions _serializerSettings = new()
    {
        PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
        Converters = { new UppercaseGuidConverter(), new DateTimeOffsetConverter() }
    };
    
    public async Task SendMessageAsync(Message message, string connectionId)
    {
        var serializedMessage = JsonSerializer.Serialize(message, _serializerSettings);
        
        await hubContext.Clients.Client(connectionId).SendAsync("ReceiveMessage", serializedMessage);
    }
}