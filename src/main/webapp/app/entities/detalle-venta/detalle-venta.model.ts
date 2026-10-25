import dayjs from 'dayjs/esm';

import { ILibro } from 'app/entities/libro/libro.model';
import { IPromocion } from 'app/entities/promocion/promocion.model';
import { IVenta } from 'app/entities/venta/venta.model';

export interface IDetalleVenta {
  id: number;
  cantidadVenta?: number | null;
  precioUnitario?: number | null;
  descuento?: number | null;
  subtotal?: number | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  venta?: Pick<IVenta, 'id'> | null;
  libro?: Pick<ILibro, 'id'> | null;
  promocionAplicada?: Pick<IPromocion, 'id'> | null;
}

export type NewDetalleVenta = Omit<IDetalleVenta, 'id'> & { id: null };
