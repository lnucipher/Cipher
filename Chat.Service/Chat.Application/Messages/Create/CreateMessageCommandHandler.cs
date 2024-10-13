namespace Chat.Application.Messages.Create;

internal sealed class CreateMessageCommandHandler(IUnitOfWork unitOfWork) : ICommandHandler<CreateMessageCommand>
{
    public async Task<Result> Handle(CreateMessageCommand request, CancellationToken cancellationToken)
    {
        var message = new Message()
        {
            Text = request.Text,
            SenderId = request.SenderId,
            ReceiverId = request.ReceiverId
        };
        
        unitOfWork.Messages.Add(message);
        await unitOfWork.SaveChangesAsync(cancellationToken);
        
        return Result.Success();
    }
}