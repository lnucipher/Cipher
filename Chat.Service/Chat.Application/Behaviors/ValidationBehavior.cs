using Chat.Application.Models.CQRSMessaging;
using FluentValidation;
using FluentValidation.Results;
using MediatR;

namespace Chat.Application.Behaviors;

public class ValidationBehavior<TRequest, TResponse>(IEnumerable<IValidator<TRequest>> validators)
    : IPipelineBehavior<TRequest, TResponse>
    where TRequest : ICommand
{
    public async Task<TResponse> Handle(TRequest request, RequestHandlerDelegate<TResponse> next,
        CancellationToken cancellationToken)
    {
        var context = new ValidationContext<TRequest>(request);

        var validationFailures = await Task.WhenAll(
            validators.Select(v => v.ValidateAsync(context, cancellationToken)));

        var errors = validationFailures
            .Where(result => !result.IsValid)
            .SelectMany(result => result.Errors)
            .Select(failure => new 
            {
                failure.PropertyName,
                failure.ErrorMessage
            })
            .ToList();

        if (errors.Any())
        {
            throw new ValidationException(errors.Select(e => new ValidationFailure(e.PropertyName, e.ErrorMessage)));
        }

        var response = await next();

        return response;
    }
}