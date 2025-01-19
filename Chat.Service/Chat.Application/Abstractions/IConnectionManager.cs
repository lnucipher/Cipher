namespace Chat.Application.Abstractions;

public interface IConnectionManager
{
    IEnumerable<string> GetConnectionIdsByUserId(Guid userId);
    bool IsUserConnected(Guid userId);
    void AddConnection(string connectionId, Guid userId);
    bool RemoveConnection(string connectionId, out Guid userId);
}