import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PromocionResolve from './route/promocion-routing-resolve.service';

const promocionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/promocion').then(m => m.Promocion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/promocion-detail').then(m => m.PromocionDetail),
    resolve: {
      promocion: PromocionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/promocion-update').then(m => m.PromocionUpdate),
    resolve: {
      promocion: PromocionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/promocion-update').then(m => m.PromocionUpdate),
    resolve: {
      promocion: PromocionResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default promocionRoute;
