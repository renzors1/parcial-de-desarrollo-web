import dayjs from 'dayjs/esm';

import { IEditorial, NewEditorial } from './editorial.model';

export const sampleWithRequiredData: IEditorial = {
  id: 19349,
  nombreEditorial: 'oh kick',
  activo: true,
};

export const sampleWithPartialData: IEditorial = {
  id: 16289,
  nombreEditorial: 'apropos negotiation',
  activo: false,
  usuarioModificacion: 'molasses readily incidentally',
};

export const sampleWithFullData: IEditorial = {
  id: 6307,
  nombreEditorial: 'analogy if yuck',
  pais: 'hepatitis gadzooks suburban',
  activo: true,
  usuarioCreacion: 'inspection',
  fechaCreacion: dayjs('2026-10-01T05:05'),
  usuarioModificacion: 'wafer',
  fechaModificacion: dayjs('2026-10-01T20:24'),
};

export const sampleWithNewData: NewEditorial = {
  nombreEditorial: 'blah frizz',
  activo: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
