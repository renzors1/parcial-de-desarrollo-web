import dayjs from 'dayjs/esm';

import { IVenta, NewVenta } from './venta.model';

export const sampleWithRequiredData: IVenta = {
  id: 21245,
  numeroComprobante: 'blah',
  fechaVenta: dayjs('2026-10-01T19:02'),
  subtotalVenta: 13164.95,
  totalDescuento: 2634.22,
  igv: 1666.07,
  totalVenta: 15296.1,
};

export const sampleWithPartialData: IVenta = {
  id: 5293,
  numeroComprobante: 'after',
  fechaVenta: dayjs('2026-10-01T07:55'),
  subtotalVenta: 5543.82,
  totalDescuento: 31783.17,
  igv: 6995.92,
  totalVenta: 4917.94,
  usuarioCreacion: 'throughout delightfully however',
  fechaModificacion: dayjs('2026-10-01T15:31'),
};

export const sampleWithFullData: IVenta = {
  id: 8723,
  numeroComprobante: 'after until wonderful',
  fechaVenta: dayjs('2026-10-01T10:45'),
  subtotalVenta: 3651.14,
  totalDescuento: 11174.57,
  igv: 17704.13,
  totalVenta: 12137.31,
  usuarioCreacion: 'over negligible',
  fechaCreacion: dayjs('2026-10-01T11:46'),
  usuarioModificacion: 'thread consistency',
  fechaModificacion: dayjs('2026-10-01T20:24'),
};

export const sampleWithNewData: NewVenta = {
  numeroComprobante: 'lifestyle',
  fechaVenta: dayjs('2026-10-01T14:34'),
  subtotalVenta: 1416.23,
  totalDescuento: 5785.88,
  igv: 9048.02,
  totalVenta: 12114.4,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
