using Chat.Application.Common.DTOs;
using Chat.Application.Models.CQRSMessaging;

namespace Chat.Application.Messages.Create;

public sealed record CreateMessageCommand(
    Guid SenderId,
    Guid ReceiverId,
    string Text) : ICommand;

public sealed record CreateMessageRequest(
    Guid ReceiverId, 
    string Text);