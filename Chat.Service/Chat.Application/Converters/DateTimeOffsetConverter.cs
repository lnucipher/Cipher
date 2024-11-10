using System.Text.Json;
using System.Text.Json.Serialization;

namespace Chat.Application.Converters;

public class DateTimeOffsetConverter : JsonConverter<DateTimeOffset>
{
    public override DateTimeOffset Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
    {
        if (reader.TokenType == JsonTokenType.String)
        {
            if (DateTimeOffset.TryParse(reader.GetString(), out var dateTimeOffset))
            {
                return dateTimeOffset;
            }
        }
        return default;
    }

    public override void Write(Utf8JsonWriter writer, DateTimeOffset value, JsonSerializerOptions options)
    {
        writer.WriteStringValue(value.ToString("yyyy-MM-ddTHH:mm:ss.fffZ"));
    }
}
