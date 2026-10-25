import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import LibroResolve from './route/libro-routing-resolve.service';

const libroRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/libro').then(m => m.Libro),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/libro-detail').then(m => m.LibroDetail),
    resolve: {
      libro: LibroResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/libro-update').then(m => m.LibroUpdate),
    resolve: {
      libro: LibroResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/libro-update').then(m => m.LibroUpdate),
    resolve: {
      libro: LibroResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default libroRoute;
