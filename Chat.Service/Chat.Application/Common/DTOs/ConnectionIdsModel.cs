namespace Chat.Application.Common.DTOs;

public record ConnectionIdsDto(IEnumerable<string> SenderConnectionIds, IEnumerable<string> ReceiverConnectionIds);