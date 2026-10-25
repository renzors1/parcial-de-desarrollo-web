import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IVenta, NewVenta } from '../venta.model';

export type PartialUpdateVenta = Partial<IVenta> & Pick<IVenta, 'id'>;

type RestOf<T extends IVenta | NewVenta> = Omit<T, 'fechaVenta' | 'fechaCreacion' | 'fechaModificacion'> & {
  fechaVenta?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestVenta = RestOf<IVenta>;

export type NewRestVenta = RestOf<NewVenta>;

export type PartialUpdateRestVenta = RestOf<PartialUpdateVenta>;

@Service()
export class VentasService {
  readonly ventasParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly ventasResource = httpResource<RestVenta[]>(() => {
    const params = this.ventasParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of venta that have been fetched. It is updated when the ventasResource emits a new value.
   * In case of error while fetching the ventas, the signal is set to an empty array.
   */
  readonly ventas = computed(() =>
    (this.ventasResource.hasValue() ? this.ventasResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/ventas`;

  protected convertValueFromServer(restVenta: RestVenta): IVenta {
    return {
      ...restVenta,
      fechaVenta: restVenta.fechaVenta ? dayjs(restVenta.fechaVenta) : undefined,
      fechaCreacion: restVenta.fechaCreacion ? dayjs(restVenta.fechaCreacion) : undefined,
      fechaModificacion: restVenta.fechaModificacion ? dayjs(restVenta.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class VentaService extends VentasService {
  protected readonly http = inject(HttpClient);

  create(venta: NewVenta): Observable<IVenta> {
    const copy = this.convertValueFromClient(venta);
    return this.http.post<RestVenta>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(venta: IVenta): Observable<IVenta> {
    const copy = this.convertValueFromClient(venta);
    return this.http
      .put<RestVenta>(`${this.resourceUrl}/${encodeURIComponent(this.getVentaIdentifier(venta))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(venta: PartialUpdateVenta): Observable<IVenta> {
    const copy = this.convertValueFromClient(venta);
    return this.http
      .patch<RestVenta>(`${this.resourceUrl}/${encodeURIComponent(this.getVentaIdentifier(venta))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IVenta> {
    return this.http.get<RestVenta>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IVenta[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVenta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getVentaIdentifier(venta: Pick<IVenta, 'id'>): number {
    return venta.id;
  }

  compareVenta(o1: Pick<IVenta, 'id'> | null, o2: Pick<IVenta, 'id'> | null): boolean {
    return o1 && o2 ? this.getVentaIdentifier(o1) === this.getVentaIdentifier(o2) : o1 === o2;
  }

  addVentaToCollectionIfMissing<Type extends Pick<IVenta, 'id'>>(
    ventaCollection: Type[],
    ...ventasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ventas: Type[] = ventasToCheck.filter(ventaItem => ventaItem !== null && ventaItem !== undefined);
    if (ventas.length > 0) {
      const ventaCollectionIdentifiers = ventaCollection.map(ventaItem => this.getVentaIdentifier(ventaItem));
      const ventasToAdd = ventas.filter(ventaItem => {
        const ventaIdentifier = this.getVentaIdentifier(ventaItem);
        if (ventaCollectionIdentifiers.includes(ventaIdentifier)) {
          return false;
        }
        ventaCollectionIdentifiers.push(ventaIdentifier);
        return true;
      });
      return [...ventasToAdd, ...ventaCollection];
    }
    return ventaCollection;
  }

  protected convertValueFromClient<T extends IVenta | NewVenta | PartialUpdateVenta>(venta: T): RestOf<T> {
    return {
      ...venta,
      fechaVenta: venta.fechaVenta?.toJSON() ?? null,
      fechaCreacion: venta.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: venta.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestVenta): IVenta {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestVenta[]): IVenta[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
