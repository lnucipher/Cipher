using Chat.Application.Models.CQRSMessaging;

namespace Chat.Application.Messages.DeleteAllForCouple;

public sealed record DeleteAllMessagesForCoupleCommand(
    Guid PrimaryUserId,
    Guid SecondaryUserId) : ICommand;

public sealed record DeleteAllMessagesForCoupleRequest(
    Guid PrimaryUserId,
    Guid SecondaryUserId);