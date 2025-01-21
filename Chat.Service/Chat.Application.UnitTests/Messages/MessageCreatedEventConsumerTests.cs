using Chat.Application.Messages.Create;
using Chat.Application.Abstractions.IServices;
using MassTransit;
using Microsoft.Extensions.Logging;
using Chat.Application.Abstractions;
using Moq;


namespace Chat.Application.UnitTests.Messages
{
    public class MessageCreatedEventConsumerTests
    {
        private readonly Mock<IUserService> _userServiceMock;
        private readonly MessageCreatedEventConsumer _consumer;
        private readonly Mock<IPushNotificationService> _pushNotificationServiceMock;
        private readonly Mock<ILogger<MessageCreatedEventConsumer>> _loggerMock;
        private readonly Mock<IWebsocketNotificationService> _websocketNotificationServiceMock;
        private readonly Mock<IConnectionManager> _connectionManagerMock;

        public MessageCreatedEventConsumerTests()
        {
            _loggerMock = new Mock<ILogger<MessageCreatedEventConsumer>>();
            _userServiceMock = new Mock<IUserService>();
            _pushNotificationServiceMock = new Mock<IPushNotificationService>();
            _websocketNotificationServiceMock = new Mock<IWebsocketNotificationService>();
            _connectionManagerMock = new Mock<IConnectionManager>();

            _consumer = new MessageCreatedEventConsumer(
                _loggerMock.Object,
                _userServiceMock.Object,
                _pushNotificationServiceMock.Object,
                _connectionManagerMock.Object,
                _websocketNotificationServiceMock.Object
            );
        }

        private static Mock<ConsumeContext<MessageCreatedEvent>> CreateMessageContext(MessageCreatedEvent message)
        {
            var contextMock = new Mock<ConsumeContext<MessageCreatedEvent>>();
            contextMock.Setup(x => x.Message).Returns(message);
            return contextMock;
        }

        [Fact]
        public async Task UpdateLastInteractionAsync_ShouldBeCalled_WithCorrectParameters()
        {
            // Arrange
            var message = new MessageCreatedEvent
            {
                Id = Guid.NewGuid(),
                SenderId = Guid.NewGuid(),
                ReceiverId = Guid.NewGuid(),
                UserAuthToken = "UserToken",
                Text = "Test message",
                CreatedAt = DateTime.UtcNow
            };

            var contextMock = CreateMessageContext(message);

            // Act
            await _consumer.Consume(contextMock.Object);

            // Assert
            _userServiceMock.Verify(
                x => x.UpdateLastInteractionAsync(message.SenderId, message.ReceiverId, It.IsAny<DateTime>()),
                Times.Once);
        }

        [Fact]
        public async Task Consume_ShouldCallSendPushNotificationToReceiverAsync_WhenMessageIsValid()
        {
            // Arrange
            var message = new MessageCreatedEvent
            {
                Id = Guid.NewGuid(),
                SenderId = Guid.NewGuid(),
                ReceiverId = Guid.NewGuid(),
                Text = "Hello!",
                UserAuthToken = "valid_token"
            };

            var contextMock = CreateMessageContext(message);

            // Act
            await _consumer.Consume(contextMock.Object);

            // Assert
            _pushNotificationServiceMock.Verify(x => x.SendPushNotificationToReceiverAsync(message), Times.Once);
        }

        [Fact]
        public async Task Consume_ShouldReturn_WhenUserAuthTokenIsNull()
        {
            // Arrange
            var message = new MessageCreatedEvent
            {
                UserAuthToken = null, // Simulating null UserAuthToken
                Id = Guid.NewGuid(),
                SenderId = Guid.NewGuid(),
                ReceiverId = Guid.NewGuid(),
                Text = "Test Message"
            };

            var contextMock = CreateMessageContext(message);

            // Act
            await _consumer.Consume(contextMock.Object);

            // Assert
            _loggerMock.Verify(
                l => l.Log(
                    LogLevel.Error,
                    It.IsAny<EventId>(),
                    It.IsAny<It.IsAnyType>(),
                    It.IsAny<Exception>(),
                    It.IsAny<Func<It.IsAnyType, Exception, string>>() ),
                Times.Once);
        }

        [Fact]
        public async Task UpdateLastInteractionAsync_ShouldThrowException_WhenNoMessage()
        {
            // Arrange
            var contextMock = new Mock<ConsumeContext<MessageCreatedEvent>>();
            contextMock.Setup(x => x.Message).Returns((MessageCreatedEvent)null);

            // Act & Assert
            var exception = await Assert.ThrowsAsync<ArgumentNullException>(() => _consumer.Consume(contextMock.Object));

            // Verify that the exception message contains the expected text
            Assert.Contains("Message cannot be null", exception.Message);
        }

        [Fact]
        public async Task Consume_ShouldSendWebSocketNotification_WhenMessageIsValid()
        {
            // Arrange
            var message = new MessageCreatedEvent
            {
                Id = Guid.NewGuid(),
                SenderId = Guid.NewGuid(),
                ReceiverId = Guid.NewGuid(),
                Text = "Hello via WebSocket!",
                UserAuthToken = "valid_token"
            };

            var connectionIds = new List<string> { "connectionId1", "connectionId2" };
            _connectionManagerMock.Setup(cm => cm.GetConnectionIdsByUserId(message.ReceiverId))
                .Returns(connectionIds);

            var contextMock = CreateMessageContext(message);

            // Act
            await _consumer.Consume(contextMock.Object);

            // Assert
            _websocketNotificationServiceMock.Verify(
                x => x.SendNotificationAsync(
                    It.Is<MessageCreatedEvent>(msg => msg.Id == message.Id && msg.UserAuthToken == message.UserAuthToken),
                    It.Is<IEnumerable<string>>(ids => ids.SequenceEqual(connectionIds))),
                Times.Once,
                "Expected SendNotificationAsync to be called with the correct parameters."
            );
        }
    }
}
