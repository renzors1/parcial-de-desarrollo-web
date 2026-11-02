using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace BlazorApp1.Components.Services;

public class PromocionService
{
    private readonly HttpClient _http;
    private readonly TokenService _tokenService;
    private readonly BackendStatusService _backendStatus;

    private static readonly List<PromocionDto> _promocionesLocales = new()
    {
        new()
        {
            Id = 1,
            Nombre = "PROMO20 - Descuento Bienvenida",
            Descripcion = "20% de descuento en todos los libros digitales del catálogo.",
            PorcentajeDescuento = 20,
            CantidadDefinida = 100,
            CantidadDisponible = 78,
            FechaInicio = DateTime.UtcNow.AddDays(-10),
            FechaFin = DateTime.UtcNow.AddMonths(2),
            Activo = true
        },
        new()
        {
            Id = 2,
            Nombre = "FLASH35 - Cyber Week Tech",
            Descripcion = "35% de descuento en libros de tecnología y desarrollo de software.",
            PorcentajeDescuento = 35,
            CantidadDefinida = 50,
            CantidadDisponible = 21,
            FechaInicio = DateTime.UtcNow.AddDays(-5),
            FechaFin = DateTime.UtcNow.AddMonths(1),
            Activo = true
        },
        new()
        {
            Id = 3,
            Nombre = "LECTURA15 - Especial Editoriales",
            Descripcion = "15% de descuento en novedades editoriales.",
            PorcentajeDescuento = 15,
            CantidadDefinida = 200,
            CantidadDisponible = 154,
            FechaInicio = DateTime.UtcNow.AddDays(-2),
            FechaFin = DateTime.UtcNow.AddMonths(3),
            Activo = true
        }
    };

    public PromocionService(HttpClient http, TokenService tokenService, BackendStatusService backendStatus)
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

    public async Task<List<PromocionDto>> GetAll()
    {
        if (_backendStatus.ModoMemoriaRAM)
        {
            return _promocionesLocales.OrderByDescending(p => p.Activo).ThenBy(p => p.Nombre).ToList();
        }

        SetAuthorizationHeader();
        try
        {
            var result = await _http.GetFromJsonAsync<List<PromocionDto>>("api/promocions");
            if (result != null && result.Count > 0)
            {
                foreach (var p in result)
                {
                    if (!_promocionesLocales.Any(x => x.Id == p.Id))
                    {
                        _promocionesLocales.Add(p);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al obtener promociones desde API: {ex.Message}");
        }

        return _promocionesLocales.OrderByDescending(p => p.Activo).ThenBy(p => p.Nombre).ToList();
    }

    public async Task<List<PromocionDto>> GetActivas()
    {
        var todas = await GetAll();
        return todas.Where(p => p.EsVigente).ToList();
    }

    public async Task<bool> Crear(PromocionDto promocion)
    {
        SetAuthorizationHeader();
        try
        {
            var response = await _http.PostAsJsonAsync("api/promocions", promocion);
            if (response.IsSuccessStatusCode)
            {
                var creada = await response.Content.ReadFromJsonAsync<PromocionDto>();
                if (creada != null)
                {
                    _promocionesLocales.Add(creada);
                    return true;
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al crear promoción en API: {ex.Message}");
        }

        promocion.Id = (_promocionesLocales.Count > 0 ? _promocionesLocales.Max(p => p.Id ?? 0) : 0) + 1;
        promocion.CantidadDisponible = promocion.CantidadDefinida;
        _promocionesLocales.Add(promocion);
        return true;
    }

    public async Task<bool> Editar(PromocionDto promocion)
    {
        SetAuthorizationHeader();
        if (promocion.Id.HasValue)
        {
            try
            {
                var response = await _http.PutAsJsonAsync($"api/promocions/{promocion.Id.Value}", promocion);
                if (response.IsSuccessStatusCode)
                {
                    var index = _promocionesLocales.FindIndex(p => p.Id == promocion.Id.Value);
                    if (index >= 0) _promocionesLocales[index] = promocion;
                    return true;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al editar promoción en API: {ex.Message}");
            }

            var localIdx = _promocionesLocales.FindIndex(p => p.Id == promocion.Id.Value);
            if (localIdx >= 0)
            {
                _promocionesLocales[localIdx] = promocion;
                return true;
            }
        }
        return false;
    }

    public async Task<bool> Eliminar(long? id)
    {
        if (!id.HasValue) return false;
        SetAuthorizationHeader();
        try
        {
            var response = await _http.DeleteAsync($"api/promocions/{id.Value}");
            if (response.IsSuccessStatusCode)
            {
                _promocionesLocales.RemoveAll(p => p.Id == id.Value);
                return true;
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al eliminar promoción en API: {ex.Message}");
        }

        _promocionesLocales.RemoveAll(p => p.Id == id.Value);
        return true;
    }

    public async Task<bool> DescontarCupo(long promocionId, int cantidad = 1)
    {
        var todas = await GetAll();
        var promo = todas.FirstOrDefault(p => p.Id == promocionId);
        if (promo != null && promo.CantidadDisponible > 0)
        {
            promo.CantidadDisponible = Math.Max(0, promo.CantidadDisponible - cantidad);
            return await Editar(promo);
        }
        return false;
    }
}
