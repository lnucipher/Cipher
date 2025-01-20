namespace Chat.Application.Common.DTOs;

public record UserInfoDto(
    Guid Id, 
    string AvatarUrl, 
    string Bio,
    DateTime LastSeen,
    string Name,
    string Status,
    string Username);