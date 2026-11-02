using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class EditorialDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("nombreEditorial")]
    [Required(ErrorMessage = "El nombre de la editorial es obligatorio.")]
    [StringLength(100, MinimumLength = 2, ErrorMessage = "La editorial debe tener entre 2 y 100 caracteres.")]
    public string NombreEditorial { get; set; } = string.Empty;

    [JsonPropertyName("pais")]
    [StringLength(50, ErrorMessage = "El país no puede superar los 50 caracteres.")]
    public string? Pais { get; set; }

    [JsonPropertyName("activo")]
    public bool Activo { get; set; } = true;

    [JsonPropertyName("usuarioCreacion")]
    public string? UsuarioCreacion { get; set; }

    [JsonPropertyName("fechaCreacion")]
    public DateTime? FechaCreacion { get; set; }
}
