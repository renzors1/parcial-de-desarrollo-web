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
            LibroId = 1,
            Puntuacion = 5,
            Comentario = "Excelente experiencia de lectura digital. El visor en línea es rápido y claro.",
            FechaCalificacion = DateTime.UtcNow.AddDays(-3),
            Activo = true,
            UsuarioCreacion = "carlos.mendoza",
            FechaCreacion = DateTime.UtcNow.AddDays(-3)
        },
        new()
        {
            Id = 2,
            LibroId = 2,
            Puntuacion = 5,
            Comentario = "Obra indispensable para arquitectura de software. La clave DRM se activó al instante.",
            FechaCalificacion = DateTime.UtcNow.AddDays(-2),
            Activo = true,
            UsuarioCreacion = "ana.torres",
            FechaCreacion = DateTime.UtcNow.AddDays(-2)
        },
        new()
        {
            Id = 3,
            LibroId = 3,
            Puntuacion = 4,
            Comentario = "Gran contenido sobre desarrollo cloud native y microservicios modernos.",
            FechaCalificacion = DateTime.UtcNow.AddDays(-1),
            Activo = true,
            UsuarioCreacion = "user",
            FechaCreacion = DateTime.UtcNow.AddDays(-1)
        }
    };

    public CalificacionService(HttpClient http, TokenService tokenService, BackendStatusService backendStatus)
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

    public async Task<List<CalificacionUsuarioDto>> GetAll()
    {
        if (_backendStatus.ModoMemoriaRAM)
        {
            return _calificacionesLocales.OrderByDescending(c => c.Id).ToList();
        }

        SetAuthorizationHeader();
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
        SetAuthorizationHeader();
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

    public async Task<bool> RegistrarOpinion(int puntuacion, string comentario, string? cliente = null, long? libroId = null)
    {
        var dto = new CalificacionUsuarioDto
        {
            LibroId = libroId,
            Puntuacion = puntuacion,
            Comentario = comentario,
            FechaCalificacion = DateTime.UtcNow,
            Activo = true,
            UsuarioCreacion = cliente ?? _tokenService.CurrentUser?.Login ?? "cliente"
        };

        return await EnviarCalificacion(dto);
    }
}
