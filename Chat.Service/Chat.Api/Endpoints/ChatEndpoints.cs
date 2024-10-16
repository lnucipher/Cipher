﻿using Carter;
using Chat.Application.Messages.Create;
using Chat.Application.Messages.Get;
using Chat.Domain.Abstractions;
using Chat.Domain.Entities;
using Chat.Domain.Shared;
using MediatR;

namespace Chat.Api.Endpoints;

public class ChatEndpoints : ICarterModule
{
    public void AddRoutes(IEndpointRouteBuilder app)
    {
        var group = app.MapGroup("api/messages");

        group.MapPost("", CreateMessage);
        group.MapGet("", GetMessages);
    }

    private async Task<Result<IPagedList<Message>>> GetMessages(ISender sender, [AsParameters] GetMessagesRequest request)
    {
        var query = new GetMessagesQuery(
            request.SenderId,
            request.ReceiverId,
            request.Page,
            request.PageSize);

        var messages = await sender.Send(query);

        return messages;
    }

    private static async Task<Result> CreateMessage(ISender sender, CreateMessageRequest request)
    {
        var command = new CreateMessageCommand(
            request.SenderId,
            request.ReceiverId,
            request.Text);

        var result = await sender.Send(command);

        return result;
    }
}