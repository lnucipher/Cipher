namespace Chat.Domain.Abstractions.Base;

public abstract record EventBase
{
    public string? UserAuthToken { get; set; }
}