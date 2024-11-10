namespace Chat.Api.DelegatingHandlers;

public class LoggingHandler(ILogger<LoggingHandler> logger) : DelegatingHandler
{
    protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
    {
        logger.LogInformation("Request:");
        logger.LogInformation(request.ToString());
        if (request.Content != null)
        {
            var requestContent = await request.Content.ReadAsStringAsync(cancellationToken);
            logger.LogInformation(requestContent);
        }

        var response = await base.SendAsync(request, cancellationToken);

        logger.LogInformation("Response:");
        logger.LogInformation(response.ToString());
        var responseContent = await response.Content.ReadAsStringAsync(cancellationToken);
        logger.LogInformation(responseContent);

        return response;
    }
}