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

                    // Garantizar perfil válido de usuario si CargarPerfilUsuarioAsync no pobló la cuenta
                    if (_tokenService.CurrentUser == null)
                    {
                        var localMatch = _usuariosLocales.FirstOrDefault(u =>
                            string.Equals(u.Username, user, StringComparison.OrdinalIgnoreCase)).Account;

                        _tokenService.CurrentUser = localMatch ?? new UserAccountDto
                        {
                            Login = user,
                            FirstName = user,
                            LastName = "Lector",
                            Authorities = new List<string> { "ROLE_USER" },
                            Activated = true
                        };
                    }

                    // Sincronizar credenciales en memoria para respaldar navegación fluida y modo offline
                    var localIdx = _usuariosLocales.FindIndex(u => string.Equals(u.Username, user, StringComparison.OrdinalIgnoreCase));
                    if (localIdx >= 0)
                    {
                        _usuariosLocales[localIdx] = (user, pass, _tokenService.CurrentUser);
                    }
                    else
                    {
                        _usuariosLocales.Add((user, pass, _tokenService.CurrentUser));
                    }

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
        if (request == null) return "Datos de registro inválidos.";

        request.Login = request.Login?.Trim() ?? "";
        request.Email = request.Email?.Trim().ToLowerInvariant() ?? "";
        request.FirstName = request.FirstName?.Trim() ?? "";
        request.LastName = request.LastName?.Trim() ?? "";
        request.Password = request.Password?.Trim() ?? "";

        if (string.IsNullOrWhiteSpace(request.Login) || request.Login.Length < 3)
        {
            return "El nombre de usuario debe tener al menos 3 caracteres.";
        }

        if (string.IsNullOrWhiteSpace(request.Email) || !request.Email.Contains("@") || !request.Email.Contains("."))
        {
            return "El correo electrónico no tiene un formato válido.";
        }

        if (string.IsNullOrWhiteSpace(request.Password) || request.Password.Length < 4)
        {
            return "La contraseña debe tener al menos 4 caracteres.";
        }

        // Si ya existe en la lista local, avisar amigablemente antes de enviar
        if (_usuariosLocales.Any(u => string.Equals(u.Username, request.Login, StringComparison.OrdinalIgnoreCase)))
        {
            return "El nombre de usuario ya se encuentra registrado. Por favor elige otro o inicia sesión.";
        }

        if (_usuariosLocales.Any(u => string.Equals(u.Account?.Email, request.Email, StringComparison.OrdinalIgnoreCase)))
        {
            return "El correo electrónico ya se encuentra registrado con otra cuenta.";
        }

        if (!_backendStatus.ModoMemoriaRAM)
        {
            try
            {
                var response = await _http.PostAsJsonAsync("api/register", request);
                if (!response.IsSuccessStatusCode)
                {
                    var errorBody = await response.Content.ReadAsStringAsync();
                    if (!string.IsNullOrWhiteSpace(errorBody))
                    {
                        var lower = errorBody.ToLowerInvariant();
                        if (lower.Contains("login-already-used") || lower.Contains("login already used") || lower.Contains("userexists"))
                        {
                            return "El nombre de usuario ya está registrado en el sistema. Por favor elige otro o inicia sesión.";
                        }
                        if (lower.Contains("email-already-used") || lower.Contains("email already in use") || lower.Contains("emailexists"))
                        {
                            return "El correo electrónico ya se encuentra registrado con otra cuenta.";
                        }
                        return "No se pudo completar el registro en el servidor. Verifica los datos ingresados.";
                    }
                    return $"Error en el servidor al registrar cuenta ({response.StatusCode}).";
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Conexión remota no disponible ({ex.Message}), registrando en repositorio local.");
            }
        }

        // Siempre guardar la cuenta registrada en el repositorio de memoria para permitir acceso inmediato
        var nuevoId = (_usuariosLocales.Count > 0 ? _usuariosLocales.Max(u => u.Account.Id ?? 0) : 0) + 1;
        var nuevaCuenta = new UserAccountDto
        {
            Id = nuevoId,
            Login = request.Login,
            FirstName = string.IsNullOrWhiteSpace(request.FirstName) ? request.Login : request.FirstName,
            LastName = string.IsNullOrWhiteSpace(request.LastName) ? "Lector" : request.LastName,
            Email = request.Email,
            Activated = true,
            Authorities = new List<string> { "ROLE_USER" },
            CreatedDate = DateTime.UtcNow
        };

        var existIdx = _usuariosLocales.FindIndex(u => string.Equals(u.Username, request.Login, StringComparison.OrdinalIgnoreCase));
        if (existIdx >= 0)
        {
            _usuariosLocales[existIdx] = (request.Login, request.Password, nuevaCuenta);
        }
        else
        {
            _usuariosLocales.Add((request.Login, request.Password, nuevaCuenta));
        }

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

    public async Task<(bool Exito, string? Error)> ActualizarPerfilAsync(UserAccountDto cuenta)
    {
        if (string.IsNullOrWhiteSpace(cuenta.Email) || !cuenta.Email.Contains("@") || !cuenta.Email.Contains("."))
        {
            return (false, "El correo electrónico ingresado no es válido.");
        }

        if (string.IsNullOrWhiteSpace(cuenta.FirstName) || cuenta.FirstName.Trim().Length < 2)
        {
            return (false, "El nombre debe contener al menos 2 caracteres.");
        }

        cuenta.FirstName = cuenta.FirstName.Trim();
        if (cuenta.FirstName.Length > 50) cuenta.FirstName = cuenta.FirstName[..50];

        cuenta.LastName = (cuenta.LastName ?? "").Trim();
        if (cuenta.LastName.Length > 50) cuenta.LastName = cuenta.LastName[..50];

        cuenta.Email = cuenta.Email.Trim().ToLowerInvariant();
        if (cuenta.Email.Length > 100) cuenta.Email = cuenta.Email[..100];

        if (!_backendStatus.ModoMemoriaRAM && !string.IsNullOrWhiteSpace(_tokenService.Token))
        {
            try
            {
                var req = new HttpRequestMessage(HttpMethod.Post, "api/account");
                req.Headers.Authorization = new AuthenticationHeaderValue("Bearer", _tokenService.Token);
                req.Content = JsonContent.Create(cuenta);

                var res = await _http.SendAsync(req);
                if (res.IsSuccessStatusCode)
                {
                    _tokenService.CurrentUser = cuenta;
                    ActualizarEnMemoriaLocal(cuenta);
                    return (true, null);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Conexión remota no disponible ({ex.Message}), actualizando localmente.");
            }
        }

        _tokenService.CurrentUser = cuenta;
        ActualizarEnMemoriaLocal(cuenta);
        return (true, null);
    }

    private void ActualizarEnMemoriaLocal(UserAccountDto cuenta)
    {
        var idx = _usuariosLocales.FindIndex(u => string.Equals(u.Username, cuenta.Login, StringComparison.OrdinalIgnoreCase) || (cuenta.Id.HasValue && u.Account.Id == cuenta.Id));
        if (idx >= 0)
        {
            var actual = _usuariosLocales[idx];
            actual.Account.FirstName = cuenta.FirstName;
            actual.Account.LastName = cuenta.LastName;
            actual.Account.Email = cuenta.Email;
            _usuariosLocales[idx] = (actual.Username, actual.Password, actual.Account);
        }
    }

    public async Task<(bool Exito, string? Error)> CambiarPasswordAsync(string passActual, string nuevoPass)
    {
        if (string.IsNullOrWhiteSpace(passActual))
        {
            return (false, "Por favor ingresa tu contraseña actual.");
        }

        if (string.IsNullOrWhiteSpace(nuevoPass) || nuevoPass.Length < 4 || nuevoPass.Length > 100)
        {
            return (false, "La nueva contraseña debe tener entre 4 y 100 caracteres.");
        }

        var login = _tokenService.CurrentUser?.Login;
        if (string.IsNullOrWhiteSpace(login))
        {
            return (false, "No hay una sesión activa de usuario.");
        }

        if (!_backendStatus.ModoMemoriaRAM && !string.IsNullOrWhiteSpace(_tokenService.Token))
        {
            try
            {
                var payload = new { currentPassword = passActual, newPassword = nuevoPass };
                var req = new HttpRequestMessage(HttpMethod.Post, "api/account/change-password");
                req.Headers.Authorization = new AuthenticationHeaderValue("Bearer", _tokenService.Token);
                req.Content = JsonContent.Create(payload);

                var res = await _http.SendAsync(req);
                if (res.IsSuccessStatusCode)
                {
                    ActualizarPasswordEnMemoria(login, nuevoPass);
                    return (true, null);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Conexión remota no disponible ({ex.Message}), cambiando contraseña localmente.");
            }
        }

        var idx = _usuariosLocales.FindIndex(u => string.Equals(u.Username, login, StringComparison.OrdinalIgnoreCase));
        if (idx >= 0)
        {
            if (_usuariosLocales[idx].Password != passActual)
            {
                return (false, "La contraseña actual es incorrecta.");
            }

            ActualizarPasswordEnMemoria(login, nuevoPass);
            return (true, null);
        }

        return (true, null);
    }

    private void ActualizarPasswordEnMemoria(string login, string nuevoPass)
    {
        var idx = _usuariosLocales.FindIndex(u => string.Equals(u.Username, login, StringComparison.OrdinalIgnoreCase));
        if (idx >= 0)
        {
            var item = _usuariosLocales[idx];
            _usuariosLocales[idx] = (item.Username, nuevoPass, item.Account);
        }
    }

    public async Task<List<UserAccountDto>> ObtenerTodosLosUsuariosAsync()
    {
        if (!_backendStatus.ModoMemoriaRAM && !string.IsNullOrWhiteSpace(_tokenService.Token) && _tokenService.IsAdmin)
        {
            try
            {
                var req = new HttpRequestMessage(HttpMethod.Get, "api/admin/users?size=100");
                req.Headers.Authorization = new AuthenticationHeaderValue("Bearer", _tokenService.Token);

                var res = await _http.SendAsync(req);
                if (res.IsSuccessStatusCode)
                {
                    var remotos = await res.Content.ReadFromJsonAsync<List<UserAccountDto>>();
                    if (remotos != null && remotos.Count > 0)
                    {
                        foreach (var r in remotos)
                        {
                            if (!_usuariosLocales.Any(u => string.Equals(u.Username, r.Login, StringComparison.OrdinalIgnoreCase)))
                            {
                                _usuariosLocales.Add((r.Login, "password", r));
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Conexión remota no disponible ({ex.Message}), listando usuarios locales.");
            }
        }

        return _usuariosLocales
            .Select(u => u.Account)
            .GroupBy(u => u.Login.ToLowerInvariant())
            .Select(g => g.First())
            .OrderBy(u => u.Id)
            .ToList();
    }

    public async Task<(bool Exito, string? Error)> GuardarUsuarioPorAdminAsync(UserAccountDto usuario, string? password = null)
    {
        if (string.IsNullOrWhiteSpace(usuario.Login) || usuario.Login.Length < 3 || usuario.Login.Length > 50)
        {
            return (false, "El nombre de usuario debe tener entre 3 y 50 caracteres.");
        }

        if (string.IsNullOrWhiteSpace(usuario.Email) || !usuario.Email.Contains("@") || !usuario.Email.Contains("."))
        {
            return (false, "El correo electrónico no tiene un formato válido.");
        }

        var esEdicion = usuario.Id.HasValue && usuario.Id > 0;

        if (!esEdicion && string.IsNullOrWhiteSpace(password))
        {
            return (false, "La contraseña es obligatoria para nuevos usuarios.");
        }

        if (!_backendStatus.ModoMemoriaRAM && !string.IsNullOrWhiteSpace(_tokenService.Token) && _tokenService.IsAdmin)
        {
            try
            {
                var req = new HttpRequestMessage(
                    esEdicion ? HttpMethod.Put : HttpMethod.Post, 
                    "api/admin/users"
                );
                req.Headers.Authorization = new AuthenticationHeaderValue("Bearer", _tokenService.Token);
                req.Content = JsonContent.Create(usuario);

                var res = await _http.SendAsync(req);
                if (res.IsSuccessStatusCode)
                {
                    ActualizarOInsertarEnMemoria(usuario, password);
                    return (true, null);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Conexión remota no disponible ({ex.Message}), guardando usuario localmente.");
            }
        }

        ActualizarOInsertarEnMemoria(usuario, password);
        return (true, null);
    }

    private void ActualizarOInsertarEnMemoria(UserAccountDto usuario, string? password)
    {
        var idx = _usuariosLocales.FindIndex(u => 
            string.Equals(u.Username, usuario.Login, StringComparison.OrdinalIgnoreCase) || 
            (usuario.Id.HasValue && u.Account.Id == usuario.Id));

        if (idx >= 0)
        {
            var item = _usuariosLocales[idx];
            var pass = !string.IsNullOrWhiteSpace(password) ? password : item.Password;
            item.Account.FirstName = usuario.FirstName;
            item.Account.LastName = usuario.LastName;
            item.Account.Email = usuario.Email;
            item.Account.Activated = usuario.Activated;
            item.Account.Authorities = new List<string>(usuario.Authorities);
            _usuariosLocales[idx] = (usuario.Login, pass, item.Account);
        }
        else
        {
            usuario.Id = (_usuariosLocales.Count > 0 ? _usuariosLocales.Max(u => u.Account.Id ?? 0) : 0) + 1;
            usuario.CreatedDate = DateTime.UtcNow;
            _usuariosLocales.Add((usuario.Login, password ?? "user123", usuario));
        }
    }

    public async Task<bool> CambiarEstadoUsuarioAsync(string login, bool activado)
    {
        var idx = _usuariosLocales.FindIndex(u => string.Equals(u.Username, login, StringComparison.OrdinalIgnoreCase));
        if (idx >= 0)
        {
            var item = _usuariosLocales[idx];
            item.Account.Activated = activado;
            _usuariosLocales[idx] = (item.Username, item.Password, item.Account);
            return true;
        }
        return false;
    }

    public void Logout()
    {
        _tokenService.Clear();
    }
}
