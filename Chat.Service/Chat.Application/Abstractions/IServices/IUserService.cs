using Chat.Application.Common.DTOs;
using Chat.Domain.Enums;

namespace Chat.Application.Abstractions.IServices;

public interface IUserService
{
    void SetAuthToken(string? token);
    Task UpdateUserStatusAsync(Guid userId, UserStatusEnum userStatus);
    Task UpdateLastInteractionAsync(Guid senderId, Guid receiverId, DateTimeOffset lastInteraction);
    Task<IEnumerable<Guid>> GetContactsByUserId(Guid userId);
    Task<UserInfoDto> GetUserInfoByIdAsync(Guid requestorId, Guid userId);
}