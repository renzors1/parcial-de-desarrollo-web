import dayjs from 'dayjs/esm';

import { IAutor } from 'app/entities/autor/autor.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { IEditorial } from 'app/entities/editorial/editorial.model';
import { IPromocion } from 'app/entities/promocion/promocion.model';

export interface ILibro {
  id: number;
  isbn?: string | null;
  titulo?: string | null;
  sinopsis?: string | null;
  urlPortada?: string | null;
  fechaPublicacion?: dayjs.Dayjs | null;
  precio?: number | null;
  idioma?: string | null;
  activo?: boolean | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  categoriases?: Pick<ICategoria, 'id'>[] | null;
  autoreses?: Pick<IAutor, 'id'>[] | null;
  editorial?: Pick<IEditorial, 'id'> | null;
  promocioneses?: Pick<IPromocion, 'id'>[] | null;
}

export type NewLibro = Omit<ILibro, 'id'> & { id: null };
