import dayjs from 'dayjs/esm';

import { IDetalleVenta, NewDetalleVenta } from './detalle-venta.model';

export const sampleWithRequiredData: IDetalleVenta = {
  id: 20796,
  cantidadVenta: 17533,
  precioUnitario: 15740.42,
  descuento: 3150.04,
  subtotal: 25106.24,
};

export const sampleWithPartialData: IDetalleVenta = {
  id: 338,
  cantidadVenta: 16761,
  precioUnitario: 5774.46,
  descuento: 10485.77,
  subtotal: 28638.21,
  usuarioCreacion: 'well',
  fechaCreacion: dayjs('2026-10-01T01:27'),
};

export const sampleWithFullData: IDetalleVenta = {
  id: 8811,
  cantidadVenta: 18713,
  precioUnitario: 19940.24,
  descuento: 4909.05,
  subtotal: 9582.91,
  usuarioCreacion: 'athwart',
  fechaCreacion: dayjs('2026-10-01T01:22'),
  usuarioModificacion: 'quietly',
  fechaModificacion: dayjs('2026-10-01T04:21'),
};

export const sampleWithNewData: NewDetalleVenta = {
  cantidadVenta: 3958,
  precioUnitario: 19725.36,
  descuento: 4766.78,
  subtotal: 1053.77,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
