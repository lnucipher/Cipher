using System.Runtime.Serialization;

namespace Chat.Domain.Enums;

public enum UserStatusEnum
{
    [EnumMember(Value = "online")]
    Online,
    [EnumMember(Value = "offline")]
    Offline
}