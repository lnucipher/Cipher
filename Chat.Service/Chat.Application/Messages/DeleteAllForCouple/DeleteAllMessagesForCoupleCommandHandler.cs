using Chat.Application.Models.CQRSMessaging;

namespace Chat.Application.Messages.DeleteAllForCouple;

public class DeleteAllMessagesForCoupleCommandHandler(IUnitOfWork unitOfWork)
    : ICommandHandler<DeleteAllMessagesForCoupleCommand>
{
    public async Task Handle(DeleteAllMessagesForCoupleCommand request, CancellationToken cancellationToken)
    {
        unitOfWork.Messages.DeleteByFilter(m =>
            m.SenderId == request.PrimaryUserId && m.ReceiverId == request.SecondaryUserId ||
            m.SenderId == request.SecondaryUserId && m.ReceiverId == request.PrimaryUserId);
        
        await unitOfWork.SaveChangesAsync(cancellationToken);
    }
}