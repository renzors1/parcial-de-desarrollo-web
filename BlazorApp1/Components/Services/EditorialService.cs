using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace BlazorApp1.Components.Services;

public class EditorialService
{
    private readonly HttpClient _http;
    private readonly TokenService _tokenService;
    private readonly BackendStatusService _backendStatus;

    private static readonly List<EditorialDto> _editorialesLocales = new()
    {
        new() { Id = 1, NombreEditorial = "O'Reilly Media", Pais = "Estados Unidos", Activo = true },
        new() { Id = 2, NombreEditorial = "Packt Publishing", Pais = "Reino Unido", Activo = true },
        new() { Id = 3, NombreEditorial = "Manning Publications", Pais = "Estados Unidos", Activo = true },
        new() { Id = 4, NombreEditorial = "Planeta Digital", Pais = "España", Activo = true },
        new() { Id = 5, NombreEditorial = "Fondo de Cultura Económica", Pais = "México", Activo = true },
        new() { Id = 6, NombreEditorial = "Editorial Macro", Pais = "Perú", Activo = true },
        new() { Id = 7, NombreEditorial = "Penguin Random House", Pais = "España", Activo = true },
        new() { Id = 8, NombreEditorial = "Anaya Multimedia", Pais = "España", Activo = true },
        new() { Id = 9, NombreEditorial = "Editorial Alfaomega", Pais = "México", Activo = true },
        new() { Id = 10, NombreEditorial = "Siglo XXI Editores", Pais = "Argentina", Activo = true },
        new() { Id = 11, NombreEditorial = "Editorial Norma", Pais = "Colombia", Activo = true },
        new() { Id = 12, NombreEditorial = "Ediciones UC", Pais = "Chile", Activo = true },
        new() { Id = 13, NombreEditorial = "Springer Nature", Pais = "Alemania", Activo = true },
        new() { Id = 14, NombreEditorial = "Éditions Eyrolles", Pais = "Francia", Activo = true },
        new() { Id = 15, NombreEditorial = "Companhia das Letras", Pais = "Brasil", Activo = true },
        new() { Id = 16, NombreEditorial = "Kodansha", Pais = "Japón", Activo = true },
        new() { Id = 17, NombreEditorial = "Apress", Pais = "Estados Unidos", Activo = true },
        new() { Id = 18, NombreEditorial = "Pearson Educación", Pais = "Reino Unido", Activo = true }
    };

    public EditorialService(HttpClient http, TokenService tokenService, BackendStatusService backendStatus)
    {
        _http = http;
        _tokenService = tokenService;
        _backendStatus = backendStatus;
    }

    public async Task<List<EditorialDto>> GetAll()
    {
        if (_backendStatus.ModoMemoriaRAM)
        {
            return _editorialesLocales;
        }

        _tokenService.ApplyBearerToken(_http);
        try
        {
            var result = await _http.GetFromJsonAsync<List<EditorialDto>>("api/editorials");
            if (result != null && result.Count > 0)
            {
                return result;
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al obtener editoriales desde API: {ex.Message}");
        }

        return _editorialesLocales;
    }

    public async Task<bool> Create(EditorialDto editorial)
    {
        if (editorial == null || string.IsNullOrWhiteSpace(editorial.NombreEditorial))
            return false;

        if (!_backendStatus.ModoMemoriaRAM)
        {
            _tokenService.ApplyBearerToken(_http);
            try
            {
                var response = await _http.PostAsJsonAsync("api/editorials", editorial);
                if (response.IsSuccessStatusCode)
                    return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al crear editorial en API: {ex.Message}");
            }
        }

        var nextId = _editorialesLocales.Any() ? _editorialesLocales.Max(e => e.Id ?? 0) + 1 : 1;
        editorial.Id = nextId;
        editorial.Activo = true;
        _editorialesLocales.Add(editorial);
        return true;
    }

    public async Task<bool> Delete(long id)
    {
        if (!_backendStatus.ModoMemoriaRAM)
        {
            _tokenService.ApplyBearerToken(_http);
            try
            {
                var response = await _http.DeleteAsync($"api/editorials/{id}");
                if (response.IsSuccessStatusCode)
                    return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al eliminar editorial en API: {ex.Message}");
            }
        }

        var local = _editorialesLocales.FirstOrDefault(e => e.Id == id);
        if (local != null)
        {
            _editorialesLocales.Remove(local);
            return true;
        }
        return false;
    }
}
