import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICalificacionUsuario, NewCalificacionUsuario } from '../calificacion-usuario.model';

export type PartialUpdateCalificacionUsuario = Partial<ICalificacionUsuario> & Pick<ICalificacionUsuario, 'id'>;

type RestOf<T extends ICalificacionUsuario | NewCalificacionUsuario> = Omit<
  T,
  'fechaCalificacion' | 'fechaCreacion' | 'fechaModificacion'
> & {
  fechaCalificacion?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestCalificacionUsuario = RestOf<ICalificacionUsuario>;

export type NewRestCalificacionUsuario = RestOf<NewCalificacionUsuario>;

export type PartialUpdateRestCalificacionUsuario = RestOf<PartialUpdateCalificacionUsuario>;

@Service()
export class CalificacionUsuariosService {
  readonly calificacionUsuariosParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly calificacionUsuariosResource = httpResource<RestCalificacionUsuario[]>(() => {
    const params = this.calificacionUsuariosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of calificacionUsuario that have been fetched. It is updated when the calificacionUsuariosResource emits a new value.
   * In case of error while fetching the calificacionUsuarios, the signal is set to an empty array.
   */
  readonly calificacionUsuarios = computed(() =>
    (this.calificacionUsuariosResource.hasValue() ? this.calificacionUsuariosResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/calificacion-usuarios`;

  protected convertValueFromServer(restCalificacionUsuario: RestCalificacionUsuario): ICalificacionUsuario {
    return {
      ...restCalificacionUsuario,
      fechaCalificacion: restCalificacionUsuario.fechaCalificacion ? dayjs(restCalificacionUsuario.fechaCalificacion) : undefined,
      fechaCreacion: restCalificacionUsuario.fechaCreacion ? dayjs(restCalificacionUsuario.fechaCreacion) : undefined,
      fechaModificacion: restCalificacionUsuario.fechaModificacion ? dayjs(restCalificacionUsuario.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class CalificacionUsuarioService extends CalificacionUsuariosService {
  protected readonly http = inject(HttpClient);

  create(calificacionUsuario: NewCalificacionUsuario): Observable<ICalificacionUsuario> {
    const copy = this.convertValueFromClient(calificacionUsuario);
    return this.http.post<RestCalificacionUsuario>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(calificacionUsuario: ICalificacionUsuario): Observable<ICalificacionUsuario> {
    const copy = this.convertValueFromClient(calificacionUsuario);
    return this.http
      .put<RestCalificacionUsuario>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCalificacionUsuarioIdentifier(calificacionUsuario))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(calificacionUsuario: PartialUpdateCalificacionUsuario): Observable<ICalificacionUsuario> {
    const copy = this.convertValueFromClient(calificacionUsuario);
    return this.http
      .patch<RestCalificacionUsuario>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCalificacionUsuarioIdentifier(calificacionUsuario))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICalificacionUsuario> {
    return this.http
      .get<RestCalificacionUsuario>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICalificacionUsuario[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCalificacionUsuario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCalificacionUsuarioIdentifier(calificacionUsuario: Pick<ICalificacionUsuario, 'id'>): number {
    return calificacionUsuario.id;
  }

  compareCalificacionUsuario(o1: Pick<ICalificacionUsuario, 'id'> | null, o2: Pick<ICalificacionUsuario, 'id'> | null): boolean {
    return o1 && o2 ? this.getCalificacionUsuarioIdentifier(o1) === this.getCalificacionUsuarioIdentifier(o2) : o1 === o2;
  }

  addCalificacionUsuarioToCollectionIfMissing<Type extends Pick<ICalificacionUsuario, 'id'>>(
    calificacionUsuarioCollection: Type[],
    ...calificacionUsuariosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const calificacionUsuarios: Type[] = calificacionUsuariosToCheck.filter(
      calificacionUsuarioItem => calificacionUsuarioItem !== null && calificacionUsuarioItem !== undefined,
    );
    if (calificacionUsuarios.length > 0) {
      const calificacionUsuarioCollectionIdentifiers = calificacionUsuarioCollection.map(calificacionUsuarioItem =>
        this.getCalificacionUsuarioIdentifier(calificacionUsuarioItem),
      );
      const calificacionUsuariosToAdd = calificacionUsuarios.filter(calificacionUsuarioItem => {
        const calificacionUsuarioIdentifier = this.getCalificacionUsuarioIdentifier(calificacionUsuarioItem);
        if (calificacionUsuarioCollectionIdentifiers.includes(calificacionUsuarioIdentifier)) {
          return false;
        }
        calificacionUsuarioCollectionIdentifiers.push(calificacionUsuarioIdentifier);
        return true;
      });
      return [...calificacionUsuariosToAdd, ...calificacionUsuarioCollection];
    }
    return calificacionUsuarioCollection;
  }

  protected convertValueFromClient<T extends ICalificacionUsuario | NewCalificacionUsuario | PartialUpdateCalificacionUsuario>(
    calificacionUsuario: T,
  ): RestOf<T> {
    return {
      ...calificacionUsuario,
      fechaCalificacion: calificacionUsuario.fechaCalificacion?.toJSON() ?? null,
      fechaCreacion: calificacionUsuario.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: calificacionUsuario.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCalificacionUsuario): ICalificacionUsuario {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCalificacionUsuario[]): ICalificacionUsuario[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
