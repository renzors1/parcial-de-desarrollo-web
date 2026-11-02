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
        new() { Id = 5, NombreEditorial = "Fondo de Cultura Económica", Pais = "México", Activo = true }
    };

    public EditorialService(HttpClient http, TokenService tokenService, BackendStatusService backendStatus)
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

    public async Task<List<EditorialDto>> GetAll()
    {
        if (_backendStatus.ModoMemoriaRAM)
        {
            return _editorialesLocales;
        }

        SetAuthorizationHeader();
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
}
