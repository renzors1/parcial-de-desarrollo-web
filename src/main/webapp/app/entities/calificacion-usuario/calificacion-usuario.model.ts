import dayjs from 'dayjs/esm';

import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IVenta } from 'app/entities/venta/venta.model';

export interface ICalificacionUsuario {
  id: number;
  puntuacion?: number | null;
  comentario?: string | null;
  fechaCalificacion?: dayjs.Dayjs | null;
  activo?: boolean | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  clienteEvaluado?: Pick<IUsuario, 'id'> | null;
  evaluadorTienda?: Pick<IUsuario, 'id'> | null;
  venta?: Pick<IVenta, 'id'> | null;
}

export type NewCalificacionUsuario = Omit<ICalificacionUsuario, 'id'> & { id: null };
