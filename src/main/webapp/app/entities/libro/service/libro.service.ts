import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ILibro, NewLibro } from '../libro.model';

export type PartialUpdateLibro = Partial<ILibro> & Pick<ILibro, 'id'>;

type RestOf<T extends ILibro | NewLibro> = Omit<T, 'fechaPublicacion' | 'fechaCreacion' | 'fechaModificacion'> & {
  fechaPublicacion?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestLibro = RestOf<ILibro>;

export type NewRestLibro = RestOf<NewLibro>;

export type PartialUpdateRestLibro = RestOf<PartialUpdateLibro>;

@Service()
export class LibrosService {
  readonly librosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly librosResource = httpResource<RestLibro[]>(() => {
    const params = this.librosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of libro that have been fetched. It is updated when the librosResource emits a new value.
   * In case of error while fetching the libros, the signal is set to an empty array.
   */
  readonly libros = computed(() =>
    (this.librosResource.hasValue() ? this.librosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/libros`;

  protected convertValueFromServer(restLibro: RestLibro): ILibro {
    return {
      ...restLibro,
      fechaPublicacion: restLibro.fechaPublicacion ? dayjs(restLibro.fechaPublicacion) : undefined,
      fechaCreacion: restLibro.fechaCreacion ? dayjs(restLibro.fechaCreacion) : undefined,
      fechaModificacion: restLibro.fechaModificacion ? dayjs(restLibro.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class LibroService extends LibrosService {
  protected readonly http = inject(HttpClient);

  create(libro: NewLibro): Observable<ILibro> {
    const copy = this.convertValueFromClient(libro);
    return this.http.post<RestLibro>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(libro: ILibro): Observable<ILibro> {
    const copy = this.convertValueFromClient(libro);
    return this.http
      .put<RestLibro>(`${this.resourceUrl}/${encodeURIComponent(this.getLibroIdentifier(libro))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(libro: PartialUpdateLibro): Observable<ILibro> {
    const copy = this.convertValueFromClient(libro);
    return this.http
      .patch<RestLibro>(`${this.resourceUrl}/${encodeURIComponent(this.getLibroIdentifier(libro))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ILibro> {
    return this.http.get<RestLibro>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ILibro[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLibro[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getLibroIdentifier(libro: Pick<ILibro, 'id'>): number {
    return libro.id;
  }

  compareLibro(o1: Pick<ILibro, 'id'> | null, o2: Pick<ILibro, 'id'> | null): boolean {
    return o1 && o2 ? this.getLibroIdentifier(o1) === this.getLibroIdentifier(o2) : o1 === o2;
  }

  addLibroToCollectionIfMissing<Type extends Pick<ILibro, 'id'>>(
    libroCollection: Type[],
    ...librosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const libros: Type[] = librosToCheck.filter(libroItem => libroItem !== null && libroItem !== undefined);
    if (libros.length > 0) {
      const libroCollectionIdentifiers = libroCollection.map(libroItem => this.getLibroIdentifier(libroItem));
      const librosToAdd = libros.filter(libroItem => {
        const libroIdentifier = this.getLibroIdentifier(libroItem);
        if (libroCollectionIdentifiers.includes(libroIdentifier)) {
          return false;
        }
        libroCollectionIdentifiers.push(libroIdentifier);
        return true;
      });
      return [...librosToAdd, ...libroCollection];
    }
    return libroCollection;
  }

  protected convertValueFromClient<T extends ILibro | NewLibro | PartialUpdateLibro>(libro: T): RestOf<T> {
    return {
      ...libro,
      fechaPublicacion: libro.fechaPublicacion?.format(DATE_FORMAT) ?? null,
      fechaCreacion: libro.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: libro.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestLibro): ILibro {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestLibro[]): ILibro[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
