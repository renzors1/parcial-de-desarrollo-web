using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace BlazorApp1.Components.Services;

public class LicenciaDigitalService
{
    private readonly HttpClient _http;
    private readonly TokenService _tokenService;
    private readonly BackendStatusService _backendStatus;

    private static readonly List<LicenciaDigitalDto> _licenciasLocales = new()
    {
        // 1. Licencia Vendida para carlos.mendoza
        new()
        {
            Id = 1,
            CodigoLicencia = "LIC-2026-NET10-CM01",
            EnlaceDescarga = "https://visor.bookstore.com/read/1?token=carlos_net10",
            FechaAsignacion = DateTime.UtcNow.AddDays(-3),
            Estado = "VENDIDA",
            UsuarioCreacion = "carlos.mendoza",
            FechaCreacion = DateTime.UtcNow.AddDays(-15),
            Libro = new LibroDto
            {
                Id = 1,
                Titulo = "C# 13 and .NET 10 – Modern Cross-Platform Development",
                Isbn = "978-1835081983",
                Precio = 89.90m,
                PortadaUrl = "/covers/cover-csharp13.svg",
                Categoriases = new() { new CategoriaDto { NombreCategoria = "Programación" } }
            }
        },
        // 2. Licencia Vendida para carlos.mendoza
        new()
        {
            Id = 2,
            CodigoLicencia = "LIC-2026-GOLNG-CM02",
            EnlaceDescarga = "https://visor.bookstore.com/read/3?token=carlos_go",
            FechaAsignacion = DateTime.UtcNow.AddDays(-3),
            Estado = "VENDIDA",
            UsuarioCreacion = "carlos.mendoza",
            FechaCreacion = DateTime.UtcNow.AddDays(-15),
            Libro = new LibroDto
            {
                Id = 3,
                Titulo = "Cloud Native Go: Building Web Applications and Microservices",
                Isbn = "978-1492076339",
                Precio = 59.90m,
                PortadaUrl = "/covers/cover-cloud-go.svg",
                Categoriases = new() { new CategoriaDto { NombreCategoria = "Cloud Computing" } }
            }
        },
        // 3. Licencia Vendida para ana.torres
        new()
        {
            Id = 3,
            CodigoLicencia = "LIC-2026-ARCH-AT01",
            EnlaceDescarga = "https://visor.bookstore.com/read/2?token=ana_cleanarch",
            FechaAsignacion = DateTime.UtcNow.AddDays(-2),
            Estado = "VENDIDA",
            UsuarioCreacion = "ana.torres",
            FechaCreacion = DateTime.UtcNow.AddDays(-10),
            Libro = new LibroDto
            {
                Id = 2,
                Titulo = "Clean Architecture: A Craftsman's Guide to Software Structure",
                Isbn = "978-0134494166",
                Precio = 75.50m,
                PortadaUrl = "/covers/cover-clean-arch.svg",
                Categoriases = new() { new CategoriaDto { NombreCategoria = "Arquitectura" } }
            }
        },
        // 4. Licencia Vendida para usuario cliente demo
        new()
        {
            Id = 4,
            CodigoLicencia = "LIC-2026-SQL-USR01",
            EnlaceDescarga = "https://visor.bookstore.com/read/4?token=user_sql",
            FechaAsignacion = DateTime.UtcNow.AddDays(-1),
            Estado = "VENDIDA",
            UsuarioCreacion = "user",
            FechaCreacion = DateTime.UtcNow.AddDays(-5),
            Libro = new LibroDto
            {
                Id = 4,
                Titulo = "Diseño de Bases de Datos Relacionales y SQL Avanzado",
                Isbn = "978-8441541234",
                Precio = 49.00m,
                PortadaUrl = "/covers/cover-sql-db.svg",
                Categoriases = new() { new CategoriaDto { NombreCategoria = "Base de Datos" } }
            }
        },
        // 5. Licencia Vendida para el administrador
        new()
        {
            Id = 5,
            CodigoLicencia = "LIC-2026-BLAZOR-ADM",
            EnlaceDescarga = "https://visor.bookstore.com/read/5?token=admin_blazor",
            FechaAsignacion = DateTime.UtcNow.AddHours(-4),
            Estado = "VENDIDA",
            UsuarioCreacion = "admin",
            FechaCreacion = DateTime.UtcNow.AddDays(-2),
            Libro = new LibroDto
            {
                Id = 5,
                Titulo = "Desarrollo Web Full-Stack con Blazor y Spring Boot",
                Isbn = "978-1617298578",
                Precio = 95.00m,
                PortadaUrl = "/covers/cover-blazor-spring.svg",
                Categoriases = new() { new CategoriaDto { NombreCategoria = "Desarrollo Web" } }
            }
        },
        // 6. Licencias DISPONIBLES en inventario editorial
        new()
        {
            Id = 6,
            CodigoLicencia = "STOCK-PACKT-NET10-001",
            EnlaceDescarga = "https://visor.bookstore.com/read/1",
            FechaAsignacion = null,
            Estado = "DISPONIBLE",
            UsuarioCreacion = "admin",
            FechaCreacion = DateTime.UtcNow.AddDays(-1),
            Libro = new LibroDto
            {
                Id = 1,
                Titulo = "C# 13 and .NET 10 – Modern Cross-Platform Development",
                Isbn = "978-1835081983",
                Precio = 89.90m,
                PortadaUrl = "/covers/cover-csharp13.svg",
                Categoriases = new() { new CategoriaDto { NombreCategoria = "Programación" } }
            }
        },
        new()
        {
            Id = 7,
            CodigoLicencia = "STOCK-OREILLY-ARCH-002",
            EnlaceDescarga = "https://visor.bookstore.com/read/2",
            FechaAsignacion = null,
            Estado = "DISPONIBLE",
            UsuarioCreacion = "admin",
            FechaCreacion = DateTime.UtcNow.AddDays(-1),
            Libro = new LibroDto
            {
                Id = 2,
                Titulo = "Clean Architecture: A Craftsman's Guide to Software Structure",
                Isbn = "978-0134494166",
                Precio = 75.50m,
                PortadaUrl = "/covers/cover-clean-arch.svg",
                Categoriases = new() { new CategoriaDto { NombreCategoria = "Arquitectura" } }
            }
        },
        new()
        {
            Id = 8,
            CodigoLicencia = "STOCK-MANNING-MICRO-003",
            EnlaceDescarga = "https://visor.bookstore.com/read/6",
            FechaAsignacion = null,
            Estado = "DISPONIBLE",
            UsuarioCreacion = "admin",
            FechaCreacion = DateTime.UtcNow.AddDays(-1),
            Libro = new LibroDto
            {
                Id = 6,
                Titulo = "Patrones de Microservicios y Sistemas Distribuidos",
                Isbn = "978-1617294549",
                Precio = 82.50m,
                PortadaUrl = "/covers/cover-microservices.svg",
                Categoriases = new() { new CategoriaDto { NombreCategoria = "Arquitectura" } }
            }
        }
    };

