using FluentValidation;

namespace Chat.Application.Messages.Create;

public class CreateMessageCommandValidator : AbstractValidator<CreateMessageCommand>
{
    public CreateMessageCommandValidator()
    {
        RuleFor(x => x.Text)
            .NotEmpty()
            .Length(1, 500)
            .WithMessage("'Text' must be between 1 and 500 characters.");
        
        RuleFor(x => x.SenderId)
            .NotEmpty();
        
        RuleFor(x => x.ReceiverId)
            .NotEmpty();
        
        RuleFor(x => x.ReceiverId)
            .NotEqual(x => x.SenderId)
            .WithMessage("'Receiver Id' must not be the same as 'Sender Id'.");
    }
}