using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace BlazorApp1.Components.Services;

public class CalificacionService
{
    private readonly HttpClient _http;
    private readonly TokenService _tokenService;
    private readonly BackendStatusService _backendStatus;

    private static readonly List<CalificacionUsuarioDto> _calificacionesLocales = new()
    {
        new()
        {
            Id = 1,
            Puntuacion = 5,
            Comentario = "Cliente puntual y sin incidencias en sus compras digitales.",
            FechaCalificacion = DateTime.UtcNow.AddDays(-3),
            Activo = true,
            UsuarioCreacion = "admin",
            ClienteEvaluado = new UserAccountDto { Login = "carlos.mendoza", FirstName = "Carlos", LastName = "Mendoza" },
            FechaCreacion = DateTime.UtcNow.AddDays(-3)
        },
        new()
        {
            Id = 2,
            Puntuacion = 5,
            Comentario = "Compra verificada correctamente y activacion inmediata de licencia.",
            FechaCalificacion = DateTime.UtcNow.AddDays(-2),
            Activo = true,
            UsuarioCreacion = "admin",
            ClienteEvaluado = new UserAccountDto { Login = "ana.torres", FirstName = "Ana", LastName = "Torres" },
            FechaCreacion = DateTime.UtcNow.AddDays(-2)
        },
        new()
        {
            Id = 3,
            Puntuacion = 4,
            Comentario = "Buen historial de compras en la plataforma.",
            FechaCalificacion = DateTime.UtcNow.AddDays(-1),
            Activo = true,
            UsuarioCreacion = "admin",
            ClienteEvaluado = new UserAccountDto { Login = "user", FirstName = "Usuario", LastName = "Cliente Demo" },
            FechaCreacion = DateTime.UtcNow.AddDays(-1)
        }
    };

    public CalificacionService(HttpClient http, TokenService tokenService, BackendStatusService backendStatus)
    {
        _http = http;
        _tokenService = tokenService;
        _backendStatus = backendStatus;
    }

    public async Task<List<CalificacionUsuarioDto>> GetAll()
    {
        if (_backendStatus.ModoMemoriaRAM)
        {
            return _calificacionesLocales.OrderByDescending(c => c.Id).ToList();
        }

        _tokenService.ApplyBearerToken(_http);
        try
        {
            var result = await _http.GetFromJsonAsync<List<CalificacionUsuarioDto>>("api/calificacion-usuarios");
            if (result != null && result.Count > 0)
            {
                foreach (var item in result)
                {
                    if (!_calificacionesLocales.Any(c => c.Id == item.Id))
                    {
                        _calificacionesLocales.Add(item);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), consultando reseñas locales.");
        }

        return _calificacionesLocales.OrderByDescending(c => c.FechaCalificacion).ToList();
    }

    public async Task<bool> EnviarCalificacion(CalificacionUsuarioDto calificacion)
    {
        _tokenService.ApplyBearerToken(_http);
        calificacion.Puntuacion = Math.Clamp(calificacion.Puntuacion, 1, 5);
        calificacion.FechaCalificacion = DateTime.UtcNow;
        calificacion.FechaCreacion ??= DateTime.UtcNow;
        calificacion.Activo = true;
        calificacion.UsuarioCreacion ??= _tokenService.CurrentUser?.Login ?? "cliente";

        try
        {
            var response = await _http.PostAsJsonAsync("api/calificacion-usuarios", calificacion);
            if (response.IsSuccessStatusCode)
            {
                var creada = await response.Content.ReadFromJsonAsync<CalificacionUsuarioDto>();
                if (creada != null)
                {
                    _calificacionesLocales.Add(creada);
                    return true;
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), registrando reseña localmente.");
        }

        calificacion.Id = (_calificacionesLocales.Count > 0 ? _calificacionesLocales.Max(c => c.Id ?? 0) : 0) + 1;
        _calificacionesLocales.Add(calificacion);
        return true;
    }

    public static (int Puntuacion, string Nivel) CalcularPuntuacionPorMetricas(int compras, decimal totalGastado)
    {
        if (compras >= 5 || totalGastado >= 250m) return (5, "Cliente VIP");
        if (compras >= 3 || totalGastado >= 120m) return (4, "Cliente Frecuente");
        if (compras >= 2 || totalGastado >= 60m) return (3, "Cliente Activo");
        if (compras == 1) return (2, "Cliente Nuevo");
        return (1, "Sin Actividad");
    }

    public async Task<int> RecalcularPorVentasAsync(List<VentaDto> ventas, string? loginObjetivo = null, string? nombreObjetivo = null)
    {
        var grupos = ventas
            .Where(v => !string.IsNullOrWhiteSpace(v.Cliente ?? v.UsuarioCreacion))
            .GroupBy(v => (v.Cliente ?? v.UsuarioCreacion ?? "cliente").Trim(), StringComparer.OrdinalIgnoreCase)
            .ToList();

        if (!string.IsNullOrWhiteSpace(loginObjetivo))
        {
            grupos = grupos.Where(g => g.Key.Equals(loginObjetivo.Trim(), StringComparison.OrdinalIgnoreCase)).ToList();
        }

        int actualizadas = 0;
        foreach (var grupo in grupos)
        {
            var compras = grupo.Count();
            var totalGastado = grupo.Sum(v => v.TotalVenta);
            var (puntuacion, nivel) = CalcularPuntuacionPorMetricas(compras, totalGastado);
            var nombre = nombreObjetivo
                ?? grupo.Select(v => v.Cliente).FirstOrDefault(n => !string.IsNullOrWhiteSpace(n))
                ?? grupo.Key;

            var comentario = $"Cálculo automático ({nivel}): {compras} compra(s) registrada(s) por S/ {totalGastado:N2}.";

            var existente = _calificacionesLocales.FirstOrDefault(c =>
                string.Equals(c.ClienteEvaluado?.Login ?? c.UsuarioCreacion, grupo.Key, StringComparison.OrdinalIgnoreCase));

            if (existente != null)
            {
                existente.Puntuacion = puntuacion;
                existente.Comentario = comentario;
                existente.FechaCalificacion = DateTime.UtcNow;
                actualizadas++;
            }
            else
            {
                var dto = new CalificacionUsuarioDto
                {
                    Puntuacion = puntuacion,
                    Comentario = comentario,
                    FechaCalificacion = DateTime.UtcNow,
                    UsuarioCreacion = grupo.Key,
                    ClienteEvaluado = new UserAccountDto { FirstName = nombre, Login = grupo.Key },
                    EvaluadorTienda = new UserAccountDto { FirstName = "Sistema Automático", Login = "admin" }
                };
                if (await EnviarCalificacion(dto))
                {
                    actualizadas++;
                }
            }
        }

        return actualizadas;
    }

    public async Task<bool> RegistrarOpinion(int puntuacion, string comentario, string? cliente = null)
    {
        var dto = new CalificacionUsuarioDto
        {
            Puntuacion = puntuacion,
            Comentario = comentario,
            FechaCalificacion = DateTime.UtcNow,
            Activo = true,
            UsuarioCreacion = _tokenService.CurrentUser?.Login ?? "admin",
            ClienteEvaluado = string.IsNullOrWhiteSpace(cliente) ? null : new UserAccountDto { Login = cliente, FirstName = cliente }
        };

        return await EnviarCalificacion(dto);
    }
}
