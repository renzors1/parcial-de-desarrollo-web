import dayjs from 'dayjs/esm';

export interface IEditorial {
  id: number;
  nombreEditorial?: string | null;
  pais?: string | null;
  activo?: boolean | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
}

export type NewEditorial = Omit<IEditorial, 'id'> & { id: null };
