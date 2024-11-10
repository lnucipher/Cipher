using System.Text.Json;
using System.Text.Json.Serialization;

namespace Chat.Application.Converters;

public class UppercaseGuidConverter : JsonConverter<Guid>
{
    public override Guid Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
    {
        if (reader.TokenType == JsonTokenType.String)
        {
            var strValue = reader.GetString();
            return Guid.TryParse(strValue, out Guid guid) ? guid : Guid.Empty;
        }
        return Guid.Empty;
    }

    public override void Write(Utf8JsonWriter writer, Guid value, JsonSerializerOptions options)
    {
        writer.WriteStringValue(value.ToString("D").ToUpper());
    }
}
