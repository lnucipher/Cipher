using FluentValidation;
using Microsoft.AspNetCore.Mvc;

namespace Chat.Api.Middleware;

public class ExceptionHandlingMiddleware(RequestDelegate next, ILogger<ExceptionHandlingMiddleware> logger)
{
    public async Task InvokeAsync(HttpContext context)
    {
        try
        {
            await next(context);
        }
        catch (Exception ex)
        {
            logger.LogError(ex, "Exception occurred: {Message}", ex.Message);

            var details = GetExceptionDetails(ex);
            
            var problemDetails = new ProblemDetails
            {
                Status = details.Status,
                Type = details.Type,
                Title = details.Title,
                Detail = details.Detail,
                Instance = context.Request.Path
            };
            
            if (details.Errors is not null)
            {
                problemDetails.Extensions["errors"] = details.Errors;
            }
            
            context.Response.StatusCode = details.Status;
            
            await context.Response.WriteAsJsonAsync(problemDetails);
        }
    }

    private static ExceptionDetails GetExceptionDetails(Exception ex)
    {
        return ex switch
        {
            ValidationException validationException => new ExceptionDetails(
                StatusCodes.Status400BadRequest,
                "ValidationFailure",
                "Validation failed",
                "One or more validation failures have occurred",
                validationException.Errors),
            _ => new ExceptionDetails(
                StatusCodes.Status500InternalServerError,
                "InternalServerError",
                "Server error",
                "A unexpected error occurred",
                null)
        };
    }

    private record ExceptionDetails(
        int Status,
        string Type,
        string Title,
        string Detail,
        IEnumerable<object?>? Errors);
}