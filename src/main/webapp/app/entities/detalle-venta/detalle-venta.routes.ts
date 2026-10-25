import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import DetalleVentaResolve from './route/detalle-venta-routing-resolve.service';

const detalleVentaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/detalle-venta').then(m => m.DetalleVenta),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/detalle-venta-detail').then(m => m.DetalleVentaDetail),
    resolve: {
      detalleVenta: DetalleVentaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/detalle-venta-update').then(m => m.DetalleVentaUpdate),
    resolve: {
      detalleVenta: DetalleVentaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/detalle-venta-update').then(m => m.DetalleVentaUpdate),
    resolve: {
      detalleVenta: DetalleVentaResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default detalleVentaRoute;
