import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICalificacionUsuario } from '../calificacion-usuario.model';
import { CalificacionUsuarioService } from '../service/calificacion-usuario.service';

const calificacionUsuarioResolve = (route: ActivatedRouteSnapshot): Observable<null | ICalificacionUsuario> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CalificacionUsuarioService);
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

export default calificacionUsuarioResolve;
