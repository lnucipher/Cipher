using Chat.Domain.Abstractions.Base;

namespace Chat.Application.Messages.Create;

public record MessageCreatedEvent : EventBase
{
    public Guid Id { get; init; }
    public Guid SenderId { get; init; }
    public Guid ReceiverId { get; init; }
    public string Text { get; init; } = string.Empty;
    public DateTime CreatedAt { get; init; }
}