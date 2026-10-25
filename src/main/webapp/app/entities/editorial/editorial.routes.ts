import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import EditorialResolve from './route/editorial-routing-resolve.service';

const editorialRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/editorial').then(m => m.Editorial),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/editorial-detail').then(m => m.EditorialDetail),
    resolve: {
      editorial: EditorialResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/editorial-update').then(m => m.EditorialUpdate),
    resolve: {
      editorial: EditorialResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/editorial-update').then(m => m.EditorialUpdate),
    resolve: {
      editorial: EditorialResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default editorialRoute;
