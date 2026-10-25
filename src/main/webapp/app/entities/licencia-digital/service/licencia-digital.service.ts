import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ILicenciaDigital, NewLicenciaDigital } from '../licencia-digital.model';

export type PartialUpdateLicenciaDigital = Partial<ILicenciaDigital> & Pick<ILicenciaDigital, 'id'>;

type RestOf<T extends ILicenciaDigital | NewLicenciaDigital> = Omit<T, 'fechaAsignacion' | 'fechaCreacion' | 'fechaModificacion'> & {
  fechaAsignacion?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestLicenciaDigital = RestOf<ILicenciaDigital>;

export type NewRestLicenciaDigital = RestOf<NewLicenciaDigital>;

export type PartialUpdateRestLicenciaDigital = RestOf<PartialUpdateLicenciaDigital>;

@Service()
export class LicenciaDigitalsService {
  readonly licenciaDigitalsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly licenciaDigitalsResource = httpResource<RestLicenciaDigital[]>(() => {
    const params = this.licenciaDigitalsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of licenciaDigital that have been fetched. It is updated when the licenciaDigitalsResource emits a new value.
   * In case of error while fetching the licenciaDigitals, the signal is set to an empty array.
   */
  readonly licenciaDigitals = computed(() =>
    (this.licenciaDigitalsResource.hasValue() ? this.licenciaDigitalsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/licencia-digitals`;

  protected convertValueFromServer(restLicenciaDigital: RestLicenciaDigital): ILicenciaDigital {
    return {
      ...restLicenciaDigital,
      fechaAsignacion: restLicenciaDigital.fechaAsignacion ? dayjs(restLicenciaDigital.fechaAsignacion) : undefined,
      fechaCreacion: restLicenciaDigital.fechaCreacion ? dayjs(restLicenciaDigital.fechaCreacion) : undefined,
      fechaModificacion: restLicenciaDigital.fechaModificacion ? dayjs(restLicenciaDigital.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class LicenciaDigitalService extends LicenciaDigitalsService {
  protected readonly http = inject(HttpClient);

  create(licenciaDigital: NewLicenciaDigital): Observable<ILicenciaDigital> {
    const copy = this.convertValueFromClient(licenciaDigital);
    return this.http.post<RestLicenciaDigital>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(licenciaDigital: ILicenciaDigital): Observable<ILicenciaDigital> {
    const copy = this.convertValueFromClient(licenciaDigital);
    return this.http
      .put<RestLicenciaDigital>(`${this.resourceUrl}/${encodeURIComponent(this.getLicenciaDigitalIdentifier(licenciaDigital))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(licenciaDigital: PartialUpdateLicenciaDigital): Observable<ILicenciaDigital> {
    const copy = this.convertValueFromClient(licenciaDigital);
    return this.http
      .patch<RestLicenciaDigital>(`${this.resourceUrl}/${encodeURIComponent(this.getLicenciaDigitalIdentifier(licenciaDigital))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ILicenciaDigital> {
    return this.http
      .get<RestLicenciaDigital>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ILicenciaDigital[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLicenciaDigital[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getLicenciaDigitalIdentifier(licenciaDigital: Pick<ILicenciaDigital, 'id'>): number {
    return licenciaDigital.id;
  }

  compareLicenciaDigital(o1: Pick<ILicenciaDigital, 'id'> | null, o2: Pick<ILicenciaDigital, 'id'> | null): boolean {
    return o1 && o2 ? this.getLicenciaDigitalIdentifier(o1) === this.getLicenciaDigitalIdentifier(o2) : o1 === o2;
  }

  addLicenciaDigitalToCollectionIfMissing<Type extends Pick<ILicenciaDigital, 'id'>>(
    licenciaDigitalCollection: Type[],
    ...licenciaDigitalsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const licenciaDigitals: Type[] = licenciaDigitalsToCheck.filter(
      licenciaDigitalItem => licenciaDigitalItem !== null && licenciaDigitalItem !== undefined,
    );
    if (licenciaDigitals.length > 0) {
      const licenciaDigitalCollectionIdentifiers = licenciaDigitalCollection.map(licenciaDigitalItem =>
        this.getLicenciaDigitalIdentifier(licenciaDigitalItem),
      );
      const licenciaDigitalsToAdd = licenciaDigitals.filter(licenciaDigitalItem => {
        const licenciaDigitalIdentifier = this.getLicenciaDigitalIdentifier(licenciaDigitalItem);
        if (licenciaDigitalCollectionIdentifiers.includes(licenciaDigitalIdentifier)) {
          return false;
        }
        licenciaDigitalCollectionIdentifiers.push(licenciaDigitalIdentifier);
        return true;
      });
      return [...licenciaDigitalsToAdd, ...licenciaDigitalCollection];
    }
    return licenciaDigitalCollection;
  }

  protected convertValueFromClient<T extends ILicenciaDigital | NewLicenciaDigital | PartialUpdateLicenciaDigital>(
    licenciaDigital: T,
  ): RestOf<T> {
    return {
      ...licenciaDigital,
      fechaAsignacion: licenciaDigital.fechaAsignacion?.toJSON() ?? null,
      fechaCreacion: licenciaDigital.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: licenciaDigital.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestLicenciaDigital): ILicenciaDigital {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestLicenciaDigital[]): ILicenciaDigital[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
