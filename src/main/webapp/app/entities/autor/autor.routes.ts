import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import AutorResolve from './route/autor-routing-resolve.service';

const autorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/autor').then(m => m.Autor),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/autor-detail').then(m => m.AutorDetail),
    resolve: {
      autor: AutorResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/autor-update').then(m => m.AutorUpdate),
    resolve: {
      autor: AutorResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/autor-update').then(m => m.AutorUpdate),
    resolve: {
      autor: AutorResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default autorRoute;
