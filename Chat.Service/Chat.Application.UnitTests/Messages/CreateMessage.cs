using Chat.Application.Abstractions.EventBus;
using Chat.Application.Messages.Create;
using Chat.Domain.Abstractions;
using Chat.Domain.Abstractions.IProviders;
using Chat.Domain.Abstractions.IServices;
using Chat.Domain.Entities;
using FluentAssertions;
using FluentValidation;
using Moq;

namespace Chat.Application.UnitTests.Messages;

public class CreateMessageCommandHandlerTests
{
    private readonly Mock<IUnitOfWork> _unitOfWorkMock;
    private readonly Mock<IEncryptionService> _encryptionServiceMock;
    private readonly Mock<IEventBus> _eventBusMock;
    private readonly Mock<IUserContextProvider> _userContextProviderMock;
    private readonly CreateMessageCommandHandler _handler;
    private readonly CreateMessageCommandValidator _validator;

    public CreateMessageCommandHandlerTests()
    {
        _unitOfWorkMock = new Mock<IUnitOfWork>();
        _encryptionServiceMock = new Mock<IEncryptionService>();
        _eventBusMock = new Mock<IEventBus>();
        _userContextProviderMock = new Mock<IUserContextProvider>();
        _validator = new CreateMessageCommandValidator();

        _handler = new CreateMessageCommandHandler(
            _unitOfWorkMock.Object,
            _encryptionServiceMock.Object,
            _eventBusMock.Object,
            _userContextProviderMock.Object
        );
    }

    #region Validator

    [Fact]
    public async Task Validator_ShouldReturnError_WhenReceiverIdIsEmpty()
    {
        // Arrange
        var senderId = Guid.NewGuid();
        var command = new CreateMessageCommand(senderId, Guid.Empty, "Hello!");

        // Act
        var result = await _validator.ValidateAsync(command);

        // Assert
        result.IsValid.Should().BeFalse();
        result.Errors.Should().ContainSingle(error =>
            error.PropertyName == "ReceiverId" && error.ErrorMessage == "'Receiver Id' must not be empty.");
    }

    [Fact]
    public async Task Validator_ShouldReturnError_WhenSenderIdIsEmpty()
    {
        // Arrange
        var receiverId = Guid.NewGuid();
        var command = new CreateMessageCommand(Guid.Empty, receiverId, "Hello!");

        // Act
        var result = await _validator.ValidateAsync(command);

        // Assert
        result.IsValid.Should().BeFalse();
        result.Errors.Should().ContainSingle(error =>
            error.PropertyName == "SenderId" && error.ErrorMessage == "'Sender Id' must not be empty.");
    }

    [Fact]
    public async Task ShouldReturnError_WhenMessageIsEmpty()
    {
        // Arrange
        var senderId = Guid.NewGuid();
        var receiverId = Guid.NewGuid();
        var command = new CreateMessageCommand(senderId, receiverId, string.Empty);

        // Act
        var result = await _validator.ValidateAsync(command);

        // Assert
        result.IsValid.Should().BeFalse();
        result.Errors.Should().ContainSingle(error =>
            error.PropertyName == "Text" && error.ErrorMessage == "'Text' must not be empty.");
    }
    
    [Fact]
    public async Task ShouldReturnError_WhenSenderAndReceiverAreSame()
    {
        // Arrange
        var userId = Guid.NewGuid();
        var command = new CreateMessageCommand(userId, userId, "Hello, World!");

        // Act
        var result = await _validator.ValidateAsync(command);

        // Assert
        result.IsValid.Should().BeFalse();
        result.Errors.Should().ContainSingle(error =>
            error.PropertyName == "ReceiverId" && error.ErrorMessage == "'Receiver Id' must not be the same as 'Sender Id'.");
    }
    
    [Fact]
    public async Task ShouldReturnError_WhenMessageTextExceedsMaxLength()
    {
        // Arrange
        var senderId = Guid.NewGuid();
        var receiverId = Guid.NewGuid();
        var longText = new string('A', 501);

        var command = new CreateMessageCommand(senderId, receiverId, longText);

        // Act
        var result = await _validator.ValidateAsync(command);

        // Assert
        result.IsValid.Should().BeFalse();
        result.Errors.Should().ContainSingle(error =>
            error.PropertyName == "Text" && error.ErrorMessage == "'Text' must be between 1 and 500 characters.");
    }

