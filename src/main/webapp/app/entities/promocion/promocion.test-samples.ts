import dayjs from 'dayjs/esm';

import { IPromocion, NewPromocion } from './promocion.model';

export const sampleWithRequiredData: IPromocion = {
  id: 21038,
  nombre: 'glittering',
  porcentajeDescuento: 19.17,
  cantidadDefinida: 10266,
  cantidadDisponible: 22186,
  fechaInicio: dayjs('2026-10-01T01:52'),
  fechaFin: dayjs('2026-10-01T06:58'),
  activo: true,
};

export const sampleWithPartialData: IPromocion = {
  id: 14921,
  nombre: 'incinerate fooey',
  descripcion: 'per graduate',
  porcentajeDescuento: 88.97,
  cantidadDefinida: 7308,
  cantidadDisponible: 2765,
  fechaInicio: dayjs('2026-10-01T22:00'),
  fechaFin: dayjs('2026-10-01T16:47'),
  activo: true,
  usuarioCreacion: 'tennis beautifully bonfire',
  fechaCreacion: dayjs('2026-10-01T14:57'),
  fechaModificacion: dayjs('2026-10-01T23:34'),
};

export const sampleWithFullData: IPromocion = {
  id: 9521,
  nombre: 'glaring tune gallery',
  descripcion: 'delicious unabashedly',
  porcentajeDescuento: 48.7,
  cantidadDefinida: 3402,
  cantidadDisponible: 19764,
  fechaInicio: dayjs('2026-10-01T06:43'),
  fechaFin: dayjs('2026-10-01T06:01'),
  activo: true,
  usuarioCreacion: 'midwife',
  fechaCreacion: dayjs('2026-10-01T02:21'),
  usuarioModificacion: 'card',
  fechaModificacion: dayjs('2026-10-01T16:01'),
};

export const sampleWithNewData: NewPromocion = {
  nombre: 'or',
  porcentajeDescuento: 7.41,
  cantidadDefinida: 805,
  cantidadDisponible: 1152,
  fechaInicio: dayjs('2026-10-01T12:08'),
  fechaFin: dayjs('2026-10-01T02:02'),
  activo: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
