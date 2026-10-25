import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ILicenciaDigital } from '../licencia-digital.model';
import { LicenciaDigitalService } from '../service/licencia-digital.service';

const licenciaDigitalResolve = (route: ActivatedRouteSnapshot): Observable<null | ILicenciaDigital> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(LicenciaDigitalService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default licenciaDigitalResolve;
