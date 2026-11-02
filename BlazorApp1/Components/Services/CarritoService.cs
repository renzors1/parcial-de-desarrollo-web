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

    /// <summary>
    /// Calcula la porción del subtotal sobre la cual aplica la promoción vigente.
    /// Si la campaña asigna libros específicos, solo se totalizan esos libros.
    /// Si es una campaña global, abarca todo el subtotal.
    /// </summary>
    public decimal SubtotalAplicable
    {
        get
        {
            if (PromocionAplicada == null || Items.Count == 0) return 0m;

            if (PromocionAplicada.Libroses != null && PromocionAplicada.Libroses.Count > 0)
            {
                return Items
                    .Where(i => PromocionAplicada.AplicaALibro(i.Libro?.Id))
                    .Sum(i => i.Subtotal);
            }

            return SubtotalBruto;
        }
    }

    /// <summary>
    /// Descuento monetario calculado respetando:
    /// 1. Monto mínimo de compra global exigido por la promoción.
    /// 2. Alcance por libros (solo aplica a ítems autorizados cuando no es global).
    /// 3. Tipo de descuento (PORCENTAJE o MONTO_FIJO).
    /// 4. Tope máximo de descuento (si aplica).
    /// </summary>
    public decimal MontoDescuento
    {
        get
        {
            if (PromocionAplicada == null || Items.Count == 0) return 0m;

            // Si tiene MontoMinimoCompra y el subtotal no llega, el descuento es 0
            if (PromocionAplicada.MontoMinimoCompra.HasValue && SubtotalBruto < PromocionAplicada.MontoMinimoCompra.Value)
            {
                return 0m;
            }

            var subtotalAplicable = SubtotalAplicable;
            if (subtotalAplicable <= 0) return 0m;

            decimal descuento;
            if (string.Equals(PromocionAplicada.TipoDescuento, "MONTO_FIJO", StringComparison.OrdinalIgnoreCase))
            {
                // MONTO_FIJO: aplica el monto fijo o hasta el subtotal aplicable
                descuento = Math.Min(PromocionAplicada.DescuentoFijo, subtotalAplicable);
            }
            else
            {
                // PORCENTAJE
                descuento = Math.Round(subtotalAplicable * (decimal)(PromocionAplicada.PorcentajeDescuento / 100.0f), 2);
            }

            // Respetar TopeDescuento si está configurado
            if (PromocionAplicada.TopeDescuento.HasValue && PromocionAplicada.TopeDescuento.Value > 0)
            {
                descuento = Math.Min(descuento, PromocionAplicada.TopeDescuento.Value);
            }

            return Math.Max(0m, Math.Min(descuento, SubtotalBruto));
        }
    }

    public decimal TotalPagar => Math.Max(0m, SubtotalBruto - MontoDescuento);

    public decimal CalcularDescuentoPorItem(CarritoItem item)
    {
        if (PromocionAplicada == null || item == null) return 0m;
        if (!PromocionAplicada.AplicaALibro(item.Libro?.Id)) return 0m;
        if (PromocionAplicada.MontoMinimoCompra.HasValue && SubtotalBruto < PromocionAplicada.MontoMinimoCompra.Value) return 0m;

        if (string.Equals(PromocionAplicada.TipoDescuento, "MONTO_FIJO", StringComparison.OrdinalIgnoreCase))
        {
            var totalAplicable = SubtotalAplicable;
            if (totalAplicable <= 0) return 0m;
            var proporcion = item.Subtotal / totalAplicable;
            return Math.Round(MontoDescuento * proporcion, 2);
        }
        else
        {
            var desc = Math.Round(item.Subtotal * (decimal)(PromocionAplicada.PorcentajeDescuento / 100.0f), 2);
            return Math.Min(desc, item.Subtotal);
        }
    }

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
