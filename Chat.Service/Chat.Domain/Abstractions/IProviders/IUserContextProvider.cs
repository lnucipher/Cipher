namespace Chat.Domain.Abstractions.IProviders;

public interface IUserContextProvider
{ 
    string? GetTokenFromRequest();
}