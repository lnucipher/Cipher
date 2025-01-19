using Chat.Application.Abstractions.EventBus;
using Chat.Domain.Abstractions.Base;
using MassTransit;

namespace Chat.Infrastructure.MessageBroker
{
    public sealed class EventBus(IPublishEndpoint publishEndpoint)
        : IEventBus
    {
        public async Task PublishAsync<TEvent>(TEvent message, CancellationToken cancellationToken = default)
            where TEvent : EventBase => await publishEndpoint.Publish(message, cancellationToken);
    }
}