namespace BlazorApp1.Components.Services;

public class CarritoItem
{
    public LibroDto Libro { get; set; } = new();
    public int Cantidad { get; set; } = 1;
    public decimal Subtotal => Libro.Precio * Cantidad;
}

public class CarritoService
{
    public const int MaxCantidadPorTitulo = 20;

    public List<CarritoItem> Items { get; private set; } = new();
    public PromocionDto? PromocionAplicada { get; private set; }

    public event Action? OnChange;

    public int TotalItems => Items.Sum(i => i.Cantidad);
    public decimal SubtotalBruto => Items.Sum(i => i.Subtotal);

    public decimal MontoDescuento => PromocionAplicada != null
        ? Math.Round(SubtotalBruto * (decimal)(PromocionAplicada.PorcentajeDescuento / 100.0f), 2)
        : 0m;

    public decimal TotalPagar => Math.Max(0m, SubtotalBruto - MontoDescuento);

    public void AgregarLibro(LibroDto libro)
    {
        var existente = Items.FirstOrDefault(i => i.Libro.Id == libro.Id);
        if (existente != null)
        {
            if (existente.Cantidad < MaxCantidadPorTitulo)
            {
                existente.Cantidad++;
            }
        }
        else
        {
            Items.Add(new CarritoItem { Libro = libro, Cantidad = 1 });
        }
        NotifyStateChanged();
    }

    public void ModificarCantidad(long libroId, int delta)
    {
        var item = Items.FirstOrDefault(i => i.Libro.Id == libroId);
        if (item != null)
        {
            var nueva = item.Cantidad + delta;
            if (nueva <= 0)
            {
                Items.Remove(item);
            }
            else
            {
                item.Cantidad = Math.Min(nueva, MaxCantidadPorTitulo);
            }
            NotifyStateChanged();
        }
    }

    public void EliminarItem(long libroId)
    {
        var item = Items.FirstOrDefault(i => i.Libro.Id == libroId);
        if (item != null)
        {
            Items.Remove(item);
            NotifyStateChanged();
        }
    }

    public void AplicarPromocion(PromocionDto? promo)
    {
        PromocionAplicada = promo;
        NotifyStateChanged();
    }

    public void RemoverPromocion()
    {
        PromocionAplicada = null;
        NotifyStateChanged();
    }

    public void Limpiar()
    {
        Items.Clear();
        PromocionAplicada = null;
        NotifyStateChanged();
    }

    private void NotifyStateChanged() => OnChange?.Invoke();
}
