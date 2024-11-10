namespace Chat.Application.Common.DTOs;

public record ReceiveMessageEventDto(string Id, string SenderId, string ReceiverId, string Text, DateTimeOffset CreatedAt);