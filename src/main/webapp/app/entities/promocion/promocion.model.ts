import dayjs from 'dayjs/esm';

import { ILibro } from 'app/entities/libro/libro.model';

export interface IPromocion {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  porcentajeDescuento?: number | null;
  cantidadDefinida?: number | null;
  cantidadDisponible?: number | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  activo?: boolean | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  libroses?: Pick<ILibro, 'id'>[] | null;
}

export type NewPromocion = Omit<IPromocion, 'id'> & { id: null };
