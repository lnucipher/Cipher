using System.Security.Claims;
using Carter;
using Chat.Application.Common.DTOs;
using Chat.Application.Messages.Create;
using Chat.Application.Messages.Get;
using Chat.Domain.Abstractions;
using Chat.Domain.Abstractions.IServices;
using Chat.Domain.Entities;
using Chat.Infrastructure.Hubs;
using MediatR;
using Microsoft.AspNetCore.SignalR;

namespace Chat.Api.Endpoints;

public class ChatEndpoints(IHubContext<ChatHub> hubContext) : ICarterModule
{
    public void AddRoutes(IEndpointRouteBuilder app)
    {
        var group = app.MapGroup("api/messages");

        group.MapPost("", CreateMessage)
            .RequireAuthorization();
        group.MapGet("", GetMessages)
            .RequireAuthorization();;
    }

    private async Task<IPagedList<Message>> GetMessages(ISender sender, [AsParameters] GetMessagesRequest request)
    {
        var query = new GetMessagesQuery(
            request.SenderId,
            request.ReceiverId,
            request.Page,
            request.PageSize);

        var messages = await sender.Send(query);

        return messages;
    }

    private async Task<IResult> CreateMessage(ISender sender, CreateMessageRequest request, IHubContext<ChatHub> hubContext, ClaimsPrincipal user)
    {
        var senderId = Guid.Parse(user.FindFirst("userId")?.Value!);
        var receiverConnectionId = ChatHub.Users.Where(p => p.Value == request.ReceiverId)
            .Select(kp => kp.Key)
            .ToList();
        var senderConnectionId = ChatHub.Users.Where(p => p.Value == senderId)
            .Select(kp => kp.Key)
            .ToList();
        
        var command = new CreateMessageCommand(
            senderId,
            request.ReceiverId,
            request.Text,
            new ConnectionIdsDto(senderConnectionId, receiverConnectionId));
        
        await sender.Send(command);

        return Results.Created();
    }
}