    #endregion

    #region Handler
    
    [Fact]
    public async Task Handler_ShouldHandleEncryptionServiceFailure()
    {
        // Arrange
        var senderId = Guid.NewGuid();
        var receiverId = Guid.NewGuid();
        var command = new CreateMessageCommand(senderId, receiverId, "Hello, World!");

        _encryptionServiceMock.Setup(x => x.Encrypt(It.IsAny<string>())).Throws(new Exception("Encryption failed"));

        // Act
        var act = async () => await _handler.Handle(command, CancellationToken.None);

        // Assert
        await act.Should().ThrowAsync<Exception>().WithMessage("Encryption failed");

        _unitOfWorkMock.Verify(uow => uow.SaveChangesAsync(It.IsAny<CancellationToken>()), Times.Never);

        _eventBusMock.Verify(x => x.PublishAsync(It.IsAny<MessageCreatedEvent>(), It.IsAny<CancellationToken>()), Times.Never);
    }

    [Fact]
    public async Task Handle_ShouldEncryptTextAndSaveMessage()
    {
        // Arrange
        var command = new CreateMessageCommand(Guid.NewGuid(), Guid.NewGuid(), "Hello, World!");

        const string encryptedText = "EncryptedText";
        _encryptionServiceMock.Setup(x => x.Encrypt(It.IsAny<string>())).Returns(encryptedText);

        var mockMessageRepo = new Mock<IGenericRepository<Message>>();
        _unitOfWorkMock.Setup(uow => uow.Messages).Returns(mockMessageRepo.Object);
        _unitOfWorkMock.Setup(uow => uow.SaveChangesAsync(It.IsAny<CancellationToken>()))
            .Returns(Task.CompletedTask);

        // Act
        await _handler.Handle(command, CancellationToken.None);

        // Assert
        _encryptionServiceMock.Verify(x => x.Encrypt("Hello, World!"), Times.Once);
        _unitOfWorkMock.Verify(uow => uow.Messages.Add(It.Is<Message>(m => m.Text == encryptedText)), Times.Once);
        _unitOfWorkMock.Verify(uow => uow.SaveChangesAsync(It.IsAny<CancellationToken>()), Times.Once);
    }
    
    [Fact]
    public async Task Handler_ShouldHandleEncryptionReturningEmptyString()
    {
        // Arrange
        var command = new CreateMessageCommand(Guid.NewGuid(), Guid.NewGuid(), "Hello, World!");
        _encryptionServiceMock.Setup(x => x.Encrypt(It.IsAny<string>())).Returns(string.Empty);

        // Act
        var act = async () => await _handler.Handle(command, CancellationToken.None);

        // Assert
        await act.Should().ThrowAsync<InvalidOperationException>().WithMessage("Failed to encrypt the message.");
    }
    
    [Fact]
    public async Task Handle_ShouldPublishMessageCreatedEvent()
    {
        // Arrange
        var senderId = Guid.NewGuid();
        var receiverId = Guid.NewGuid();
        var command = new CreateMessageCommand(senderId, receiverId, "Hello, World!");

        var encryptedText = "EncryptedText";
        var decryptedText = "Hello, World!";
        _encryptionServiceMock.Setup(x => x.Encrypt(It.IsAny<string>())).Returns(encryptedText);
        _userContextProviderMock.Setup(x => x.GetTokenFromRequest()).Returns("UserToken");

        var message = new Message
        {
            SenderId = senderId,
            ReceiverId = receiverId,
            Text = encryptedText,
            CreatedAt = DateTime.UtcNow
        };

        _unitOfWorkMock.Setup(uow => uow.SaveChangesAsync(It.IsAny<CancellationToken>()))
            .Returns(Task.CompletedTask);
        _unitOfWorkMock.Setup(uow => uow.Messages.Add(It.IsAny<Message>()));

        // Act
        await _handler.Handle(command, CancellationToken.None);

        // Assert
        _eventBusMock.Verify(x => x.PublishAsync(It.Is<MessageCreatedEvent>(e =>
                e.Id == message.Id &&
                e.Text == decryptedText &&
                e.SenderId == message.SenderId &&
                e.ReceiverId == message.ReceiverId &&
                e.UserAuthToken == "UserToken"),
            It.IsAny<CancellationToken>()), Times.Once);
    }

    #endregion
}