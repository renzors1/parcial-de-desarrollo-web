import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IDetalleVenta, NewDetalleVenta } from '../detalle-venta.model';

export type PartialUpdateDetalleVenta = Partial<IDetalleVenta> & Pick<IDetalleVenta, 'id'>;

type RestOf<T extends IDetalleVenta | NewDetalleVenta> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestDetalleVenta = RestOf<IDetalleVenta>;

export type NewRestDetalleVenta = RestOf<NewDetalleVenta>;

export type PartialUpdateRestDetalleVenta = RestOf<PartialUpdateDetalleVenta>;

@Service()
export class DetalleVentasService {
  readonly detalleVentasParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly detalleVentasResource = httpResource<RestDetalleVenta[]>(() => {
    const params = this.detalleVentasParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of detalleVenta that have been fetched. It is updated when the detalleVentasResource emits a new value.
   * In case of error while fetching the detalleVentas, the signal is set to an empty array.
   */
  readonly detalleVentas = computed(() =>
    (this.detalleVentasResource.hasValue() ? this.detalleVentasResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/detalle-ventas`;

  protected convertValueFromServer(restDetalleVenta: RestDetalleVenta): IDetalleVenta {
    return {
      ...restDetalleVenta,
      fechaCreacion: restDetalleVenta.fechaCreacion ? dayjs(restDetalleVenta.fechaCreacion) : undefined,
      fechaModificacion: restDetalleVenta.fechaModificacion ? dayjs(restDetalleVenta.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class DetalleVentaService extends DetalleVentasService {
  protected readonly http = inject(HttpClient);

  create(detalleVenta: NewDetalleVenta): Observable<IDetalleVenta> {
    const copy = this.convertValueFromClient(detalleVenta);
    return this.http.post<RestDetalleVenta>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(detalleVenta: IDetalleVenta): Observable<IDetalleVenta> {
    const copy = this.convertValueFromClient(detalleVenta);
    return this.http
      .put<RestDetalleVenta>(`${this.resourceUrl}/${encodeURIComponent(this.getDetalleVentaIdentifier(detalleVenta))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(detalleVenta: PartialUpdateDetalleVenta): Observable<IDetalleVenta> {
    const copy = this.convertValueFromClient(detalleVenta);
    return this.http
      .patch<RestDetalleVenta>(`${this.resourceUrl}/${encodeURIComponent(this.getDetalleVentaIdentifier(detalleVenta))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IDetalleVenta> {
    return this.http
      .get<RestDetalleVenta>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IDetalleVenta[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDetalleVenta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getDetalleVentaIdentifier(detalleVenta: Pick<IDetalleVenta, 'id'>): number {
    return detalleVenta.id;
  }

  compareDetalleVenta(o1: Pick<IDetalleVenta, 'id'> | null, o2: Pick<IDetalleVenta, 'id'> | null): boolean {
    return o1 && o2 ? this.getDetalleVentaIdentifier(o1) === this.getDetalleVentaIdentifier(o2) : o1 === o2;
  }

  addDetalleVentaToCollectionIfMissing<Type extends Pick<IDetalleVenta, 'id'>>(
    detalleVentaCollection: Type[],
    ...detalleVentasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const detalleVentas: Type[] = detalleVentasToCheck.filter(
      detalleVentaItem => detalleVentaItem !== null && detalleVentaItem !== undefined,
    );
    if (detalleVentas.length > 0) {
      const detalleVentaCollectionIdentifiers = detalleVentaCollection.map(detalleVentaItem =>
        this.getDetalleVentaIdentifier(detalleVentaItem),
      );
      const detalleVentasToAdd = detalleVentas.filter(detalleVentaItem => {
        const detalleVentaIdentifier = this.getDetalleVentaIdentifier(detalleVentaItem);
        if (detalleVentaCollectionIdentifiers.includes(detalleVentaIdentifier)) {
          return false;
        }
        detalleVentaCollectionIdentifiers.push(detalleVentaIdentifier);
        return true;
      });
      return [...detalleVentasToAdd, ...detalleVentaCollection];
    }
    return detalleVentaCollection;
  }

  protected convertValueFromClient<T extends IDetalleVenta | NewDetalleVenta | PartialUpdateDetalleVenta>(detalleVenta: T): RestOf<T> {
    return {
      ...detalleVenta,
      fechaCreacion: detalleVenta.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: detalleVenta.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestDetalleVenta): IDetalleVenta {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestDetalleVenta[]): IDetalleVenta[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
