using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace BlazorApp1.Components.Services;

public class LibroService
{
    private readonly HttpClient _http;
    private readonly TokenService _tokenService;
    private readonly BackendStatusService _backendStatus;

    private static readonly List<LibroDto> _librosLocales = new()
    {
        new()
        {
            Id = 1,
            Isbn = "978-1835081983",
            Titulo = "C# 13 and .NET 10 – Modern Cross-Platform Development",
            Sinopsis = "Aprende C# moderno, Blazor interactivo y desarrollo multiplataforma empresarial con ejemplos prácticos y limpios.",
            Precio = 89.90m,
            Idioma = "Español",
            Activo = true,
            PortadaUrl = "https://covers.openlibrary.org/b/isbn/9781803237800-L.jpg",
            UrlContraportada = "https://covers.openlibrary.org/b/isbn/9781803237800-M.jpg",
            Editorial = new EditorialDto { Id = 2, NombreEditorial = "Packt Publishing", Pais = "Reino Unido" },
            Autoreses = new() { new AutorDto { Id = 1, Nombres = "Mark J.", Apellidos = "Price" } },
            Categoriases = new() { new CategoriaDto { Id = 1, NombreCategoria = "Programación" } }
        },
        new()
        {
            Id = 2,
            Isbn = "978-0134494166",
            Titulo = "Clean Architecture: A Craftsman's Guide to Software Structure",
            Sinopsis = "Principios universales de arquitectura de software, desacoplamiento, SOLID y separación de responsabilidades en capas.",
            Precio = 75.50m,
            Idioma = "Español",
            Activo = true,
            PortadaUrl = "https://covers.openlibrary.org/b/isbn/9780134494166-L.jpg",
            UrlContraportada = "https://covers.openlibrary.org/b/isbn/9780134494166-M.jpg",
            Editorial = new EditorialDto { Id = 1, NombreEditorial = "O'Reilly Media", Pais = "Estados Unidos" },
            Autoreses = new() { new AutorDto { Id = 2, Nombres = "Robert C.", Apellidos = "Martin" } },
            Categoriases = new() { new CategoriaDto { Id = 2, NombreCategoria = "Arquitectura" } }
        },
        new()
        {
            Id = 3,
            Isbn = "978-1492076339",
            Titulo = "Cloud Native Go: Building Web Applications and Microservices",
            Sinopsis = "Guía completa para construir microservicios resistentes, aplicaciones nativas de la nube y sistemas distribuidos.",
            Precio = 59.90m,
            Idioma = "Español",
            Activo = true,
            PortadaUrl = "https://covers.openlibrary.org/b/isbn/9781492076339-L.jpg",
            UrlContraportada = "https://covers.openlibrary.org/b/isbn/9781492076339-M.jpg",
            Editorial = new EditorialDto { Id = 1, NombreEditorial = "O'Reilly Media", Pais = "Estados Unidos" },
            Autoreses = new() { new AutorDto { Id = 3, Nombres = "Matthew", Apellidos = "Titmus" } },
            Categoriases = new() { new CategoriaDto { Id = 3, NombreCategoria = "Cloud Computing" } }
        },
        new()
        {
            Id = 4,
            Isbn = "978-8441541234",
            Titulo = "Diseño de Bases de Datos Relacionales y SQL Avanzado",
            Sinopsis = "Fundamentos y conceptos avanzados sobre normalización, optimización de índices, integridad referencial y transacciones ACID.",
            Precio = 49.00m,
            Idioma = "Español",
            Activo = true,
            PortadaUrl = "https://covers.openlibrary.org/b/isbn/9781492057611-L.jpg",
            UrlContraportada = "https://covers.openlibrary.org/b/isbn/9781492057611-M.jpg",
            Editorial = new EditorialDto { Id = 4, NombreEditorial = "Planeta Digital", Pais = "España" },
            Autoreses = new() { new AutorDto { Id = 4, Nombres = "C. J.", Apellidos = "Date" } },
            Categoriases = new() { new CategoriaDto { Id = 4, NombreCategoria = "Base de Datos" } }
        },
        new()
        {
            Id = 5,
            Isbn = "978-1617298578",
            Titulo = "Desarrollo Web Full-Stack con Blazor y Spring Boot",
            Sinopsis = "Arquitectura moderna cliente-servidor combinando la agilidad visual de Blazor con la robustez y seguridad de Spring Boot.",
            Precio = 95.00m,
            Idioma = "Español",
            Activo = true,
            PortadaUrl = "https://covers.openlibrary.org/b/isbn/9781617298578-L.jpg",
            UrlContraportada = "https://covers.openlibrary.org/b/isbn/9781617298578-M.jpg",
            Editorial = new EditorialDto { Id = 3, NombreEditorial = "Manning Publications", Pais = "Estados Unidos" },
            Autoreses = new() { new AutorDto { Id = 5, Nombres = "Martin", Apellidos = "Fowler" } },
            Categoriases = new() { new CategoriaDto { Id = 5, NombreCategoria = "Desarrollo Web" } }
        },
        new()
        {
            Id = 6,
            Isbn = "978-1617294549",
            Titulo = "Patrones de Microservicios y Sistemas Distribuidos",
            Sinopsis = "Soluciones probadas para descomponer monolitos, gestión de transacciones con Saga y persistencia políglota.",
            Precio = 82.50m,
            Idioma = "Español",
            Activo = true,
            PortadaUrl = "https://covers.openlibrary.org/b/isbn/9781617294549-L.jpg",
            UrlContraportada = "https://covers.openlibrary.org/b/isbn/9781617294549-M.jpg",
            Editorial = new EditorialDto { Id = 3, NombreEditorial = "Manning Publications", Pais = "Estados Unidos" },
            Autoreses = new() { new AutorDto { Id = 6, Nombres = "Chris", Apellidos = "Richardson" } },
            Categoriases = new() { new CategoriaDto { Id = 2, NombreCategoria = "Arquitectura" } }
        }
    };

