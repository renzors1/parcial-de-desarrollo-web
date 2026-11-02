using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class CategoriaDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("nombreCategoria")]
    [Required(ErrorMessage = "El nombre de la categoría es obligatorio.")]
    [StringLength(60, MinimumLength = 2, ErrorMessage = "La categoría debe tener entre 2 y 60 caracteres.")]
    public string NombreCategoria { get; set; } = string.Empty;

    [JsonPropertyName("activo")]
    public bool? Activo { get; set; } = true;
}

public class AutorDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("nombres")]
    [Required(ErrorMessage = "El nombre del autor es obligatorio.")]
    [StringLength(60, MinimumLength = 2, ErrorMessage = "El nombre debe tener entre 2 y 60 caracteres.")]
    public string Nombres { get; set; } = string.Empty;

    [JsonPropertyName("apellidos")]
    [StringLength(60, ErrorMessage = "Los apellidos no pueden superar los 60 caracteres.")]
    public string Apellidos { get; set; } = string.Empty;

    [JsonIgnore]
    public string NombreCompleto => $"{Nombres} {Apellidos}".Trim();
}

public class LibroDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("isbn")]
    [Required(ErrorMessage = "El código ISBN es obligatorio.")]
    [StringLength(20, MinimumLength = 10, ErrorMessage = "El ISBN debe tener entre 10 y 20 caracteres (ej. 978-0134494164).")]
    public string Isbn { get; set; } = string.Empty;

    [JsonPropertyName("titulo")]
    [Required(ErrorMessage = "El título del libro es obligatorio.")]
    [StringLength(150, MinimumLength = 2, ErrorMessage = "El título debe tener entre 2 y 150 caracteres.")]
    public string Titulo { get; set; } = string.Empty;

    [JsonPropertyName("sinopsis")]
    [StringLength(1000, ErrorMessage = "La sinopsis no puede exceder los 1,000 caracteres.")]
    public string Sinopsis { get; set; } = string.Empty;

    [JsonPropertyName("precio")]
    [Required(ErrorMessage = "El precio es obligatorio.")]
    [Range(0.50, 9999.99, ErrorMessage = "El precio debe estar entre S/ 0.50 y S/ 9,999.99.")]
    public decimal Precio { get; set; }

    [JsonPropertyName("idioma")]
    [StringLength(30, ErrorMessage = "El idioma no puede superar los 30 caracteres.")]
    public string Idioma { get; set; } = "Español";

    [JsonPropertyName("activo")]
    public bool Activo { get; set; } = true;

    [JsonPropertyName("portadaUrl")]
    [StringLength(500, ErrorMessage = "La URL de portada no puede superar 500 caracteres.")]
    public string? PortadaUrl { get; set; }

    [JsonPropertyName("categoriases")]
    public List<CategoriaDto> Categoriases { get; set; } = new();

    [JsonPropertyName("autoreses")]
    public List<AutorDto> Autoreses { get; set; } = new();

    [JsonPropertyName("editorial")]
    public EditorialDto? Editorial { get; set; }

    [JsonPropertyName("promocioneses")]
    public List<PromocionDto> Promociones { get; set; } = new();

    // Propiedades de conveniencia y compatibilidad con vistas
    public string Autor
    {
        get => AutorPrincipal;
        set
        {
            var parts = (value ?? string.Empty).Trim().Split(' ', 2, StringSplitOptions.RemoveEmptyEntries);
            var nombres = parts.Length > 0 ? parts[0] : string.Empty;
            var apellidos = parts.Length > 1 ? parts[1] : string.Empty;

            if (Autoreses.Count > 0)
            {
                Autoreses[0].Nombres = nombres;
                Autoreses[0].Apellidos = apellidos;
            }
            else
            {
                Autoreses.Add(new AutorDto { Nombres = nombres, Apellidos = apellidos });
            }
        }
    }

    [Range(0, 99999, ErrorMessage = "El stock debe estar entre 0 y 99,999 unidades.")]
    public int Stock { get; set; } = 999;

    public long? CategoriaId
    {
        get => Categoriases.FirstOrDefault()?.Id;
        set
        {
            if (value.HasValue)
            {
                if (Categoriases.Count > 0) Categoriases[0].Id = value;
                else Categoriases.Add(new CategoriaDto { Id = value });
            }
        }
    }

    [JsonIgnore]
    public string AutorPrincipal => Autoreses.Count > 0 ? Autoreses[0].NombreCompleto : "Autor de Catálogo";

    [JsonIgnore]
    public string CategoriaPrincipal => Categoriases.Count > 0 ? Categoriases[0].NombreCategoria : "Digital";

    [JsonIgnore]
    public string NombreEditorial => Editorial?.NombreEditorial ?? "Editorial Digital";

    [JsonIgnore]
    public PromocionDto? PromocionActiva => Promociones.FirstOrDefault(p => p.EsVigente);

    [JsonIgnore]
    public decimal PrecioFinal => PromocionActiva != null
        ? Math.Round(Precio * (decimal)(1 - (PromocionActiva.PorcentajeDescuento / 100.0f)), 2)
        : Precio;
}
