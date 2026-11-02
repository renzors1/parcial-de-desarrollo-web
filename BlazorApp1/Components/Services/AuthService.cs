using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace BlazorApp1.Components.Services;

public class AuthService
{
    private readonly HttpClient _http;
    private readonly TokenService _tokenService;
    private readonly BackendStatusService _backendStatus;

    private static readonly List<(string Username, string Password, UserAccountDto Account)> _usuariosLocales = new()
    {
        ("admin", "admin123", new UserAccountDto
        {
            Id = 1,
            Login = "admin",
            FirstName = "Renzo",
            LastName = "Administrador",
            Email = "admin@bookstore.pe",
            Authorities = new List<string> { "ROLE_ADMIN", "ROLE_USER" }
        }),
        ("admin", "admin", new UserAccountDto
        {
            Id = 1,
            Login = "admin",
            FirstName = "Renzo",
            LastName = "Administrador",
            Email = "admin@bookstore.pe",
            Authorities = new List<string> { "ROLE_ADMIN", "ROLE_USER" }
        }),
        ("carlos.mendoza", "carlos123", new UserAccountDto
        {
            Id = 2,
            Login = "carlos.mendoza",
            FirstName = "Carlos",
            LastName = "Mendoza",
            Email = "carlos.mendoza@gmail.com",
            Authorities = new List<string> { "ROLE_USER" }
        }),
        ("ana.torres", "ana123", new UserAccountDto
        {
            Id = 3,
            Login = "ana.torres",
            FirstName = "Ana",
            LastName = "Torres",
            Email = "ana.torres@hotmail.com",
            Authorities = new List<string> { "ROLE_USER" }
        }),
        ("user", "user123", new UserAccountDto
        {
            Id = 4,
            Login = "user",
            FirstName = "Usuario",
            LastName = "Cliente Demo",
            Email = "cliente@bookstore.pe",
            Authorities = new List<string> { "ROLE_USER" }
        }),
        ("user", "user", new UserAccountDto
        {
            Id = 4,
            Login = "user",
            FirstName = "Usuario",
            LastName = "Cliente Demo",
            Email = "cliente@bookstore.pe",
            Authorities = new List<string> { "ROLE_USER" }
        })
    };

    public AuthService(HttpClient http, TokenService tokenService, BackendStatusService backendStatus)
    {
        _http = http;
        _tokenService = tokenService;
        _backendStatus = backendStatus;
    }

    public async Task<bool> Login(string user, string pass)
    {
        if (_backendStatus.ModoMemoriaRAM)
        {
            var matchRam = _usuariosLocales.FirstOrDefault(u =>
                string.Equals(u.Username, user, StringComparison.OrdinalIgnoreCase) && u.Password == pass);

            if (matchRam.Account != null)
            {
                _tokenService.SetSession($"simulated_jwt_token_{matchRam.Account.Login}", matchRam.Account);
                return true;
            }

            return false;
        }

        var loginRequest = new LoginRequest
        {
            Username = user,
            Password = pass,
            RememberMe = true
        };

        try
        {
            var response = await _http.PostAsJsonAsync("api/authenticate", loginRequest);

            if (response.IsSuccessStatusCode)
            {
                var result = await response.Content.ReadFromJsonAsync<LoginResponse>();
                if (result != null && !string.IsNullOrWhiteSpace(result.IdToken))
                {
                    _tokenService.Token = result.IdToken;
                    await CargarPerfilUsuarioAsync();
                    return true;
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), autenticando con repositorio local.");
        }

        var match = _usuariosLocales.FirstOrDefault(u =>
            string.Equals(u.Username, user, StringComparison.OrdinalIgnoreCase) && u.Password == pass);

        if (match.Account != null)
        {
            _tokenService.SetSession($"simulated_jwt_token_{match.Account.Login}", match.Account);
            return true;
        }

        return false;
    }

    public async Task<string?> RegisterAsync(RegisterRequest request)
    {
        try
        {
            var response = await _http.PostAsJsonAsync("api/register", request);
            if (response.IsSuccessStatusCode)
            {
                return null;
            }

            var errorBody = await response.Content.ReadAsStringAsync();
            if (!string.IsNullOrWhiteSpace(errorBody)) return errorBody;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Conexión remota no disponible ({ex.Message}), registrando en repositorio local.");
        }

        if (string.IsNullOrWhiteSpace(request.Login) || string.IsNullOrWhiteSpace(request.Password))
        {
            return "El usuario y contraseña son obligatorios.";
        }

        if (_usuariosLocales.Any(u => string.Equals(u.Username, request.Login, StringComparison.OrdinalIgnoreCase)))
        {
            return "El nombre de usuario ya se encuentra registrado.";
        }

        var nuevoId = _usuariosLocales.Max(u => u.Account.Id ?? 0) + 1;
        var nuevaCuenta = new UserAccountDto
        {
            Id = nuevoId,
            Login = request.Login,
            FirstName = request.FirstName ?? request.Login,
            LastName = request.LastName ?? "Usuario",
            Email = request.Email,
            Authorities = new List<string> { "ROLE_USER" }
        };

        _usuariosLocales.Add((request.Login, request.Password, nuevaCuenta));
        return null;
    }

    public async Task CargarPerfilUsuarioAsync()
    {
        if (string.IsNullOrWhiteSpace(_tokenService.Token)) return;

        try
        {
            var req = new HttpRequestMessage(HttpMethod.Get, "api/account");
            req.Headers.Authorization = new AuthenticationHeaderValue("Bearer", _tokenService.Token);

            var res = await _http.SendAsync(req);
            if (res.IsSuccessStatusCode)
            {
                var account = await res.Content.ReadFromJsonAsync<UserAccountDto>();
                if (account != null)
                {
                    _tokenService.CurrentUser = account;
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al obtener perfil: {ex.Message}");
        }
    }

    public void Logout()
    {
        _tokenService.Clear();
    }
}
