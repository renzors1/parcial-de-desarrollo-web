using System.ComponentModel.DataAnnotations;

namespace BlazorApp1.Components.Services;

public class LoginRequest
{
    [Required(ErrorMessage = "El nombre de usuario es obligatorio.")]
    [StringLength(50, MinimumLength = 2, ErrorMessage = "El usuario debe tener entre 2 y 50 caracteres.")]
    public string Username { get; set; } = string.Empty;

    [Required(ErrorMessage = "La contraseña es obligatoria.")]
    [StringLength(100, MinimumLength = 3, ErrorMessage = "La contraseña debe tener entre 3 y 100 caracteres.")]
    public string Password { get; set; } = string.Empty;

    public bool RememberMe { get; set; } = false;
}
