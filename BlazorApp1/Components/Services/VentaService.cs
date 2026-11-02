using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Services;

public class DetalleVentaDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("cantidadVenta")]
    public int CantidadVenta { get; set; } = 1;

    [JsonPropertyName("precioUnitario")]
    public decimal PrecioUnitario { get; set; }

    [JsonPropertyName("descuento")]
    public decimal Descuento { get; set; }

    private decimal _subtotal;

    [JsonPropertyName("subtotal")]
    public decimal Subtotal
    {
        get => _subtotal > 0 ? _subtotal : Math.Max(0, (CantidadVenta * PrecioUnitario) - Descuento);
        set => _subtotal = value;
    }

    [JsonPropertyName("libroId")]
    public long LibroId { get; set; }

    [JsonPropertyName("libroTitulo")]
    public string? LibroTitulo { get; set; }

    [JsonPropertyName("promocionAplicadaId")]
    public long? PromocionAplicadaId { get; set; }

    [JsonIgnore]
    public int Cantidad
    {
        get => CantidadVenta;
        set => CantidadVenta = value;
    }
}

public class VentaDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }

    [JsonPropertyName("numeroComprobante")]
    public string NumeroComprobante { get; set; } = string.Empty;

    [JsonPropertyName("subtotalVenta")]
    public decimal SubtotalVenta { get; set; }

    [JsonPropertyName("totalDescuento")]
    public decimal TotalDescuento { get; set; }

    [JsonPropertyName("igv")]
    public decimal Igv { get; set; }

    [JsonPropertyName("totalVenta")]
    public decimal TotalVenta { get; set; }

    [JsonPropertyName("fechaVenta")]
    public DateTime FechaVenta { get; set; } = DateTime.UtcNow;

    [JsonPropertyName("cliente")]
    public string Cliente { get; set; } = string.Empty;

    [JsonPropertyName("usuarioCreacion")]
    public string? UsuarioCreacion { get; set; }

    [JsonPropertyName("fechaCreacion")]
    public DateTime? FechaCreacion { get; set; }

    [JsonPropertyName("detalles")]
    public List<DetalleVentaDto> Detalles { get; set; } = new();

    [JsonIgnore]
    public DateTime Fecha
    {
        get => FechaVenta;
        set => FechaVenta = value;
    }

    [JsonIgnore]
    public decimal Total
    {
        get => TotalVenta;
        set => TotalVenta = value;
    }
}

public class VentaService
{
    private readonly HttpClient _http;
    private readonly TokenService _tokenService;
    private readonly BackendStatusService _backendStatus;

    private static readonly List<VentaDto> _ventasLocales = new()
    {
        new()
        {
            Id = 1,
            NumeroComprobante = "B001-00000101",
            FechaVenta = DateTime.UtcNow.AddDays(-3),
            Cliente = "carlos.mendoza",
            UsuarioCreacion = "carlos.mendoza",
            SubtotalVenta = 149.80m,
            TotalDescuento = 0.00m,
            Igv = 26.96m,
            TotalVenta = 176.76m,
            Detalles = new List<DetalleVentaDto>
            {
                new()
                {
                    Id = 1,
                    LibroId = 1,
                    LibroTitulo = "C# 13 and .NET 10 – Modern Cross-Platform Development",
                    CantidadVenta = 1,
                    PrecioUnitario = 89.90m,
                    Descuento = 0m,
                    Subtotal = 89.90m
                },
                new()
                {
                    Id = 2,
                    LibroId = 3,
                    LibroTitulo = "Cloud Native Go: Building Web Applications and Microservices",
                    CantidadVenta = 1,
                    PrecioUnitario = 59.90m,
                    Descuento = 0m,
                    Subtotal = 59.90m
                }
            }
        },
        new()
        {
            Id = 2,
            NumeroComprobante = "B001-00000102",
            FechaVenta = DateTime.UtcNow.AddDays(-2),
            Cliente = "ana.torres",
            UsuarioCreacion = "ana.torres",
            SubtotalVenta = 75.50m,
            TotalDescuento = 15.10m,
            Igv = 10.87m,
            TotalVenta = 71.27m,
            Detalles = new List<DetalleVentaDto>
            {
                new()
                {
                    Id = 3,
                    LibroId = 2,
                    LibroTitulo = "Clean Architecture: A Craftsman's Guide to Software Structure",
                    CantidadVenta = 1,
                    PrecioUnitario = 75.50m,
                    Descuento = 15.10m,
                    Subtotal = 60.40m,
                    PromocionAplicadaId = 1
                }
            }
        },
        new()
        {
            Id = 3,
            NumeroComprobante = "B001-00000103",
            FechaVenta = DateTime.UtcNow.AddDays(-1),
            Cliente = "user",
            UsuarioCreacion = "user",
            SubtotalVenta = 49.00m,
            TotalDescuento = 0.00m,
            Igv = 8.82m,
            TotalVenta = 57.82m,
            Detalles = new List<DetalleVentaDto>
            {
                new()
                {
                    Id = 4,
                    LibroId = 4,
                    LibroTitulo = "Diseño de Bases de Datos Relacionales y SQL Avanzado",
                    CantidadVenta = 1,
                    PrecioUnitario = 49.00m,
                    Descuento = 0.00m,
                    Subtotal = 49.00m
                }
            }
        },
        new()
        {
            Id = 4,
            NumeroComprobante = "B001-00000104",
            FechaVenta = DateTime.UtcNow.AddHours(-4),
            Cliente = "admin",
            UsuarioCreacion = "admin",
            SubtotalVenta = 95.00m,
            TotalDescuento = 19.00m,
            Igv = 13.68m,
            TotalVenta = 89.68m,
            Detalles = new List<DetalleVentaDto>
            {
                new()
                {
                    Id = 5,
                    LibroId = 5,
                    LibroTitulo = "Desarrollo Web Full-Stack con Blazor y Spring Boot",
                    CantidadVenta = 1,
                    PrecioUnitario = 95.00m,
                    Descuento = 19.00m,
                    Subtotal = 76.00m,
                    PromocionAplicadaId = 1
                }
            }
        }
    };

