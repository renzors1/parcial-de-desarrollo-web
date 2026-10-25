import dayjs from 'dayjs/esm';

import { ICategoria, NewCategoria } from './categoria.model';

export const sampleWithRequiredData: ICategoria = {
  id: 16672,
  nombreCategoria: 'clavicle',
  activo: false,
};

export const sampleWithPartialData: ICategoria = {
  id: 31700,
  nombreCategoria: 'beside redesign',
  activo: false,
  fechaModificacion: dayjs('2026-10-01T05:29'),
};

export const sampleWithFullData: ICategoria = {
  id: 12751,
  nombreCategoria: 'what unlucky yowza',
  activo: true,
  usuarioCreacion: 'bourgeoisie astride',
  fechaCreacion: dayjs('2026-10-01T07:56'),
  usuarioModificacion: 'positively huzzah till',
  fechaModificacion: dayjs('2026-10-01T03:18'),
};

export const sampleWithNewData: NewCategoria = {
  nombreCategoria: 'unbalance delightfully like',
  activo: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
