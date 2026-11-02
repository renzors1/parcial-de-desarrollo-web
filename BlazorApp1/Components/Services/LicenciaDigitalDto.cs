using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class LicenciaDigitalDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("codigoLicencia")]
    [Required(ErrorMessage = "El código de licencia es obligatorio.")]
    [StringLength(50, MinimumLength = 5, ErrorMessage = "El código debe tener entre 5 y 50 caracteres.")]
    public string CodigoLicencia { get; set; } = string.Empty;

    [JsonPropertyName("enlaceDescarga")]
    [StringLength(500, ErrorMessage = "El enlace no puede superar los 500 caracteres.")]
    public string? EnlaceDescarga { get; set; }

    [JsonPropertyName("fechaAsignacion")]
    public DateTime? FechaAsignacion { get; set; }

    [JsonPropertyName("estado")]
    public string Estado { get; set; } = "DISPONIBLE"; // "DISPONIBLE", "VENDIDA"

    [JsonPropertyName("libro")]
    public LibroDto? Libro { get; set; }

    [JsonPropertyName("detalleVenta")]
    public DetalleVentaDto? DetalleVenta { get; set; }

    [JsonPropertyName("usuarioCreacion")]
    [StringLength(50, ErrorMessage = "El usuario no puede superar 50 caracteres.")]
    public string? UsuarioCreacion { get; set; }

    [JsonPropertyName("fechaCreacion")]
    public DateTime? FechaCreacion { get; set; }

    [JsonPropertyName("usuarioModificacion")]
    [StringLength(50, ErrorMessage = "El usuario no puede superar 50 caracteres.")]
    public string? UsuarioModificacion { get; set; }

    [JsonPropertyName("fechaModificacion")]
    public DateTime? FechaModificacion { get; set; }

    [JsonIgnore]
    public bool EstaDisponible => Estado == "DISPONIBLE";

    [JsonIgnore]
    public bool EstaVendida => Estado == "VENDIDA";
}
