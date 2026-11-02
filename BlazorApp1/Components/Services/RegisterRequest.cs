using System.Net.Http.Json;
using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class RegisterRequest
{
    [JsonPropertyName("login")]
    public string Login { get; set; } = string.Empty;

    [JsonPropertyName("email")]
    public string Email { get; set; } = string.Empty;

    [JsonPropertyName("password")]
    public string Password { get; set; } = string.Empty;

    [JsonPropertyName("firstName")]
    public string? FirstName { get; set; }

    [JsonPropertyName("lastName")]
    public string? LastName { get; set; }

    [JsonPropertyName("langKey")]
    public string LangKey { get; set; } = "es";
}

public class UserAccountDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("login")]
    public string Login { get; set; } = string.Empty;

    [JsonPropertyName("firstName")]
    public string? FirstName { get; set; }

    [JsonPropertyName("lastName")]
    public string? LastName { get; set; }

    [JsonPropertyName("email")]
    public string Email { get; set; } = string.Empty;

    [JsonPropertyName("authorities")]
    public List<string> Authorities { get; set; } = new();

    public bool IsAdmin => Authorities.Contains("ROLE_ADMIN");
}
