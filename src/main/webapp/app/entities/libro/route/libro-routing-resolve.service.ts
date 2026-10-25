import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ILibro } from '../libro.model';
import { LibroService } from '../service/libro.service';

const libroResolve = (route: ActivatedRouteSnapshot): Observable<null | ILibro> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(LibroService);
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

export default libroResolve;
