import dayjs from 'dayjs/esm';

import { ICalificacionUsuario, NewCalificacionUsuario } from './calificacion-usuario.model';

export const sampleWithRequiredData: ICalificacionUsuario = {
  id: 7025,
  puntuacion: 3,
  fechaCalificacion: dayjs('2026-10-24T13:28'),
  activo: false,
};

export const sampleWithPartialData: ICalificacionUsuario = {
  id: 20726,
  puntuacion: 3,
  fechaCalificacion: dayjs('2026-10-25T00:28'),
  activo: false,
  usuarioCreacion: 'whoa nimble gee',
  fechaCreacion: dayjs('2026-10-24T11:41'),
  fechaModificacion: dayjs('2026-10-24T18:46'),
};

export const sampleWithFullData: ICalificacionUsuario = {
  id: 10227,
  puntuacion: 5,
  comentario: 'tackle break',
  fechaCalificacion: dayjs('2026-10-24T03:59'),
  activo: false,
  usuarioCreacion: 'harvest keenly',
  fechaCreacion: dayjs('2026-10-25T00:40'),
  usuarioModificacion: 'idealistic',
  fechaModificacion: dayjs('2026-10-24T05:52'),
};

export const sampleWithNewData: NewCalificacionUsuario = {
  puntuacion: 4,
  fechaCalificacion: dayjs('2026-10-25T02:16'),
  activo: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
