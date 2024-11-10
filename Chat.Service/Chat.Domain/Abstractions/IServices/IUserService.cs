using Chat.Domain.Enums;

namespace Chat.Domain.Abstractions.IServices;

public interface IUserService
{
    Task UpdateUserStatusAsync(Guid userId, UserStatusEnum userStatus);
    Task UpdateLastInteractionAsync(Guid senderId, Guid receiverId, DateTimeOffset lastInteraction);
    Task<IEnumerable<Guid>> GetContactsByUserId(Guid userId);
}