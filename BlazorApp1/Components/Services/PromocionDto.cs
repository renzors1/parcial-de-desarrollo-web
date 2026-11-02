using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class PromocionDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("nombre")]
    public string Nombre { get; set; } = string.Empty;

    [JsonPropertyName("descripcion")]
    public string? Descripcion { get; set; }

    [JsonPropertyName("porcentajeDescuento")]
    public float PorcentajeDescuento { get; set; }

    [JsonPropertyName("cantidadDefinida")]
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
