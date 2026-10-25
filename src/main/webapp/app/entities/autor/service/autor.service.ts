import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IAutor, NewAutor } from '../autor.model';

export type PartialUpdateAutor = Partial<IAutor> & Pick<IAutor, 'id'>;

type RestOf<T extends IAutor | NewAutor> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestAutor = RestOf<IAutor>;

export type NewRestAutor = RestOf<NewAutor>;

export type PartialUpdateRestAutor = RestOf<PartialUpdateAutor>;

@Service()
export class AutorsService {
  readonly autorsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly autorsResource = httpResource<RestAutor[]>(() => {
    const params = this.autorsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of autor that have been fetched. It is updated when the autorsResource emits a new value.
   * In case of error while fetching the autors, the signal is set to an empty array.
   */
  readonly autors = computed(() =>
    (this.autorsResource.hasValue() ? this.autorsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/autors`;

  protected convertValueFromServer(restAutor: RestAutor): IAutor {
    return {
      ...restAutor,
      fechaCreacion: restAutor.fechaCreacion ? dayjs(restAutor.fechaCreacion) : undefined,
      fechaModificacion: restAutor.fechaModificacion ? dayjs(restAutor.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class AutorService extends AutorsService {
  protected readonly http = inject(HttpClient);

  create(autor: NewAutor): Observable<IAutor> {
    const copy = this.convertValueFromClient(autor);
    return this.http.post<RestAutor>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(autor: IAutor): Observable<IAutor> {
    const copy = this.convertValueFromClient(autor);
    return this.http
      .put<RestAutor>(`${this.resourceUrl}/${encodeURIComponent(this.getAutorIdentifier(autor))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(autor: PartialUpdateAutor): Observable<IAutor> {
    const copy = this.convertValueFromClient(autor);
    return this.http
      .patch<RestAutor>(`${this.resourceUrl}/${encodeURIComponent(this.getAutorIdentifier(autor))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IAutor> {
    return this.http.get<RestAutor>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IAutor[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAutor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getAutorIdentifier(autor: Pick<IAutor, 'id'>): number {
    return autor.id;
  }

  compareAutor(o1: Pick<IAutor, 'id'> | null, o2: Pick<IAutor, 'id'> | null): boolean {
    return o1 && o2 ? this.getAutorIdentifier(o1) === this.getAutorIdentifier(o2) : o1 === o2;
  }

  addAutorToCollectionIfMissing<Type extends Pick<IAutor, 'id'>>(
    autorCollection: Type[],
    ...autorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const autors: Type[] = autorsToCheck.filter(autorItem => autorItem !== null && autorItem !== undefined);
    if (autors.length > 0) {
      const autorCollectionIdentifiers = autorCollection.map(autorItem => this.getAutorIdentifier(autorItem));
      const autorsToAdd = autors.filter(autorItem => {
        const autorIdentifier = this.getAutorIdentifier(autorItem);
        if (autorCollectionIdentifiers.includes(autorIdentifier)) {
          return false;
        }
        autorCollectionIdentifiers.push(autorIdentifier);
        return true;
      });
      return [...autorsToAdd, ...autorCollection];
    }
    return autorCollection;
  }

  protected convertValueFromClient<T extends IAutor | NewAutor | PartialUpdateAutor>(autor: T): RestOf<T> {
    return {
      ...autor,
      fechaCreacion: autor.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: autor.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestAutor): IAutor {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestAutor[]): IAutor[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
