using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class CalificacionUsuarioDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("puntuacion")]
    [Required(ErrorMessage = "La puntuación es obligatoria.")]
    [Range(1, 5, ErrorMessage = "La puntuación debe ser entre 1 y 5 estrellas.")]
    public int Puntuacion { get; set; } = 5;

    [JsonPropertyName("comentario")]
    [StringLength(500, ErrorMessage = "El comentario no puede superar los 500 caracteres.")]
    public string? Comentario { get; set; }

    [JsonPropertyName("libroId")]
    public long? LibroId { get; set; }

    [JsonPropertyName("fechaCalificacion")]
    public DateTime FechaCalificacion { get; set; } = DateTime.UtcNow;

    [JsonPropertyName("activo")]
    public bool Activo { get; set; } = true;

    [JsonPropertyName("usuarioCreacion")]
    [StringLength(50, ErrorMessage = "El usuario no puede superar 50 caracteres.")]
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
