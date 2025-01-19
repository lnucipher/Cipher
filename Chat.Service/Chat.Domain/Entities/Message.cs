using Chat.Domain.Primitives;

namespace Chat.Domain.Entities;

public class Message : Entity
{
    public Guid SenderId { get; set; }
    public Guid ReceiverId { get; set; }
    public string Text { get; set; } = string.Empty;
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
}