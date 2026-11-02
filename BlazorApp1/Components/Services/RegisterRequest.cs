using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class RegisterRequest
{
    [JsonPropertyName("login")]
    [Required(ErrorMessage = "El nombre de usuario es obligatorio.")]
    [StringLength(50, MinimumLength = 3, ErrorMessage = "El usuario debe tener entre 3 y 50 caracteres.")]
    [RegularExpression(@"^[a-zA-Z0-9_\.-]+$", ErrorMessage = "Solo se permiten letras, números, puntos y guiones.")]
    public string Login { get; set; } = string.Empty;

    [JsonPropertyName("email")]
    [Required(ErrorMessage = "El correo electrónico es obligatorio.")]
    [EmailAddress(ErrorMessage = "Formato de correo electrónico inválido.")]
    [StringLength(100, ErrorMessage = "El correo no puede exceder los 100 caracteres.")]
    public string Email { get; set; } = string.Empty;

    [JsonPropertyName("password")]
    [Required(ErrorMessage = "La contraseña es obligatoria.")]
    [StringLength(100, MinimumLength = 4, ErrorMessage = "La contraseña debe tener entre 4 y 100 caracteres.")]
    public string Password { get; set; } = string.Empty;

    [JsonPropertyName("firstName")]
    [StringLength(50, ErrorMessage = "El nombre no puede superar los 50 caracteres.")]
    public string? FirstName { get; set; }

    [JsonPropertyName("lastName")]
    [StringLength(50, ErrorMessage = "Los apellidos no pueden superar los 50 caracteres.")]
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
