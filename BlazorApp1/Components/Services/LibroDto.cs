using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class CategoriaDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("nombreCategoria")]
    public string NombreCategoria { get; set; } = string.Empty;

    [JsonPropertyName("activo")]
    public bool? Activo { get; set; }
}

public class AutorDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("nombres")]
    public string Nombres { get; set; } = string.Empty;

    [JsonPropertyName("apellidos")]
    public string Apellidos { get; set; } = string.Empty;

    [JsonIgnore]
    public string NombreCompleto => $"{Nombres} {Apellidos}".Trim();
}

public class LibroDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("isbn")]
    public string Isbn { get; set; } = string.Empty;

    [JsonPropertyName("titulo")]
    public string Titulo { get; set; } = string.Empty;

    [JsonPropertyName("sinopsis")]
    public string Sinopsis { get; set; } = string.Empty;

    [JsonPropertyName("precio")]
    public decimal Precio { get; set; }

    [JsonPropertyName("idioma")]
    public string Idioma { get; set; } = "Español";

    [JsonPropertyName("activo")]
    public bool Activo { get; set; } = true;

    [JsonPropertyName("portadaUrl")]
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
