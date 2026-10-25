import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IEditorial, NewEditorial } from '../editorial.model';

export type PartialUpdateEditorial = Partial<IEditorial> & Pick<IEditorial, 'id'>;

type RestOf<T extends IEditorial | NewEditorial> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestEditorial = RestOf<IEditorial>;

export type NewRestEditorial = RestOf<NewEditorial>;

export type PartialUpdateRestEditorial = RestOf<PartialUpdateEditorial>;

@Service()
export class EditorialsService {
  readonly editorialsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly editorialsResource = httpResource<RestEditorial[]>(() => {
    const params = this.editorialsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of editorial that have been fetched. It is updated when the editorialsResource emits a new value.
   * In case of error while fetching the editorials, the signal is set to an empty array.
   */
  readonly editorials = computed(() =>
    (this.editorialsResource.hasValue() ? this.editorialsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/editorials`;

  protected convertValueFromServer(restEditorial: RestEditorial): IEditorial {
    return {
      ...restEditorial,
      fechaCreacion: restEditorial.fechaCreacion ? dayjs(restEditorial.fechaCreacion) : undefined,
      fechaModificacion: restEditorial.fechaModificacion ? dayjs(restEditorial.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class EditorialService extends EditorialsService {
  protected readonly http = inject(HttpClient);

  create(editorial: NewEditorial): Observable<IEditorial> {
    const copy = this.convertValueFromClient(editorial);
    return this.http.post<RestEditorial>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(editorial: IEditorial): Observable<IEditorial> {
    const copy = this.convertValueFromClient(editorial);
    return this.http
      .put<RestEditorial>(`${this.resourceUrl}/${encodeURIComponent(this.getEditorialIdentifier(editorial))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(editorial: PartialUpdateEditorial): Observable<IEditorial> {
    const copy = this.convertValueFromClient(editorial);
    return this.http
      .patch<RestEditorial>(`${this.resourceUrl}/${encodeURIComponent(this.getEditorialIdentifier(editorial))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IEditorial> {
    return this.http
      .get<RestEditorial>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IEditorial[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEditorial[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEditorialIdentifier(editorial: Pick<IEditorial, 'id'>): number {
    return editorial.id;
  }

  compareEditorial(o1: Pick<IEditorial, 'id'> | null, o2: Pick<IEditorial, 'id'> | null): boolean {
    return o1 && o2 ? this.getEditorialIdentifier(o1) === this.getEditorialIdentifier(o2) : o1 === o2;
  }

  addEditorialToCollectionIfMissing<Type extends Pick<IEditorial, 'id'>>(
    editorialCollection: Type[],
    ...editorialsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const editorials: Type[] = editorialsToCheck.filter(editorialItem => editorialItem !== null && editorialItem !== undefined);
    if (editorials.length > 0) {
      const editorialCollectionIdentifiers = editorialCollection.map(editorialItem => this.getEditorialIdentifier(editorialItem));
      const editorialsToAdd = editorials.filter(editorialItem => {
        const editorialIdentifier = this.getEditorialIdentifier(editorialItem);
        if (editorialCollectionIdentifiers.includes(editorialIdentifier)) {
          return false;
        }
        editorialCollectionIdentifiers.push(editorialIdentifier);
        return true;
      });
      return [...editorialsToAdd, ...editorialCollection];
    }
    return editorialCollection;
  }

  protected convertValueFromClient<T extends IEditorial | NewEditorial | PartialUpdateEditorial>(editorial: T): RestOf<T> {
    return {
      ...editorial,
      fechaCreacion: editorial.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: editorial.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestEditorial): IEditorial {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestEditorial[]): IEditorial[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
