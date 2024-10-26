using Microsoft.AspNetCore.SignalR;

namespace Chat.Infrastructure.Hubs;

public sealed class ChatHub : Hub
{
    public static Dictionary<string, Guid> Users { get; } = new();

    public async Task Connect(Guid userId)
    {
        Users.Add(Context.ConnectionId, userId);
        
        // Send request to update the user status to online
        
        await Clients.All.SendAsync("UserConnected", userId);
    }

    public override Task OnDisconnectedAsync(Exception? exception)
    {
        Users.TryGetValue(Context.ConnectionId, out _);
        Users.Remove(Context.ConnectionId);
        
        // Send request to update the user status to offline
        
        return base.OnDisconnectedAsync(exception);
    }
}