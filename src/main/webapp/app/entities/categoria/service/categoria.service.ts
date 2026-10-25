import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICategoria, NewCategoria } from '../categoria.model';

export type PartialUpdateCategoria = Partial<ICategoria> & Pick<ICategoria, 'id'>;

type RestOf<T extends ICategoria | NewCategoria> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestCategoria = RestOf<ICategoria>;

export type NewRestCategoria = RestOf<NewCategoria>;

export type PartialUpdateRestCategoria = RestOf<PartialUpdateCategoria>;

@Service()
export class CategoriasService {
  readonly categoriasParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly categoriasResource = httpResource<RestCategoria[]>(() => {
    const params = this.categoriasParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of categoria that have been fetched. It is updated when the categoriasResource emits a new value.
   * In case of error while fetching the categorias, the signal is set to an empty array.
   */
  readonly categorias = computed(() =>
    (this.categoriasResource.hasValue() ? this.categoriasResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/categorias`;

  protected convertValueFromServer(restCategoria: RestCategoria): ICategoria {
    return {
      ...restCategoria,
      fechaCreacion: restCategoria.fechaCreacion ? dayjs(restCategoria.fechaCreacion) : undefined,
      fechaModificacion: restCategoria.fechaModificacion ? dayjs(restCategoria.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class CategoriaService extends CategoriasService {
  protected readonly http = inject(HttpClient);

  create(categoria: NewCategoria): Observable<ICategoria> {
    const copy = this.convertValueFromClient(categoria);
    return this.http.post<RestCategoria>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(categoria: ICategoria): Observable<ICategoria> {
    const copy = this.convertValueFromClient(categoria);
    return this.http
      .put<RestCategoria>(`${this.resourceUrl}/${encodeURIComponent(this.getCategoriaIdentifier(categoria))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(categoria: PartialUpdateCategoria): Observable<ICategoria> {
    const copy = this.convertValueFromClient(categoria);
    return this.http
      .patch<RestCategoria>(`${this.resourceUrl}/${encodeURIComponent(this.getCategoriaIdentifier(categoria))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICategoria> {
    return this.http
      .get<RestCategoria>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICategoria[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCategoria[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCategoriaIdentifier(categoria: Pick<ICategoria, 'id'>): number {
    return categoria.id;
  }

  compareCategoria(o1: Pick<ICategoria, 'id'> | null, o2: Pick<ICategoria, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategoriaIdentifier(o1) === this.getCategoriaIdentifier(o2) : o1 === o2;
  }

  addCategoriaToCollectionIfMissing<Type extends Pick<ICategoria, 'id'>>(
    categoriaCollection: Type[],
    ...categoriasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categorias: Type[] = categoriasToCheck.filter(categoriaItem => categoriaItem !== null && categoriaItem !== undefined);
    if (categorias.length > 0) {
      const categoriaCollectionIdentifiers = categoriaCollection.map(categoriaItem => this.getCategoriaIdentifier(categoriaItem));
      const categoriasToAdd = categorias.filter(categoriaItem => {
        const categoriaIdentifier = this.getCategoriaIdentifier(categoriaItem);
        if (categoriaCollectionIdentifiers.includes(categoriaIdentifier)) {
          return false;
        }
        categoriaCollectionIdentifiers.push(categoriaIdentifier);
        return true;
      });
      return [...categoriasToAdd, ...categoriaCollection];
    }
    return categoriaCollection;
  }

  protected convertValueFromClient<T extends ICategoria | NewCategoria | PartialUpdateCategoria>(categoria: T): RestOf<T> {
    return {
      ...categoria,
      fechaCreacion: categoria.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: categoria.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCategoria): ICategoria {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCategoria[]): ICategoria[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
