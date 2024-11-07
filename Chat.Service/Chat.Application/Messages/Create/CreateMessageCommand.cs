using Chat.Application.DTOs;

namespace Chat.Application.Messages.Create;

public sealed record CreateMessageCommand(
    Guid SenderId,
    Guid ReceiverId,
    string Text,
    ConnectionIdsDto ConnectionIds) : ICommand;

public sealed record CreateMessageRequest(
    Guid SenderId, 
    Guid ReceiverId, 
    string Text);