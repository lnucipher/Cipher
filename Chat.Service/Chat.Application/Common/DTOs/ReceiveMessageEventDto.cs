namespace Chat.Application.Common.DTOs;

public record ReceiveMessageEventDto(string SenderId, string ReceiverId, string Text, DateTimeOffset CreatedAt);