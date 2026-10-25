import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IRol, NewRol } from '../rol.model';

export type PartialUpdateRol = Partial<IRol> & Pick<IRol, 'id'>;

type RestOf<T extends IRol | NewRol> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestRol = RestOf<IRol>;

export type NewRestRol = RestOf<NewRol>;

export type PartialUpdateRestRol = RestOf<PartialUpdateRol>;

@Service()
export class RolsService {
  readonly rolsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly rolsResource = httpResource<RestRol[]>(() => {
    const params = this.rolsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of rol that have been fetched. It is updated when the rolsResource emits a new value.
   * In case of error while fetching the rols, the signal is set to an empty array.
   */
  readonly rols = computed(() =>
    (this.rolsResource.hasValue() ? this.rolsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/rols`;

  protected convertValueFromServer(restRol: RestRol): IRol {
    return {
      ...restRol,
      fechaCreacion: restRol.fechaCreacion ? dayjs(restRol.fechaCreacion) : undefined,
      fechaModificacion: restRol.fechaModificacion ? dayjs(restRol.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class RolService extends RolsService {
  protected readonly http = inject(HttpClient);

  create(rol: NewRol): Observable<IRol> {
    const copy = this.convertValueFromClient(rol);
    return this.http.post<RestRol>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(rol: IRol): Observable<IRol> {
    const copy = this.convertValueFromClient(rol);
    return this.http
      .put<RestRol>(`${this.resourceUrl}/${encodeURIComponent(this.getRolIdentifier(rol))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(rol: PartialUpdateRol): Observable<IRol> {
    const copy = this.convertValueFromClient(rol);
    return this.http
      .patch<RestRol>(`${this.resourceUrl}/${encodeURIComponent(this.getRolIdentifier(rol))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IRol> {
    return this.http.get<RestRol>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IRol[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRol[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getRolIdentifier(rol: Pick<IRol, 'id'>): number {
    return rol.id;
  }

  compareRol(o1: Pick<IRol, 'id'> | null, o2: Pick<IRol, 'id'> | null): boolean {
    return o1 && o2 ? this.getRolIdentifier(o1) === this.getRolIdentifier(o2) : o1 === o2;
  }

  addRolToCollectionIfMissing<Type extends Pick<IRol, 'id'>>(rolCollection: Type[], ...rolsToCheck: (Type | null | undefined)[]): Type[] {
    const rols: Type[] = rolsToCheck.filter(rolItem => rolItem !== null && rolItem !== undefined);
    if (rols.length > 0) {
      const rolCollectionIdentifiers = rolCollection.map(rolItem => this.getRolIdentifier(rolItem));
      const rolsToAdd = rols.filter(rolItem => {
        const rolIdentifier = this.getRolIdentifier(rolItem);
        if (rolCollectionIdentifiers.includes(rolIdentifier)) {
          return false;
        }
        rolCollectionIdentifiers.push(rolIdentifier);
        return true;
      });
      return [...rolsToAdd, ...rolCollection];
    }
    return rolCollection;
  }

  protected convertValueFromClient<T extends IRol | NewRol | PartialUpdateRol>(rol: T): RestOf<T> {
    return {
      ...rol,
      fechaCreacion: rol.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: rol.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestRol): IRol {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestRol[]): IRol[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