    public VentaService(HttpClient http, TokenService tokenService, BackendStatusService backendStatus)
    {
        _http = http;
        _tokenService = tokenService;
        _backendStatus = backendStatus;
    }

    private void SetAuthorizationHeader()
    {
        if (!string.IsNullOrWhiteSpace(_tokenService.Token))
        {
            _http.DefaultRequestHeaders.Authorization =
                new AuthenticationHeaderValue("Bearer", _tokenService.Token);
        }
        else
        {
            _http.DefaultRequestHeaders.Authorization = null;
        }
    }

    public async Task<List<VentaDto>> GetAll()
    {
        if (_backendStatus.ModoMemoriaRAM)
        {
            return _ventasLocales.OrderByDescending(v => v.Id).ToList();
        }

        SetAuthorizationHeader();
        try
        {
            var result = await _http.GetFromJsonAsync<List<VentaDto>>("api/ventas");
            if (result != null && result.Count > 0)
            {
                foreach (var v in result)
                {
                    if (!_ventasLocales.Any(x => x.Id == v.Id))
                    {
                        _ventasLocales.Add(v);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), consultando registro local.");
        }

        return _ventasLocales.OrderByDescending(v => v.Id).ToList();
    }

    public async Task<bool> Registrar(VentaDto venta)
    {
        venta.FechaVenta = venta.FechaVenta == default ? DateTime.UtcNow : venta.FechaVenta;
        venta.FechaCreacion = DateTime.UtcNow;
        venta.UsuarioCreacion ??= _tokenService.CurrentUser?.Login ?? (!string.IsNullOrWhiteSpace(venta.Cliente) ? venta.Cliente : "Cliente");

        if (string.IsNullOrWhiteSpace(venta.NumeroComprobante))
        {
            var correlativo = (_ventasLocales.Count + 1).ToString("D8");
            venta.NumeroComprobante = $"B001-{correlativo}";
        }

        foreach (var det in venta.Detalles)
        {
            if (det.Subtotal <= 0)
            {
                det.Subtotal = det.CantidadVenta * det.PrecioUnitario - det.Descuento;
            }
        }

        if (venta.TotalVenta <= 0)
        {
            venta.TotalVenta = venta.Detalles.Sum(d => d.Subtotal);
        }

        if (venta.SubtotalVenta <= 0)
        {
            venta.SubtotalVenta = Math.Round(venta.TotalVenta / 1.18m, 2);
            venta.Igv = venta.TotalVenta - venta.SubtotalVenta;
        }

        if (_backendStatus.ModoMemoriaRAM)
        {
            venta.Id = (_ventasLocales.Count > 0 ? _ventasLocales.Max(v => v.Id ?? 0) : 0) + 1;
            _ventasLocales.Add(venta);
            return true;
        }

        SetAuthorizationHeader();
        try
        {
            var response = await _http.PostAsJsonAsync("api/ventas", venta);
            if (response.IsSuccessStatusCode)
            {
                var guardada = await response.Content.ReadFromJsonAsync<VentaDto>();
                if (guardada != null)
                {
                    _ventasLocales.Add(guardada);
                    return true;
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), registrando en base local.");
        }

        venta.Id = (_ventasLocales.Count > 0 ? _ventasLocales.Max(v => v.Id ?? 0) : 0) + 1;
        _ventasLocales.Add(venta);
        return true;
    }

    public Task<bool> RegistrarVentaAsync(VentaDto venta) => Registrar(venta);
}
