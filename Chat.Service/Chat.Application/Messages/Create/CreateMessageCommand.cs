namespace Chat.Application.Messages.Create;

public sealed record CreateMessageCommand(
    Guid SenderId,
    Guid ReceiverId,
    string Text,
    string? ConnectionId) : ICommand;

public sealed record CreateMessageRequest(
    Guid SenderId, 
    Guid ReceiverId, 
    string Text);