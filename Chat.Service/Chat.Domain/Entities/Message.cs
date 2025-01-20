using System.ComponentModel.DataAnnotations;
using Chat.Domain.Primitives;

namespace Chat.Domain.Entities;

public class Message : Entity
{
    [Required]
    public Guid SenderId { get; set; }
    
    [Required]
    public Guid ReceiverId { get; set; }
    
    [MinLength(1, ErrorMessage = "Message text cannot be empty.")]
    public string Text { get; set; } = string.Empty;

    [Required]
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
}