    public LicenciaDigitalService(HttpClient http, TokenService tokenService, BackendStatusService backendStatus)
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

    public async Task<List<LicenciaDigitalDto>> GetAll()
    {
        if (_backendStatus.ModoMemoriaRAM)
        {
            return _licenciasLocales.OrderByDescending(l => l.Id).ToList();
        }

        SetAuthorizationHeader();
        try
        {
            var result = await _http.GetFromJsonAsync<List<LicenciaDigitalDto>>("api/licencia-digitals");
            if (result != null && result.Count > 0)
            {
                foreach (var lic in result)
                {
                    if (!_licenciasLocales.Any(l => l.Id == lic.Id))
                    {
                        _licenciasLocales.Add(lic);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), consultando inventario local.");
        }

        return _licenciasLocales.OrderByDescending(l => l.Id).ToList();
    }

    public async Task<List<LicenciaDigitalDto>> GetMisLicencias(string? cliente = null)
    {
        var todas = await GetAll();
        var usuarioActual = !string.IsNullOrWhiteSpace(cliente)
            ? cliente.Trim()
            : _tokenService.CurrentUser?.Login ?? _tokenService.CurrentUser?.FirstName ?? _tokenService.CurrentUser?.Email;

        if (string.IsNullOrWhiteSpace(usuarioActual))
        {
            return new List<LicenciaDigitalDto>();
        }

        var delUsuario = todas.Where(l =>
            l.Estado == "VENDIDA" &&
            !string.IsNullOrWhiteSpace(l.UsuarioCreacion) &&
            (string.Equals(l.UsuarioCreacion, usuarioActual, StringComparison.OrdinalIgnoreCase) ||
             l.UsuarioCreacion.Contains(usuarioActual, StringComparison.OrdinalIgnoreCase) ||
             usuarioActual.Contains(l.UsuarioCreacion, StringComparison.OrdinalIgnoreCase))
        ).ToList();

        return delUsuario;
    }

    public async Task<bool> Crear(LicenciaDigitalDto licencia)
    {
        licencia.FechaCreacion ??= DateTime.UtcNow;
        licencia.UsuarioCreacion ??= _tokenService.CurrentUser?.Login ?? "admin";
        licencia.Id = (_licenciasLocales.Count > 0 ? _licenciasLocales.Max(l => l.Id ?? 0) : 0) + 1;
        _licenciasLocales.Add(licencia);

        if (_backendStatus.ModoMemoriaRAM)
        {
            return true;
        }

        SetAuthorizationHeader();
        try
        {
            var response = await _http.PostAsJsonAsync("api/licencia-digitals", licencia);
            if (response.IsSuccessStatusCode)
            {
                var creada = await response.Content.ReadFromJsonAsync<LicenciaDigitalDto>();
                if (creada != null)
                {
                    var idx = _licenciasLocales.FindIndex(l => l.Id == licencia.Id);
                    if (idx >= 0) _licenciasLocales[idx] = creada;
                    return true;
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), guardando en inventario local.");
        }

        return true;
    }

    public async Task<bool> Actualizar(LicenciaDigitalDto licencia)
    {
        if (!licencia.Id.HasValue) return false;
        var localIdx = _licenciasLocales.FindIndex(l => l.Id == licencia.Id.Value);
        if (localIdx >= 0) _licenciasLocales[localIdx] = licencia;

        if (_backendStatus.ModoMemoriaRAM)
        {
            return true;
        }

        SetAuthorizationHeader();
        try
        {
            var response = await _http.PutAsJsonAsync($"api/licencia-digitals/{licencia.Id.Value}", licencia);
            if (response.IsSuccessStatusCode) return true;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), actualizando inventario local.");
        }

        return true;
    }

    public async Task<bool> Eliminar(long? id)
    {
        if (!id.HasValue) return false;
        _licenciasLocales.RemoveAll(l => l.Id == id.Value);

        if (_backendStatus.ModoMemoriaRAM)
        {
            return true;
        }

        SetAuthorizationHeader();
        try
        {
            var response = await _http.DeleteAsync($"api/licencia-digitals/{id.Value}");
            if (response.IsSuccessStatusCode) return true;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), eliminando en inventario local.");
        }

        return true;
    }

    public async Task<List<LicenciaDigitalDto>> GenerarLicenciasParaVenta(List<CarritoItem> items, string cliente)
    {
        var resultado = new List<LicenciaDigitalDto>();
        var todas = await GetAll();
        foreach (var item in items)
        {
            var libro = item.Libro;
            var cantidad = Math.Max(1, item.Cantidad);

            for (int i = 0; i < cantidad; i++)
            {
                var disponible = todas.FirstOrDefault(l => l.Libro?.Id == libro.Id && l.Estado == "DISPONIBLE");
                if (disponible != null)
                {
                    disponible.Estado = "VENDIDA";
                    disponible.FechaAsignacion = DateTime.UtcNow;
                    disponible.UsuarioCreacion = cliente;
                    await Actualizar(disponible);
                    resultado.Add(disponible);
                }
                else
                {
                    var codigo = $"LIC-{DateTime.UtcNow.Year}-{Guid.NewGuid().ToString("N")[..8].ToUpper()}";
                    var nueva = new LicenciaDigitalDto
                    {
                        CodigoLicencia = codigo,
                        EnlaceDescarga = $"https://visor.bookstore.com/read/{libro.Id}?token={Guid.NewGuid().ToString("N")[..12]}",
                        FechaAsignacion = DateTime.UtcNow,
                        Estado = "VENDIDA",
                        UsuarioCreacion = cliente,
                        FechaCreacion = DateTime.UtcNow,
                        Libro = libro
                    };
                    await Crear(nueva);
                    resultado.Add(nueva);
                }
            }
        }
        return resultado;
    }

    public async Task<List<LicenciaDigitalDto>> GenerarLicenciasParaVenta(List<LibroDto> libros, string cliente)
    {
        var items = libros.Select(l => new CarritoItem { Libro = l, Cantidad = 1 }).ToList();
        return await GenerarLicenciasParaVenta(items, cliente);
    }
}
