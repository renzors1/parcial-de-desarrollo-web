import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import RolResolve from './route/rol-routing-resolve.service';

const rolRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/rol').then(m => m.Rol),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/rol-detail').then(m => m.RolDetail),
    resolve: {
      rol: RolResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/rol-update').then(m => m.RolUpdate),
    resolve: {
      rol: RolResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/rol-update').then(m => m.RolUpdate),
    resolve: {
      rol: RolResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default rolRoute;