    public LibroService(HttpClient http, TokenService tokenService, BackendStatusService backendStatus)
    {
        _http = http;
        _tokenService = tokenService;
        _backendStatus = backendStatus;
    }

    public async Task<List<LibroDto>> GetAll()
    {
        // Quitar registros invalidos
        _librosLocales.RemoveAll(l => l.Precio > 10000m || (l.Isbn != null && l.Isbn.Length > 25));

        if (_backendStatus.ModoMemoriaRAM)
        {
            return _librosLocales.Where(l => l.Activo).OrderByDescending(l => l.Id ?? 0).ToList();
        }

        _tokenService.ApplyBearerToken(_http);
        try
        {
            var result = await _http.GetFromJsonAsync<List<LibroDto>>("api/libros");
            if (result != null && result.Count > 0)
            {
                // Filtrar datos invalidos
                var librosApi = result.Where(l => l.Precio <= 10000m && (l.Isbn == null || l.Isbn.Length <= 25)).ToList();

                // Agregar libros locales que falten
                foreach (var local in _librosLocales.Where(l => l.Activo))
                {
                    bool yaExisteEnApi = librosApi.Any(a =>
                        (a.Id.HasValue && local.Id.HasValue && a.Id == local.Id) ||
                        (!string.IsNullOrWhiteSpace(a.Isbn) && !string.IsNullOrWhiteSpace(local.Isbn) && string.Equals(a.Isbn.Trim(), local.Isbn.Trim(), StringComparison.OrdinalIgnoreCase))
                    );

                    if (!yaExisteEnApi)
                    {
                        librosApi.Add(local);
                    }
                }

                // Actualizar lista local
                foreach (var apiBook in librosApi)
                {
                    var existenteLocal = _librosLocales.FirstOrDefault(l =>
                        (l.Id.HasValue && apiBook.Id.HasValue && l.Id == apiBook.Id) ||
                        (!string.IsNullOrWhiteSpace(l.Isbn) && !string.IsNullOrWhiteSpace(apiBook.Isbn) && string.Equals(l.Isbn.Trim(), apiBook.Isbn.Trim(), StringComparison.OrdinalIgnoreCase))
                    );

                    if (existenteLocal == null)
                    {
                        _librosLocales.Add(apiBook);
                    }
                }

                // Ordenar los mas nuevos primero
                return librosApi.OrderByDescending(l => l.Id ?? 0).ToList();
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), usando catálogo local.");
        }

        return _librosLocales.Where(l => l.Activo).OrderByDescending(l => l.Id ?? 0).ToList();
    }

    public Task<List<LibroDto>> GetAllAsync() => GetAll();

    public async Task<LibroDto?> GetById(long id)
    {
        if (_backendStatus.ModoMemoriaRAM)
        {
            return _librosLocales.FirstOrDefault(l => l.Id == id);
        }

        _tokenService.ApplyBearerToken(_http);
        try
        {
            var res = await _http.GetFromJsonAsync<LibroDto>($"api/libros/{id}");
            if (res != null) return res;
        }
        catch
        {
            // Fallback a repositorio local
        }

        return _librosLocales.FirstOrDefault(l => l.Id == id);
    }

