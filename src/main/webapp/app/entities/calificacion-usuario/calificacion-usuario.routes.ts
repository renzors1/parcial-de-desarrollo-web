import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CalificacionUsuarioResolve from './route/calificacion-usuario-routing-resolve.service';

const calificacionUsuarioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/calificacion-usuario').then(m => m.CalificacionUsuario),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/calificacion-usuario-detail').then(m => m.CalificacionUsuarioDetail),
    resolve: {
      calificacionUsuario: CalificacionUsuarioResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/calificacion-usuario-update').then(m => m.CalificacionUsuarioUpdate),
    resolve: {
      calificacionUsuario: CalificacionUsuarioResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/calificacion-usuario-update').then(m => m.CalificacionUsuarioUpdate),
    resolve: {
      calificacionUsuario: CalificacionUsuarioResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default calificacionUsuarioRoute;
