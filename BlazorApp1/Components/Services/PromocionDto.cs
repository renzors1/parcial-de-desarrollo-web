using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class PromocionDto
{
    private string? _codigoCupon;

    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("nombre")]
    [Required(ErrorMessage = "El nombre de la promoción es obligatorio.")]
    [StringLength(100, MinimumLength = 3, ErrorMessage = "El nombre debe tener entre 3 y 100 caracteres.")]
    public string Nombre { get; set; } = string.Empty;

    [JsonPropertyName("descripcion")]
    [StringLength(300, ErrorMessage = "La descripción no puede superar los 300 caracteres.")]
    public string? Descripcion { get; set; }

    [JsonPropertyName("esCupon")]
    public bool EsCupon { get; set; } = false;

    [JsonIgnore]
    public bool EsOfertaCatalogo => !EsCupon;

    [JsonPropertyName("codigoCupon")]
    public string CodigoCupon
    {
        get
        {
            if (!string.IsNullOrWhiteSpace(_codigoCupon))
                return _codigoCupon.Trim().ToUpperInvariant();

            if (EsCupon)
            {
                if (!string.IsNullOrWhiteSpace(Nombre))
                {
                    var extraido = ExtraerCodigoDeNombre(Nombre);
                    if (!string.IsNullOrWhiteSpace(extraido))
                        return extraido;
                }

                return GenerarCodigoFallback(Nombre);
            }

            return string.Empty;
        }
        set => _codigoCupon = value;
    }

    [JsonPropertyName("tipoDescuento")]
    public string TipoDescuento { get; set; } = "PORCENTAJE";

    [JsonPropertyName("porcentajeDescuento")]
    [Range(0, 100, ErrorMessage = "El descuento debe situarse entre 0% y 100%.")]
    public float PorcentajeDescuento { get; set; }

    [JsonPropertyName("descuentoFijo")]
    [Range(0, 100000, ErrorMessage = "El descuento fijo no puede ser negativo.")]
    public decimal DescuentoFijo { get; set; } = 0m;

    [JsonPropertyName("descuentoMontoFijo")]
    public decimal DescuentoMontoFijo
    {
        get => DescuentoFijo;
        set => DescuentoFijo = value;
    }

    [JsonPropertyName("montoMinimoCompra")]
    [Range(0, 100000, ErrorMessage = "El monto mínimo de compra no puede ser negativo.")]
    public decimal? MontoMinimoCompra { get; set; }

    [JsonPropertyName("topeDescuento")]
    [Range(0, 100000, ErrorMessage = "El tope de descuento no puede ser negativo.")]
    public decimal? TopeDescuento { get; set; }

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
    public bool EsMontoFijo => string.Equals(TipoDescuento, "MONTO_FIJO", StringComparison.OrdinalIgnoreCase);

    [JsonIgnore]
    public bool EsPorcentaje => !EsMontoFijo;

    [JsonIgnore]
    public int DiasRestantes
    {
        get
        {
            var fin = FechaFin.Kind == DateTimeKind.Utc ? FechaFin.ToLocalTime() : FechaFin;
            var hoy = DateTime.Today;
            var diff = (fin.Date - hoy).Days;
            return Math.Max(0, diff);
        }
    }

    [JsonIgnore]
    public bool EsVigente
    {
        get
        {
            if (!Activo || CantidadDisponible <= 0) return false;

            var ahora = DateTime.Now;
            var inicio = FechaInicio.Kind == DateTimeKind.Utc ? FechaInicio.ToLocalTime() : FechaInicio;
            var fin = FechaFin.Kind == DateTimeKind.Utc ? FechaFin.ToLocalTime() : FechaFin;

            // La campaña dura todo el día de la fecha de término indicada (hasta las 23:59:59).
            var finDelDia = fin.Date.AddDays(1).AddTicks(-1);

            return ahora >= inicio.Date && ahora <= finDelDia;
        }
    }

    [JsonIgnore]
    public bool EsPorVencer => EsVigente && DiasRestantes <= 3;

    [JsonIgnore]
    public bool EsProgramada
    {
        get
        {
            if (!Activo) return false;
            var ahora = DateTime.Now;
            var inicio = FechaInicio.Kind == DateTimeKind.Utc ? FechaInicio.ToLocalTime() : FechaInicio;
            return ahora < inicio;
        }
    }

    [JsonIgnore]
    public string EstadoCalculado
    {
        get
        {
            if (!Activo) return "INACTIVA";

            var ahora = DateTime.Now;
            var inicio = FechaInicio.Kind == DateTimeKind.Utc ? FechaInicio.ToLocalTime() : FechaInicio;
            var fin = FechaFin.Kind == DateTimeKind.Utc ? FechaFin.ToLocalTime() : FechaFin;
            var finDelDia = fin.Date.AddDays(1).AddTicks(-1);

            if (ahora < inicio) return "PROGRAMADA";
            if (ahora > finDelDia) return "EXPIRADA";
            if (CantidadDisponible <= 0) return "AGOTADA";
            if (DiasRestantes <= 3) return "POR_VENCER";
            return "VIGENTE";
        }
    }

    [JsonIgnore]
    public int CuposUsados => Math.Max(0, CantidadDefinida - CantidadDisponible);

    public bool AplicaALibro(long? libroId)
    {
        if (Libroses == null || Libroses.Count == 0) return true;
        if (!libroId.HasValue) return false;
        return Libroses.Any(l => l.Id == libroId.Value);
    }

    public decimal CalcularDescuento(decimal subtotalAplicable)
    {
        if (subtotalAplicable <= 0) return 0m;
        if (MontoMinimoCompra.HasValue && subtotalAplicable < MontoMinimoCompra.Value) return 0m;

        decimal descuento;
        if (EsMontoFijo)
        {
            descuento = Math.Min(DescuentoFijo, subtotalAplicable);
        }
        else
        {
            descuento = Math.Round(subtotalAplicable * (decimal)(PorcentajeDescuento / 100.0f), 2);
        }

        if (TopeDescuento.HasValue && TopeDescuento.Value > 0)
        {
            descuento = Math.Min(descuento, TopeDescuento.Value);
        }

        return Math.Max(0m, descuento);
    }

    private static string? ExtraerCodigoDeNombre(string nombre)
    {
        if (string.IsNullOrWhiteSpace(nombre)) return null;
        var partes = nombre.Trim().Split(new[] { ' ', '-', ':', '_', '|' }, StringSplitOptions.RemoveEmptyEntries);
        if (partes.Length > 0)
        {
            var primera = partes[0].Trim();
            if (primera.Length >= 3 && primera.All(c => char.IsLetterOrDigit(c)))
            {
                return primera.ToUpperInvariant();
            }
        }
        return null;
    }

    private static string GenerarCodigoFallback(string? nombre)
    {
        if (string.IsNullOrWhiteSpace(nombre))
            return "PROMO-0001";

        var hash = Math.Abs(nombre.Trim().GetHashCode()) % 9000 + 1000;
        return $"PROMO-{hash}";
    }
}
