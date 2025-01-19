using Chat.Application.Abstractions;
using Chat.Application.Abstractions.IServices;
using Chat.Application.Messages.Create;
using FirebaseAdmin.Messaging;
using Microsoft.Extensions.Logging;
using FBMessage = FirebaseAdmin.Messaging.Message;

namespace Chat.Infrastructure.Services;

public class PushNotificationService(
    ILogger<PushNotificationService> logger,
    IUserService userService) : IPushNotificationService
{
    public async Task SendPushNotificationToReceiverAsync(MessageCreatedEvent sentMessage)
    {
        var userInfo = await userService.GetUserInfoByIdAsync(sentMessage.SenderId, sentMessage.SenderId);
    
        var message = new FBMessage
        {
            Notification = new Notification
            {
                Title = userInfo.Name,
                Body = sentMessage.Text
            },
            Data = new Dictionary<string, string>
            {
                { "senderId", userInfo.Id.ToString() },
                { "senderAvatarUrl", userInfo.AvatarUrl },
                { "senderDisplayName", userInfo.Name },
                { "messageText", sentMessage.Text }
            },
            Topic = "user_" + sentMessage.ReceiverId
        };
    
        var messaging = FirebaseMessaging.DefaultInstance;
        var result = await messaging.SendAsync(message);

        if (!string.IsNullOrEmpty(result))
        {
            logger.LogInformation("Successfully sent notification to receiver: {result}", result);
        }
        else
        {
            logger.LogError("Failed to send notification to receiver.");
        }
    }
}