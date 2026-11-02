using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class LicenciaDigitalDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("codigoLicencia")]
    public string CodigoLicencia { get; set; } = string.Empty;

    [JsonPropertyName("enlaceDescarga")]
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
    public string? UsuarioCreacion { get; set; }

    [JsonPropertyName("fechaCreacion")]
    public DateTime? FechaCreacion { get; set; }

    [JsonPropertyName("usuarioModificacion")]
    public string? UsuarioModificacion { get; set; }

    [JsonPropertyName("fechaModificacion")]
    public DateTime? FechaModificacion { get; set; }

    [JsonIgnore]
    public bool EstaDisponible => Estado == "DISPONIBLE";

    [JsonIgnore]
    public bool EstaVendida => Estado == "VENDIDA";
}
