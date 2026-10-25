import dayjs from 'dayjs/esm';

import { ILicenciaDigital, NewLicenciaDigital } from './licencia-digital.model';

export const sampleWithRequiredData: ILicenciaDigital = {
  id: 29124,
  codigoLicencia: 'creature',
  estado: 'DISPONIBLE',
};

export const sampleWithPartialData: ILicenciaDigital = {
  id: 19312,
  codigoLicencia: 'coast railway digit',
  enlaceDescarga: 'beyond following',
  fechaAsignacion: dayjs('2026-10-01T21:57'),
  estado: 'DISPONIBLE',
  usuarioCreacion: 'adventurously',
  usuarioModificacion: 'provided formula sleet',
  fechaModificacion: dayjs('2026-10-01T06:12'),
};

export const sampleWithFullData: ILicenciaDigital = {
  id: 18176,
  codigoLicencia: 'yearningly nor',
  enlaceDescarga: 'difficult staid scowl',
  fechaAsignacion: dayjs('2026-10-01T16:25'),
  estado: 'VENDIDA',
  usuarioCreacion: 'deployment keenly that',
  fechaCreacion: dayjs('2026-10-01T19:45'),
  usuarioModificacion: 'finally unbearably neck',
  fechaModificacion: dayjs('2026-10-01T06:23'),
};

export const sampleWithNewData: NewLicenciaDigital = {
  codigoLicencia: 'uh-huh',
  estado: 'VENDIDA',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