    public async Task<bool> Create(LibroDto libro)
    {
        // Validar rango de precio y textos
        libro.Precio = Math.Clamp(libro.Precio, 0.50m, 9999.99m);
        if (!string.IsNullOrWhiteSpace(libro.Isbn) && libro.Isbn.Length > 20) libro.Isbn = libro.Isbn[..20];
        if (!string.IsNullOrWhiteSpace(libro.Titulo) && libro.Titulo.Length > 150) libro.Titulo = libro.Titulo[..150];

        // Poner apellido por defecto si falta
        if (libro.Autoreses != null)
        {
            foreach (var autor in libro.Autoreses)
            {
                if (string.IsNullOrWhiteSpace(autor.Apellidos))
                {
                    autor.Apellidos = "Autor";
                }
            }
        }

        if (_backendStatus.ModoMemoriaRAM)
        {
            libro.Id = (_librosLocales.Count > 0 ? _librosLocales.Max(l => l.Id ?? 0) : 0) + 1;
            _librosLocales.Add(libro);
            return true;
        }

        _tokenService.ApplyBearerToken(_http);
        try
        {
            var response = await _http.PostAsJsonAsync("api/libros", libro);
            if (response.IsSuccessStatusCode)
            {
                LibroDto? creado = null;
                try
                {
                    creado = await response.Content.ReadFromJsonAsync<LibroDto>();
                }
                catch
                {
                    // Si el cuerpo de respuesta es vacío
                }

                if (creado != null)
                {
                    libro.Id = creado.Id;
                    _librosLocales.RemoveAll(l => (creado.Id.HasValue && l.Id == creado.Id) || (!string.IsNullOrWhiteSpace(creado.Isbn) && string.Equals(l.Isbn, creado.Isbn, StringComparison.OrdinalIgnoreCase)));
                    _librosLocales.Add(creado);
                    return true;
                }
                else
                {
                    if (libro.Id == null || libro.Id == 0)
                    {
                        libro.Id = (_librosLocales.Count > 0 ? _librosLocales.Max(l => l.Id ?? 0) : 0) + 1;
                    }
                    _librosLocales.RemoveAll(l => (l.Id == libro.Id) || (!string.IsNullOrWhiteSpace(libro.Isbn) && string.Equals(l.Isbn, libro.Isbn, StringComparison.OrdinalIgnoreCase)));
                    _librosLocales.Add(libro);
                    return true;
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al sincronizar con el servidor ({ex.Message}), guardando localmente.");
        }

        // Guardar en memoria si falla el servidor
        if (libro.Id == null || libro.Id == 0)
        {
            libro.Id = (_librosLocales.Count > 0 ? _librosLocales.Max(l => l.Id ?? 0) : 0) + 1;
        }
        _librosLocales.RemoveAll(l => (l.Id == libro.Id) || (!string.IsNullOrWhiteSpace(libro.Isbn) && string.Equals(l.Isbn, libro.Isbn, StringComparison.OrdinalIgnoreCase)));
        _librosLocales.Add(libro);
        return true;
    }

    public async Task<bool> Update(LibroDto libro)
    {
        // Validar rango de precio y textos
        libro.Precio = Math.Clamp(libro.Precio, 0.50m, 9999.99m);
        if (!string.IsNullOrWhiteSpace(libro.Isbn) && libro.Isbn.Length > 20) libro.Isbn = libro.Isbn[..20];
        if (!string.IsNullOrWhiteSpace(libro.Titulo) && libro.Titulo.Length > 150) libro.Titulo = libro.Titulo[..150];

        // Poner apellido por defecto si falta
        if (libro.Autoreses != null)
        {
            foreach (var autor in libro.Autoreses)
            {
                if (string.IsNullOrWhiteSpace(autor.Apellidos))
                {
                    autor.Apellidos = "Autor";
                }
            }
        }

        if (_backendStatus.ModoMemoriaRAM)
        {
            var idxLocal = _librosLocales.FindIndex(l => l.Id == libro.Id);
            if (idxLocal >= 0) _librosLocales[idxLocal] = libro;
            else _librosLocales.Add(libro);
            return true;
        }

        _tokenService.ApplyBearerToken(_http);
        try
        {
            var response = await _http.PutAsJsonAsync($"api/libros/{libro.Id}", libro);
            if (response.IsSuccessStatusCode)
            {
                var idx = _librosLocales.FindIndex(l => l.Id == libro.Id);
                if (idx >= 0) _librosLocales[idx] = libro;
                else _librosLocales.Add(libro);
                return true;
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al sincronizar actualización ({ex.Message}), aplicando cambio local.");
        }

        var localIdx = _librosLocales.FindIndex(l => l.Id == libro.Id);
        if (localIdx >= 0) _librosLocales[localIdx] = libro;
        else _librosLocales.Add(libro);
        return true;
    }

    public async Task<bool> Delete(long? id)
    {
        if (!id.HasValue) return false;

        if (_backendStatus.ModoMemoriaRAM)
        {
            _librosLocales.RemoveAll(l => l.Id == id.Value);
            return true;
        }

        _tokenService.ApplyBearerToken(_http);
        try
        {
            var response = await _http.DeleteAsync($"api/libros/{id.Value}");
            if (response.IsSuccessStatusCode)
            {
                _librosLocales.RemoveAll(l => l.Id == id.Value);
                return true;
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al sincronizar eliminación ({ex.Message}), aplicando eliminación local.");
        }

        _librosLocales.RemoveAll(l => l.Id == id.Value);
        return true;
    }
}
