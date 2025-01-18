using Chat.Application.Abstractions.EventBus;
using MassTransit;

namespace Chat.Infrastructure.MessageBroker;

public sealed class EventBus(IPublishEndpoint publishEndpoint) : IEventBus
{
    public Task PublishAsync<T>(T message, CancellationToken cancellationToken = default) 
        where T : class => publishEndpoint.Publish(message, cancellationToken);
}