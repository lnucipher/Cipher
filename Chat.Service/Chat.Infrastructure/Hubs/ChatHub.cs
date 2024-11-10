using Chat.Domain.Abstractions.IServices;
using Chat.Domain.Enums;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.SignalR;

namespace Chat.Infrastructure.Hubs
{
    [Authorize]
    public sealed class ChatHub(IUserService userService) : Hub
    {
        public static Dictionary<string, Guid> Users { get; } = new();

        public override async Task OnConnectedAsync()
        {
            var userId = GetUserIdClaimValue();
            if (!Users.ContainsValue(userId))
            {
                Users.Add(Context.ConnectionId, userId);
                await userService.UpdateUserStatusAsync(userId, UserStatusEnum.Online);
                var contacts = await GetConnectedContactsForUser(userId);
                await NotifyContacts(contacts, userId, "UserConnected");
            }
            
            await base.OnConnectedAsync();
        }

        public override async Task OnDisconnectedAsync(Exception? exception)
        {
            if (Users.Remove(Context.ConnectionId, out var userId))
            {
                await userService.UpdateUserStatusAsync(userId, UserStatusEnum.Offline);
                var contacts = await GetConnectedContactsForUser(userId);
                await NotifyContacts(contacts, userId, "UserDisconnected");
            }
            
            await base.OnDisconnectedAsync(exception);
        }

        private async Task NotifyContacts(List<Guid> connectedContactIds, Guid userId, string notificationType)
        {
            foreach (var contactId in connectedContactIds)
            {
                var contactConnectionId = GetConnectionIdForUser(contactId);
                if (contactConnectionId != null)
                {
                    await Clients.Client(contactConnectionId).SendAsync(notificationType, userId.ToString().ToUpper());
                }
            }
        }

        private string? GetConnectionIdForUser(Guid userId)
        {
            return Users.FirstOrDefault(x => x.Value == userId).Key;
        }

        private async Task<List<Guid>> GetConnectedContactsForUser(Guid userId)
        {
            var contacts = await userService.GetContactsByUserId(userId);
            return contacts.Where(contact => Users.ContainsValue(contact)).ToList();
        }

        private Guid GetUserIdClaimValue()
        {
            return new Guid(Context.User.Claims.First(c => c.Type == "userId").Value);
        }
    }
}