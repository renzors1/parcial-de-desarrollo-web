import dayjs from 'dayjs/esm';

import { IAutor, NewAutor } from './autor.model';

export const sampleWithRequiredData: IAutor = {
  id: 30808,
  nombres: 'corrupt coolly ah',
  apellidos: 'married pastel formal',
};

export const sampleWithPartialData: IAutor = {
  id: 22109,
  nombres: 'dark excellent',
  apellidos: 'well-to-do jiggle',
  usuarioModificacion: 'tenant verbally repentant',
};

export const sampleWithFullData: IAutor = {
  id: 8309,
  nombres: 'ugh though',
  apellidos: 'closely moral meh',
  nacionalidad: 'ugh breed mid',
  usuarioCreacion: 'brightly kielbasa',
  fechaCreacion: dayjs('2026-10-01T15:45'),
  usuarioModificacion: 'gut',
  fechaModificacion: dayjs('2026-10-01T01:50'),
};

export const sampleWithNewData: NewAutor = {
  nombres: 'lotion',
  apellidos: 'flashy gallivant unwelcome',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
