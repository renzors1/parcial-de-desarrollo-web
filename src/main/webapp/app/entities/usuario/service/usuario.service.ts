import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IUsuario, NewUsuario } from '../usuario.model';

export type PartialUpdateUsuario = Partial<IUsuario> & Pick<IUsuario, 'id'>;

type RestOf<T extends IUsuario | NewUsuario> = Omit<T, 'fechaRegistro' | 'fechaCreacion' | 'fechaModificacion'> & {
  fechaRegistro?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestUsuario = RestOf<IUsuario>;

export type NewRestUsuario = RestOf<NewUsuario>;

export type PartialUpdateRestUsuario = RestOf<PartialUpdateUsuario>;

@Service()
export class UsuariosService {
  readonly usuariosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly usuariosResource = httpResource<RestUsuario[]>(() => {
    const params = this.usuariosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of usuario that have been fetched. It is updated when the usuariosResource emits a new value.
   * In case of error while fetching the usuarios, the signal is set to an empty array.
   */
  readonly usuarios = computed(() =>
    (this.usuariosResource.hasValue() ? this.usuariosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/usuarios`;

  protected convertValueFromServer(restUsuario: RestUsuario): IUsuario {
    return {
      ...restUsuario,
      fechaRegistro: restUsuario.fechaRegistro ? dayjs(restUsuario.fechaRegistro) : undefined,
      fechaCreacion: restUsuario.fechaCreacion ? dayjs(restUsuario.fechaCreacion) : undefined,
      fechaModificacion: restUsuario.fechaModificacion ? dayjs(restUsuario.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class UsuarioService extends UsuariosService {
  protected readonly http = inject(HttpClient);

  create(usuario: NewUsuario): Observable<IUsuario> {
    const copy = this.convertValueFromClient(usuario);
    return this.http.post<RestUsuario>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(usuario: IUsuario): Observable<IUsuario> {
    const copy = this.convertValueFromClient(usuario);
    return this.http
      .put<RestUsuario>(`${this.resourceUrl}/${encodeURIComponent(this.getUsuarioIdentifier(usuario))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(usuario: PartialUpdateUsuario): Observable<IUsuario> {
    const copy = this.convertValueFromClient(usuario);
    return this.http
      .patch<RestUsuario>(`${this.resourceUrl}/${encodeURIComponent(this.getUsuarioIdentifier(usuario))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IUsuario> {
    return this.http
      .get<RestUsuario>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IUsuario[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUsuario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getUsuarioIdentifier(usuario: Pick<IUsuario, 'id'>): number {
    return usuario.id;
  }

  compareUsuario(o1: Pick<IUsuario, 'id'> | null, o2: Pick<IUsuario, 'id'> | null): boolean {
    return o1 && o2 ? this.getUsuarioIdentifier(o1) === this.getUsuarioIdentifier(o2) : o1 === o2;
  }

  addUsuarioToCollectionIfMissing<Type extends Pick<IUsuario, 'id'>>(
    usuarioCollection: Type[],
    ...usuariosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const usuarios: Type[] = usuariosToCheck.filter(usuarioItem => usuarioItem !== null && usuarioItem !== undefined);
    if (usuarios.length > 0) {
      const usuarioCollectionIdentifiers = usuarioCollection.map(usuarioItem => this.getUsuarioIdentifier(usuarioItem));
      const usuariosToAdd = usuarios.filter(usuarioItem => {
        const usuarioIdentifier = this.getUsuarioIdentifier(usuarioItem);
        if (usuarioCollectionIdentifiers.includes(usuarioIdentifier)) {
          return false;
        }
        usuarioCollectionIdentifiers.push(usuarioIdentifier);
        return true;
      });
      return [...usuariosToAdd, ...usuarioCollection];
    }
    return usuarioCollection;
  }

  protected convertValueFromClient<T extends IUsuario | NewUsuario | PartialUpdateUsuario>(usuario: T): RestOf<T> {
    return {
      ...usuario,
      fechaRegistro: usuario.fechaRegistro?.toJSON() ?? null,
      fechaCreacion: usuario.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: usuario.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestUsuario): IUsuario {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestUsuario[]): IUsuario[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
