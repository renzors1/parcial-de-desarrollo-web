import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import UsuarioResolve from './route/usuario-routing-resolve.service';

const usuarioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/usuario').then(m => m.Usuario),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/usuario-detail').then(m => m.UsuarioDetail),
    resolve: {
      usuario: UsuarioResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/usuario-update').then(m => m.UsuarioUpdate),
    resolve: {
      usuario: UsuarioResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/usuario-update').then(m => m.UsuarioUpdate),
    resolve: {
      usuario: UsuarioResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default usuarioRoute;
