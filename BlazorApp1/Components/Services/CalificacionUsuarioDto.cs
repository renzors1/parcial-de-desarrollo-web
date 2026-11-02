using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class CalificacionUsuarioDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("puntuacion")]
    public int Puntuacion { get; set; } = 5;

    [JsonPropertyName("comentario")]
    public string? Comentario { get; set; }

    [JsonPropertyName("libroId")]
    public long? LibroId { get; set; }

    [JsonPropertyName("fechaCalificacion")]
    public DateTime FechaCalificacion { get; set; } = DateTime.UtcNow;

    [JsonPropertyName("activo")]
    public bool Activo { get; set; } = true;

    [JsonPropertyName("usuarioCreacion")]
    public string? UsuarioCreacion { get; set; }

    [JsonPropertyName("fechaCreacion")]
    public DateTime? FechaCreacion { get; set; }

    [JsonPropertyName("clienteEvaluado")]
    public UserAccountDto? ClienteEvaluado { get; set; }

    [JsonPropertyName("evaluadorTienda")]
    public UserAccountDto? EvaluadorTienda { get; set; }

    [JsonPropertyName("venta")]
    public VentaDto? Venta { get; set; }
}
