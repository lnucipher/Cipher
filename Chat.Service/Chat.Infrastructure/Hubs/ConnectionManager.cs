using Chat.Application.Abstractions;

namespace Chat.Infrastructure.Hubs;

public class ConnectionManager : IConnectionManager
{
    private static readonly Dictionary<string, Guid> Users = new();

    public IEnumerable<string> GetConnectionIdsByUserId(Guid userId)
    {
        return Users.Where(pair => pair.Value == userId).Select(pair => pair.Key);
    }

    public bool IsUserConnected(Guid userId)
    {
        return Users.ContainsValue(userId);
    }

    public void AddConnection(string connectionId, Guid userId)
    {
        Users.TryAdd(connectionId, userId);
    }

    public bool RemoveConnection(string connectionId, out Guid userId)
    {
        return Users.Remove(connectionId, out userId);
    }
}