import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

interface ModuloNavegacion {
  titulo: string;
  descripcion: string;
  icono: string;
  ruta: string;
  colorClase: string;
}

@Component({
  selector: 'jhi-modulos-grid',
  templateUrl: './modulos-grid.component.html',
  styleUrl: './modulos-grid.component.scss',
  imports: [CommonModule, RouterLink, FontAwesomeModule],
})
export class ModulosGridComponent {
  public readonly modulos: readonly ModuloNavegacion[] = [
    {
      titulo: 'Libros',
      descripcion: 'Catálogo, precios e ISBN',
      icono: 'book',
      ruta: '/libro',
      colorClase: 'bg-primary-subtle text-primary',
    },
    {
      titulo: 'Licencias Digitales',
      descripcion: 'Disponibles y asignadas',
      icono: 'key',
      ruta: '/licencia-digital',
      colorClase: 'bg-warning-subtle text-warning',
    },
    {
      titulo: 'Ventas',
      descripcion: 'Comprobantes y totales',
      icono: 'receipt',
      ruta: '/venta',
      colorClase: 'bg-success-subtle text-success',
    },
    {
      titulo: 'Detalle de Ventas',
      descripcion: 'Líneas y subtotales',
      icono: 'file-alt',
      ruta: '/detalle-venta',
      colorClase: 'bg-info-subtle text-info',
    },
    {
      titulo: 'Promociones',
      descripcion: 'Descuentos y cupos',
      icono: 'percentage',
      ruta: '/promocion',
      colorClase: 'bg-danger-subtle text-danger',
    },
    {
      titulo: 'Calificaciones',
      descripcion: 'Evaluación de clientes',
      icono: 'star',
      ruta: '/calificacion-usuario',
      colorClase: 'bg-warning-subtle text-warning',
    },
    {
      titulo: 'Categorías',
      descripcion: 'Clasificación temática',
      icono: 'folder',
      ruta: '/categoria',
      colorClase: 'bg-secondary-subtle text-secondary',
    },
    {
      titulo: 'Autores',
      descripcion: 'Escritores y biografías',
      icono: 'pencil-alt',
      ruta: '/autor',
      colorClase: 'bg-dark-subtle text-dark',
    },
  ];
}
