using System.Net.Http.Headers;
using System.Text;
using Chat.Domain.Abstractions.IServices;
using Chat.Domain.Enums;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;

namespace Chat.Infrastructure.Services;

public class UserService(
    IHttpClientFactory httpClientFactory)
    : IUserService
{
    private readonly HttpClient _httpClient = httpClientFactory.CreateClient("Identity.Service");
    private readonly JsonSerializerSettings _serializerSettings = new()
    {
        ContractResolver = new CamelCasePropertyNamesContractResolver(),
        Converters = new List<JsonConverter> { new StringEnumConverter() }
    };

    public void SetAuthToken(string? token)
    {
        _httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);
    }

    public async Task UpdateUserStatusAsync(Guid userId, UserStatusEnum userStatus)
    {
        const string endpoint = "api/users/status";
        var payload = new UserStatusDto(userId.ToString().ToUpper(), userStatus);

        var content = new StringContent(JsonConvert.SerializeObject(payload, _serializerSettings), Encoding.UTF8,
            "application/json");
        
        var response = await _httpClient.PatchAsync(endpoint, content);
        response.EnsureSuccessStatusCode();
    }

    public async Task UpdateLastInteractionAsync(Guid senderId, Guid receiverId, DateTimeOffset lastInteraction)
    {
        const string endpoint = "api/contacts/lastInteraction";
        var payload = new
        {
            PrimaryUserId = senderId.ToString().ToUpper(),
            SecondaryUserId = receiverId.ToString().ToUpper(),
            Timestamp = lastInteraction.ToString("yyyy-MM-ddTHH:mm:ss.fffZ")
        };
        
        var content = new StringContent(JsonConvert.SerializeObject(payload, _serializerSettings), Encoding.UTF8, "application/json");
        
        var response = await _httpClient.PatchAsync(endpoint, content);
        response.EnsureSuccessStatusCode();
    }

    public async Task<IEnumerable<Guid>> GetContactsByUserId(Guid userId)
    {
        var endpoint = $"api/contactIds?userId={userId.ToString().ToUpper()}";
        
        var response = await _httpClient.GetAsync(endpoint);
        response.EnsureSuccessStatusCode();
        
        var content = await response.Content.ReadAsStringAsync();
        var contacts = JsonConvert.DeserializeObject<IEnumerable<Guid>>(content, _serializerSettings);
        
        return contacts;
    }
}