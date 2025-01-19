using Chat.Domain.Abstractions.Base;

namespace Chat.Application.Abstractions.EventBus;

public interface IEventBus
{
    Task PublishAsync<TEvent>(TEvent message, CancellationToken cancellationToken = default) where TEvent : EventBase;
}