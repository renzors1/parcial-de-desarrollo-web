import dayjs from 'dayjs/esm';

import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IVenta {
  id: number;
  numeroComprobante?: string | null;
  fechaVenta?: dayjs.Dayjs | null;
  subtotalVenta?: number | null;
  totalDescuento?: number | null;
  igv?: number | null;
  totalVenta?: number | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  cliente?: Pick<IUsuario, 'id'> | null;
}

export type NewVenta = Omit<IVenta, 'id'> & { id: null };
