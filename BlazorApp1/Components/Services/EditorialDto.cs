using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class EditorialDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("nombreEditorial")]
    public string NombreEditorial { get; set; } = string.Empty;

    [JsonPropertyName("pais")]
    public string? Pais { get; set; }

    [JsonPropertyName("activo")]
    public bool Activo { get; set; } = true;

    [JsonPropertyName("usuarioCreacion")]
    public string? UsuarioCreacion { get; set; }

    [JsonPropertyName("fechaCreacion")]
    public DateTime? FechaCreacion { get; set; }
}
