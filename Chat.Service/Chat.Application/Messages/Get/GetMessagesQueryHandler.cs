namespace Chat.Application.Messages.Get;

internal sealed class GetMessagesQueryHandler(IUnitOfWork unitOfWork) : IQueryHandler<GetMessagesQuery, IPagedList<Message>>
{
    public async Task<Result<IPagedList<Message>>> Handle(GetMessagesQuery request, CancellationToken cancellationToken)
    {
        var messages = await unitOfWork
            .Messages
            .GetPagedListAsync(
                request.PageNumber,
                request.PageSize);
        
        return Result.Success(messages);
    }
}