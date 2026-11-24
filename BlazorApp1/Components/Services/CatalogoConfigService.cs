namespace BlazorApp1.Components.Services;

public class CatalogoConfigService
{
    private static readonly List<string> _idiomas = new()
    {
        "Español", "Inglés", "Portugués", "Francés", "Alemán", "Italiano",
        "Chino Mandarín", "Japonés", "Coreano", "Ruso", "Árabe", "Hindi",
        "Neerlandés", "Sueco", "Noruego", "Danés", "Finlandés", "Polaco",
        "Turco", "Griego", "Hebreo", "Checo", "Húngaro", "Rumano",
        "Ucraniano", "Vietnamita", "Tailandés", "Indonesio", "Catalán",
        "Gallego", "Euskera", "Quechua", "Latín"
    };

    private static readonly List<string> _categorias = new()
    {
        "Programación", "Arquitectura", "Sistemas", "DevOps",
        "Desarrollo Web", "Base de Datos", "Inteligencia Artificial",
        "Ciberseguridad", "Redes", "Literatura", "Ciencia", "Historia"
    };

    public List<string> GetIdiomas() => _idiomas.OrderBy(x => x).ToList();

    public List<string> GetCategorias() => _categorias.OrderBy(x => x).ToList();

    public bool AgregarIdioma(string idioma)
    {
        var limpio = idioma?.Trim();
        if (string.IsNullOrWhiteSpace(limpio) || _idiomas.Any(i => i.Equals(limpio, StringComparison.OrdinalIgnoreCase)))
            return false;
        _idiomas.Add(limpio);
        return true;
    }

    public bool EliminarIdioma(string idioma)
    {
        var item = _idiomas.FirstOrDefault(i => i.Equals(idioma, StringComparison.OrdinalIgnoreCase));
        if (item == null) return false;
        _idiomas.Remove(item);
        return true;
    }

    public bool AgregarCategoria(string categoria)
    {
        var limpio = categoria?.Trim();
        if (string.IsNullOrWhiteSpace(limpio) || _categorias.Any(c => c.Equals(limpio, StringComparison.OrdinalIgnoreCase)))
            return false;
        _categorias.Add(limpio);
        return true;
    }

    public bool EliminarCategoria(string categoria)
    {
        var item = _categorias.FirstOrDefault(c => c.Equals(categoria, StringComparison.OrdinalIgnoreCase));
        if (item == null) return false;
        _categorias.Remove(item);
        return true;
    }
}
