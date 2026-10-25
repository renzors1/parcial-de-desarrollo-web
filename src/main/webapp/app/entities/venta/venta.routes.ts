import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import VentaResolve from './route/venta-routing-resolve.service';

const ventaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/venta').then(m => m.Venta),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/venta-detail').then(m => m.VentaDetail),
    resolve: {
      venta: VentaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/venta-update').then(m => m.VentaUpdate),
    resolve: {
      venta: VentaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/venta-update').then(m => m.VentaUpdate),
    resolve: {
      venta: VentaResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default ventaRoute;
