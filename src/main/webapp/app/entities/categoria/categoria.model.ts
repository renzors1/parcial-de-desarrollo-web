import dayjs from 'dayjs/esm';

import { ILibro } from 'app/entities/libro/libro.model';

export interface ICategoria {
  id: number;
  nombreCategoria?: string | null;
  activo?: boolean | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  libroses?: Pick<ILibro, 'id'>[] | null;
}

export type NewCategoria = Omit<ICategoria, 'id'> & { id: null };
