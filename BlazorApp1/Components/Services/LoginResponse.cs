using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class LoginResponse
{
    [JsonPropertyName("id_token")]
    public string IdToken { get; set; } = string.Empty;
}
