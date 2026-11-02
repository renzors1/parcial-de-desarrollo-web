using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class PromocionDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("nombre")]
    [Required(ErrorMessage = "El nombre de la promoción es obligatorio.")]
    [StringLength(100, MinimumLength = 3, ErrorMessage = "El nombre debe tener entre 3 y 100 caracteres.")]
    public string Nombre { get; set; } = string.Empty;

    [JsonPropertyName("descripcion")]
    [StringLength(300, ErrorMessage = "La descripción no puede superar los 300 caracteres.")]
    public string? Descripcion { get; set; }

    [JsonPropertyName("porcentajeDescuento")]
    [Required(ErrorMessage = "El porcentaje de descuento es obligatorio.")]
    [Range(1, 100, ErrorMessage = "El descuento debe situarse entre 1% y 100%.")]
    public float PorcentajeDescuento { get; set; }

    [JsonPropertyName("cantidadDefinida")]
    [Range(1, 10000, ErrorMessage = "La cantidad asignada debe estar entre 1 y 10,000 cupones.")]
    public int CantidadDefinida { get; set; } = 100;

    [JsonPropertyName("cantidadDisponible")]
    public int CantidadDisponible { get; set; } = 100;

    [JsonPropertyName("fechaInicio")]
    public DateTime FechaInicio { get; set; } = DateTime.UtcNow;

    [JsonPropertyName("fechaFin")]
    public DateTime FechaFin { get; set; } = DateTime.UtcNow.AddMonths(1);

    [JsonPropertyName("activo")]
    public bool Activo { get; set; } = true;

    [JsonPropertyName("libroses")]
    public List<LibroDto> Libroses { get; set; } = new();

    [JsonIgnore]
    public bool EsVigente => Activo && CantidadDisponible > 0 && DateTime.UtcNow >= FechaInicio && DateTime.UtcNow <= FechaFin;

    [JsonIgnore]
    public int CuposUsados => Math.Max(0, CantidadDefinida - CantidadDisponible);
}
