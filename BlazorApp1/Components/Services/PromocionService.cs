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
            EsCupon = true,
            CodigoCupon = "PROMO20",
            TipoDescuento = "PORCENTAJE",
            PorcentajeDescuento = 20,
            DescuentoFijo = 0m,
            MontoMinimoCompra = 30m,
            TopeDescuento = 50m,
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
            EsCupon = false,
            CodigoCupon = "FLASH35",
            TipoDescuento = "PORCENTAJE",
            PorcentajeDescuento = 35,
            DescuentoFijo = 0m,
            MontoMinimoCompra = 50m,
            TopeDescuento = 70m,
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
            Descripcion = "S/ 15 de descuento fijo en compras superiores a S/ 40.",
            EsCupon = false,
            CodigoCupon = "LECTURA15",
            TipoDescuento = "MONTO_FIJO",
            PorcentajeDescuento = 0,
            DescuentoFijo = 15m,
            MontoMinimoCompra = 40m,
            TopeDescuento = 15m,
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
                    var existente = _promocionesLocales.FirstOrDefault(x => x.Id == p.Id);
                    if (existente == null)
                    {
                        _promocionesLocales.Add(p);
                    }
                    else
                    {
                        // Sincronizar campos manteniendo valores enriquecidos locales si la API JHipster viene básica
                        existente.Nombre = p.Nombre;
                        existente.Descripcion = p.Descripcion;
                        existente.PorcentajeDescuento = p.PorcentajeDescuento;
                        existente.CantidadDefinida = p.CantidadDefinida;
                        existente.CantidadDisponible = p.CantidadDisponible;
                        existente.FechaInicio = p.FechaInicio;
                        existente.FechaFin = p.FechaFin;
                        existente.Activo = p.Activo;
                        existente.Libroses = p.Libroses;
                        if (!string.IsNullOrWhiteSpace(p.CodigoCupon)) existente.CodigoCupon = p.CodigoCupon;
                        if (!string.IsNullOrWhiteSpace(p.TipoDescuento)) existente.TipoDescuento = p.TipoDescuento;
                        if (p.DescuentoFijo > 0) existente.DescuentoFijo = p.DescuentoFijo;
                        if (p.MontoMinimoCompra.HasValue) existente.MontoMinimoCompra = p.MontoMinimoCompra;
                        if (p.TopeDescuento.HasValue) existente.TopeDescuento = p.TopeDescuento;
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
                    // Preservar propiedades extendidas si el backend JHipster no las persistió
                    creada.CodigoCupon = promocion.CodigoCupon;
                    creada.TipoDescuento = promocion.TipoDescuento;
                    creada.DescuentoFijo = promocion.DescuentoFijo;
                    creada.MontoMinimoCompra = promocion.MontoMinimoCompra;
                    creada.TopeDescuento = promocion.TopeDescuento;

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
        if (cantidad <= 0) return false;
        var todas = await GetAll();
        var promo = todas.FirstOrDefault(p => p.Id == promocionId);
        if (promo != null && promo.CantidadDisponible >= cantidad)
        {
            promo.CantidadDisponible -= cantidad;
            return await Editar(promo);
        }
        return false;
    }

    public Task<bool> DescontarCupoAsync(long promocionId, int cantidad = 1) => DescontarCupo(promocionId, cantidad);

    public async Task<bool> RevertirCupo(long promocionId, int cantidad = 1)
    {
        if (cantidad <= 0) return false;
        var todas = await GetAll();
        var promo = todas.FirstOrDefault(p => p.Id == promocionId);
        if (promo != null)
        {
            promo.CantidadDisponible = Math.Min(promo.CantidadDefinida, promo.CantidadDisponible + cantidad);
            return await Editar(promo);
        }
        return false;
    }

    public Task<bool> RevertirCupoAsync(long promocionId, int cantidad = 1) => RevertirCupo(promocionId, cantidad);

    public async Task<bool> Clonar(long id)
    {
        var todas = await GetAll();
        var original = todas.FirstOrDefault(p => p.Id == id);
        if (original == null) return false;

        var copia = new PromocionDto
        {
            Nombre = $"{original.Nombre} (Copia)",
            Descripcion = original.Descripcion,
            CodigoCupon = GenerarCodigoUnico(),
            TipoDescuento = original.TipoDescuento,
            PorcentajeDescuento = original.PorcentajeDescuento,
            DescuentoFijo = original.DescuentoFijo,
            MontoMinimoCompra = original.MontoMinimoCompra,
            TopeDescuento = original.TopeDescuento,
            CantidadDefinida = original.CantidadDefinida,
            CantidadDisponible = original.CantidadDefinida,
            FechaInicio = DateTime.UtcNow,
            FechaFin = DateTime.UtcNow.AddMonths(1),
            Activo = true,
            Libroses = original.Libroses != null ? new List<LibroDto>(original.Libroses) : new List<LibroDto>()
        };

        return await Crear(copia);
    }

    public Task<bool> ClonarAsync(long id) => Clonar(id);

    public async Task<bool> ExtenderDias(long id, int dias = 7)
    {
        if (dias <= 0) dias = 7;
        var todas = await GetAll();
        var promo = todas.FirstOrDefault(p => p.Id == id);
        if (promo == null) return false;

        var ahoraUtc = DateTime.UtcNow;
        var baseFin = promo.FechaFin < ahoraUtc ? ahoraUtc : promo.FechaFin;
        promo.FechaFin = baseFin.AddDays(dias);
        promo.Activo = true;

        return await Editar(promo);
    }

    public Task<bool> ExtenderDiasAsync(long id, int dias = 7) => ExtenderDias(id, dias);

    public string GenerarCodigoUnico()
    {
        const string chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        string codigo;
        do
        {
            var randomPart = new string(Enumerable.Repeat(chars, 4).Select(s => s[Random.Shared.Next(s.Length)]).ToArray());
            codigo = $"PROMO-{randomPart}";
        } while (_promocionesLocales.Any(p => string.Equals(p.CodigoCupon, codigo, StringComparison.OrdinalIgnoreCase)));

        return codigo;
    }

    public async Task<(bool EsValido, string? Mensaje, PromocionDto? Promocion)> ValidarCuponAsync(
        string codigo, decimal subtotal, List<long> librosIds)
    {
        if (string.IsNullOrWhiteSpace(codigo))
        {
            return (false, "Por favor ingrese un código de cupón.", null);
        }

        var codigoLimpio = codigo.Trim();
        var todas = await GetAll();
        var promo = todas.FirstOrDefault(p =>
            string.Equals(p.CodigoCupon, codigoLimpio, StringComparison.OrdinalIgnoreCase) ||
            string.Equals(p.Nombre, codigoLimpio, StringComparison.OrdinalIgnoreCase));

        if (promo == null)
        {
            return (false, $"El cupón '{codigoLimpio}' no existe o no es válido.", null);
        }

        if (!promo.Activo)
        {
            return (false, $"El cupón '{promo.CodigoCupon}' se encuentra inactivo.", null);
        }

        if (promo.EsProgramada)
        {
            return (false, $"El cupón '{promo.CodigoCupon}' aún no está vigente. Inicia el {promo.FechaInicio:dd/MM/yyyy}.", null);
        }

        var ahora = DateTime.Now;
        var fin = promo.FechaFin.Kind == DateTimeKind.Utc ? promo.FechaFin.ToLocalTime() : promo.FechaFin;
        var finDelDia = fin.Date.AddDays(1).AddTicks(-1);
        if (ahora > finDelDia)
        {
            return (false, $"El cupón '{promo.CodigoCupon}' ha expirado el {promo.FechaFin:dd/MM/yyyy}.", null);
        }

        if (promo.CantidadDisponible <= 0)
        {
            return (false, $"Los cupones para '{promo.CodigoCupon}' se han agotado.", null);
        }

        if (promo.Libroses != null && promo.Libroses.Count > 0)
        {
            var tieneLibroAplicable = librosIds != null && librosIds.Any(id => promo.AplicaALibro(id));
            if (!tieneLibroAplicable)
            {
                return (false, $"El cupón '{promo.CodigoCupon}' no es aplicable a los libros seleccionados en tu carrito.", null);
            }
        }

        if (promo.MontoMinimoCompra.HasValue && promo.MontoMinimoCompra.Value > 0 && subtotal < promo.MontoMinimoCompra.Value)
        {
            return (false, $"El monto mínimo de compra para aplicar '{promo.CodigoCupon}' es de S/ {promo.MontoMinimoCompra.Value:N2}.", null);
        }

        return (true, $"¡Cupón '{promo.CodigoCupon}' aplicado con éxito!", promo);
    }

    public Task<(bool EsValido, string? Mensaje, PromocionDto? Promocion)> ValidarCupon(
        string codigo, decimal subtotal, List<long> librosIds) => ValidarCuponAsync(codigo, subtotal, librosIds);
}
