using Chat.Domain.Enums;

public record UserStatusDto(
    string Id, 
    UserStatusEnum Status, 
    string? Timestamp = null
)
{
    public string? Timestamp { get; init; } = Timestamp ?? (Status == UserStatusEnum.Offline ? DateTimeOffset.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss.fffZ") : null);
}