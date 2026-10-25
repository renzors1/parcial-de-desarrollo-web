import dayjs from 'dayjs/esm';

import { ILibro } from 'app/entities/libro/libro.model';

export interface IAutor {
  id: number;
  nombres?: string | null;
  apellidos?: string | null;
  nacionalidad?: string | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  libroses?: Pick<ILibro, 'id'>[] | null;
}

export type NewAutor = Omit<IAutor, 'id'> & { id: null };
