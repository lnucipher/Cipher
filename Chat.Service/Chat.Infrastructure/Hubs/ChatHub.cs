using Chat.Application.Abstractions;
using Chat.Application.Abstractions.IServices;
using Chat.Domain.Abstractions.IServices;
using Chat.Domain.Enums;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.SignalR;

namespace Chat.Infrastructure.Hubs
{
    [Authorize]
    public sealed class ChatHub(
        IUserService userService, 
        IConnectionManager connectionManager) : Hub
    {
        public override async Task OnConnectedAsync()
        {
            var userId = GetUserIdClaimValue();
            if (!connectionManager.IsUserConnected(userId))
            {
                connectionManager.AddConnection(Context.ConnectionId, userId);
                await userService.UpdateUserStatusAsync(userId, UserStatusEnum.Online);
            }
            else
            {
                connectionManager.AddConnection(Context.ConnectionId, userId);
            }
            
            var contacts = await GetConnectedContactsForUser(userId);
            await NotifyContacts(contacts, userId, "UserConnected");
            
            await base.OnConnectedAsync();
        }

        public override async Task OnDisconnectedAsync(Exception? exception)
        {
            if (connectionManager.RemoveConnection(Context.ConnectionId, out var userId))
            {
                if (!connectionManager.IsUserConnected(userId))
                {
                    await userService.UpdateUserStatusAsync(userId, UserStatusEnum.Offline);

                    var contacts = await GetConnectedContactsForUser(userId);
                    await NotifyContacts(contacts, userId, "UserDisconnected");
                }
            }

            await base.OnDisconnectedAsync(exception);
        }
        
        private async Task NotifyContacts(List<Guid> connectedContactIds, Guid userId, string notificationType)
        {
            foreach (var contactId in connectedContactIds)
            {
                var connectionIds = connectionManager.GetConnectionIdsByUserId(contactId);
                foreach (var connectionId in connectionIds)
                {
                    await Clients.Client(connectionId).SendAsync(notificationType, userId.ToString().ToUpper());
                }
            }
        }

        private async Task<List<Guid>> GetConnectedContactsForUser(Guid userId)
        {
            var contacts = await userService.GetContactsByUserId(userId);
            return contacts.Where(connectionManager.IsUserConnected).ToList();
        }

        private Guid GetUserIdClaimValue()
        {
            return new Guid(Context.User.Claims.First(c => c.Type == "userId").Value);
        }
    }
}