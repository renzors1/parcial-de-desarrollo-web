import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPromocion, NewPromocion } from '../promocion.model';

export type PartialUpdatePromocion = Partial<IPromocion> & Pick<IPromocion, 'id'>;

type RestOf<T extends IPromocion | NewPromocion> = Omit<T, 'fechaInicio' | 'fechaFin' | 'fechaCreacion' | 'fechaModificacion'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestPromocion = RestOf<IPromocion>;

export type NewRestPromocion = RestOf<NewPromocion>;

export type PartialUpdateRestPromocion = RestOf<PartialUpdatePromocion>;

@Service()
export class PromocionsService {
  readonly promocionsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly promocionsResource = httpResource<RestPromocion[]>(() => {
    const params = this.promocionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of promocion that have been fetched. It is updated when the promocionsResource emits a new value.
   * In case of error while fetching the promocions, the signal is set to an empty array.
   */
  readonly promocions = computed(() =>
    (this.promocionsResource.hasValue() ? this.promocionsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/promocions`;

  protected convertValueFromServer(restPromocion: RestPromocion): IPromocion {
    return {
      ...restPromocion,
      fechaInicio: restPromocion.fechaInicio ? dayjs(restPromocion.fechaInicio) : undefined,
      fechaFin: restPromocion.fechaFin ? dayjs(restPromocion.fechaFin) : undefined,
      fechaCreacion: restPromocion.fechaCreacion ? dayjs(restPromocion.fechaCreacion) : undefined,
      fechaModificacion: restPromocion.fechaModificacion ? dayjs(restPromocion.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class PromocionService extends PromocionsService {
  protected readonly http = inject(HttpClient);

  create(promocion: NewPromocion): Observable<IPromocion> {
    const copy = this.convertValueFromClient(promocion);
    return this.http.post<RestPromocion>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(promocion: IPromocion): Observable<IPromocion> {
    const copy = this.convertValueFromClient(promocion);
    return this.http
      .put<RestPromocion>(`${this.resourceUrl}/${encodeURIComponent(this.getPromocionIdentifier(promocion))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(promocion: PartialUpdatePromocion): Observable<IPromocion> {
    const copy = this.convertValueFromClient(promocion);
    return this.http
      .patch<RestPromocion>(`${this.resourceUrl}/${encodeURIComponent(this.getPromocionIdentifier(promocion))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPromocion> {
    return this.http
      .get<RestPromocion>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPromocion[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPromocion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPromocionIdentifier(promocion: Pick<IPromocion, 'id'>): number {
    return promocion.id;
  }

  comparePromocion(o1: Pick<IPromocion, 'id'> | null, o2: Pick<IPromocion, 'id'> | null): boolean {
    return o1 && o2 ? this.getPromocionIdentifier(o1) === this.getPromocionIdentifier(o2) : o1 === o2;
  }

  addPromocionToCollectionIfMissing<Type extends Pick<IPromocion, 'id'>>(
    promocionCollection: Type[],
    ...promocionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const promocions: Type[] = promocionsToCheck.filter(promocionItem => promocionItem !== null && promocionItem !== undefined);
    if (promocions.length > 0) {
      const promocionCollectionIdentifiers = promocionCollection.map(promocionItem => this.getPromocionIdentifier(promocionItem));
      const promocionsToAdd = promocions.filter(promocionItem => {
        const promocionIdentifier = this.getPromocionIdentifier(promocionItem);
        if (promocionCollectionIdentifiers.includes(promocionIdentifier)) {
          return false;
        }
        promocionCollectionIdentifiers.push(promocionIdentifier);
        return true;
      });
      return [...promocionsToAdd, ...promocionCollection];
    }
    return promocionCollection;
  }

  protected convertValueFromClient<T extends IPromocion | NewPromocion | PartialUpdatePromocion>(promocion: T): RestOf<T> {
    return {
      ...promocion,
      fechaInicio: promocion.fechaInicio?.toJSON() ?? null,
      fechaFin: promocion.fechaFin?.toJSON() ?? null,
      fechaCreacion: promocion.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: promocion.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPromocion): IPromocion {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPromocion[]): IPromocion[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
