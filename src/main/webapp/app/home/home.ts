import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { AccountService } from 'app/core/auth';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { IPromocion } from 'app/entities/promocion/promocion.model';
import { PromocionService } from 'app/entities/promocion/service/promocion.service';

import { CatalogoFiltrosComponent } from './components/catalogo-filtros/catalogo-filtros.component';
import { CompraAlertaComponent } from './components/compra-alerta/compra-alerta.component';
import { HeroBannerComponent } from './components/hero-banner/hero-banner.component';
import { LibroCardComponent } from './components/libro-card/libro-card.component';
import { ModulosGridComponent } from './components/modulos-grid/modulos-grid.component';
import { PromoBannerComponent } from './components/promo-banner/promo-banner.component';
import { CompraExitosa, LIMITE_CATEGORIAS_HOME, LIMITE_LIBROS_HOME, LIMITE_PROMOCIONES_HOME } from './model/tienda.model';
import { CheckoutTiendaService } from './service/checkout-tienda.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.html',
  styleUrl: './home.scss',
  imports: [
    CommonModule,
    RouterLink,
    FontAwesomeModule,
    HeroBannerComponent,
    CompraAlertaComponent,
    PromoBannerComponent,
    CatalogoFiltrosComponent,
    LibroCardComponent,
    ModulosGridComponent,
  ],
})
export default class Home implements OnInit {
  // Estado público reactivo (Signals)
  public readonly account = inject(AccountService).account;
  public readonly libros = signal<ILibro[]>([]);
  public readonly categorias = signal<ICategoria[]>([]);
  public readonly promociones = signal<IPromocion[]>([]);
  public readonly cargando = signal<boolean>(true);

  // Filtros reactivos
  public readonly filtroTexto = signal<string>('');
  public readonly categoriaSeleccionada = signal<number | null>(null);

  // Estado de checkout y alertas
  public readonly ultimaCompra = signal<CompraExitosa | null>(null);
  public readonly procesandoCompraId = signal<number | null>(null);
  public readonly mensajeError = signal<string | null>(null);

  // Catálogo computado reactivo
  public readonly librosFiltrados = computed(() => {
    const texto = this.filtroTexto().toLowerCase().trim();
    const catId = this.categoriaSeleccionada();

    return this.libros().filter(libro => {
      const coincideTexto =
        !texto ||
        (libro.titulo?.toLowerCase().includes(texto) ?? false) ||
        (libro.isbn?.toLowerCase().includes(texto) ?? false) ||
        (libro.idioma?.toLowerCase().includes(texto) ?? false);

      const coincideCategoria = !catId || (libro.categoriases?.some(cat => cat.id === catId) ?? false);

      return coincideTexto && coincideCategoria;
    });
  });

  // Servicios inyectados
  private readonly router = inject(Router);
  private readonly libroService = inject(LibroService);
  private readonly categoriaService = inject(CategoriaService);
  private readonly promocionService = inject(PromocionService);
  private readonly checkoutTiendaService = inject(CheckoutTiendaService);

  public ngOnInit(): void {
    this.cargarDatos();
  }

  public cargarDatos(): void {
    this.cargando.set(true);

    this.libroService.query({ size: LIMITE_LIBROS_HOME }).subscribe({
      next: res => {
        this.libros.set(res.body ?? []);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
      },
    });

    this.categoriaService.query({ size: LIMITE_CATEGORIAS_HOME }).subscribe({
      next: res => {
        this.categorias.set(res.body ?? []);
      },
    });

    this.promocionService.query({ size: LIMITE_PROMOCIONES_HOME }).subscribe({
      next: res => {
        const activas = (res.body ?? []).filter(p => p.activo);
        this.promociones.set(activas);
      },
    });
  }

  public onFiltroTextoChange(nuevoTexto: string): void {
    this.filtroTexto.set(nuevoTexto);
  }

  public onCategoriaSelect(id: number | null): void {
    this.categoriaSeleccionada.set(id);
  }

  public comprarLicencia(libro: ILibro): void {
    if (!this.account()) {
      this.login();
      return;
    }

    if (!libro.id) {
      return;
    }

    this.procesandoCompraId.set(libro.id);
    this.mensajeError.set(null);
    this.ultimaCompra.set(null);

    const usuarioActual = this.account()?.login ?? 'cliente_web';
    const promocionActiva = this.promociones().length > 0 ? this.promociones()[0] : null;

    this.checkoutTiendaService.procesarAdquisicion(libro, promocionActiva, usuarioActual).subscribe({
      next: compra => {
        this.procesandoCompraId.set(null);
        this.ultimaCompra.set(compra);
        window.scrollTo({ top: 0, behavior: 'smooth' });
      },
      error: () => {
        this.procesandoCompraId.set(null);
        this.mensajeError.set('Ocurrió un error al procesar la adquisición de la licencia. Intente nuevamente.');
      },
    });
  }

  public cerrarAlertaCompra(): void {
    this.ultimaCompra.set(null);
  }

  public limpiarFiltros(): void {
    this.filtroTexto.set('');
    this.categoriaSeleccionada.set(null);
  }

  public login(): void {
    this.router.navigate(['/login']);
  }
}
