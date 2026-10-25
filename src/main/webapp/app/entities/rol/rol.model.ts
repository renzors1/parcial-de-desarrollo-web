import dayjs from 'dayjs/esm';

export interface IRol {
  id: number;
  codigoIdentificador?: number | null;
  nombreRol?: string | null;
  descripcion?: string | null;
  activo?: boolean | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
}

export type NewRol = Omit<IRol, 'id'> & { id: null };
