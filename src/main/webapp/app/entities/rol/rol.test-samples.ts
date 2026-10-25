import dayjs from 'dayjs/esm';

import { IRol, NewRol } from './rol.model';

export const sampleWithRequiredData: IRol = {
  id: 27420,
  codigoIdentificador: 23181,
  nombreRol: 'jam-packed shiny everlasting',
  activo: true,
};

export const sampleWithPartialData: IRol = {
  id: 4917,
  codigoIdentificador: 14050,
  nombreRol: 'aw rebuff',
  descripcion: 'inspect',
  activo: false,
  usuarioCreacion: 'vast instead although',
  fechaCreacion: dayjs('2026-10-01T21:26'),
};

export const sampleWithFullData: IRol = {
  id: 7094,
  codigoIdentificador: 121,
  nombreRol: 'section dilate',
  descripcion: 'vacantly',
  activo: true,
  usuarioCreacion: 'nor',
  fechaCreacion: dayjs('2026-10-01T08:30'),
  usuarioModificacion: 'versus',
  fechaModificacion: dayjs('2026-10-01T07:56'),
};

export const sampleWithNewData: NewRol = {
  codigoIdentificador: 24757,
  nombreRol: 'hopelessly chiffonier',
  activo: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
