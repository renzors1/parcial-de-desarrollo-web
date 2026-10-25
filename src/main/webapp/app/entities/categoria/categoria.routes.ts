import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CategoriaResolve from './route/categoria-routing-resolve.service';

const categoriaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/categoria').then(m => m.Categoria),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/categoria-detail').then(m => m.CategoriaDetail),
    resolve: {
      categoria: CategoriaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/categoria-update').then(m => m.CategoriaUpdate),
    resolve: {
      categoria: CategoriaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/categoria-update').then(m => m.CategoriaUpdate),
    resolve: {
      categoria: CategoriaResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default categoriaRoute;
