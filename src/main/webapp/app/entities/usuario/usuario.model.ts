import dayjs from 'dayjs/esm';

import { IRol } from 'app/entities/rol/rol.model';

export interface IUsuario {
  id: number;
  nombre?: string | null;
  apellido?: string | null;
  email?: string | null;
  contrasena?: string | null;
  direccion?: string | null;
  celular?: string | null;
  fechaRegistro?: dayjs.Dayjs | null;
  calificacionPromedio?: number | null;
  activo?: boolean | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  rol?: Pick<IRol, 'id'> | null;
}

export type NewUsuario = Omit<IUsuario, 'id'> & { id: null };
