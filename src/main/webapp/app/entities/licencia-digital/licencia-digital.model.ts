import dayjs from 'dayjs/esm';

import { IDetalleVenta } from 'app/entities/detalle-venta/detalle-venta.model';
import { EstadoLicencia } from 'app/entities/enumerations/estado-licencia.model';
import { ILibro } from 'app/entities/libro/libro.model';

export interface ILicenciaDigital {
  id: number;
  codigoLicencia?: string | null;
  enlaceDescarga?: string | null;
  fechaAsignacion?: dayjs.Dayjs | null;
  estado?: keyof typeof EstadoLicencia | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  libro?: Pick<ILibro, 'id'> | null;
  detalleVenta?: Pick<IDetalleVenta, 'id'> | null;
}

export type NewLicenciaDigital = Omit<ILicenciaDigital, 'id'> & { id: null };
