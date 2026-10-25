import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import LicenciaDigitalResolve from './route/licencia-digital-routing-resolve.service';

const licenciaDigitalRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/licencia-digital').then(m => m.LicenciaDigital),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/licencia-digital-detail').then(m => m.LicenciaDigitalDetail),
    resolve: {
      licenciaDigital: LicenciaDigitalResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/licencia-digital-update').then(m => m.LicenciaDigitalUpdate),
    resolve: {
      licenciaDigital: LicenciaDigitalResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/licencia-digital-update').then(m => m.LicenciaDigitalUpdate),
    resolve: {
      licenciaDigital: LicenciaDigitalResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default licenciaDigitalRoute;
