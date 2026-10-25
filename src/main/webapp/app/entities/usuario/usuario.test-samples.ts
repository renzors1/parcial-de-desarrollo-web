import dayjs from 'dayjs/esm';

import { IUsuario, NewUsuario } from './usuario.model';

export const sampleWithRequiredData: IUsuario = {
  id: 23732,
  nombre: 'though phew',
  apellido: 'selfishly deliberately dandelion',
  email: 'Josep.VelascoGastelum@gmail.com',
  contrasena: 'separately machine',
  fechaRegistro: dayjs('2026-10-01T21:03'),
  activo: false,
};

export const sampleWithPartialData: IUsuario = {
  id: 9938,
  nombre: 'petal extent',
  apellido: 'neatly taxicab shrilly',
  email: 'German6@yahoo.com',
  contrasena: 'fat yippee',
  celular: 'because',
  fechaRegistro: dayjs('2026-10-01T02:33'),
  activo: false,
  usuarioCreacion: 'rule uh-huh closed',
  usuarioModificacion: 'before gestate',
};

export const sampleWithFullData: IUsuario = {
  id: 32608,
  nombre: 'outlaw lest',
  apellido: 'nocturnal yum up',
  email: 'MariaJose10@gmail.com',
  contrasena: 'release impostor whose',
  direccion: 'yieldingly ouch',
  celular: 'hurtful per horse',
  fechaRegistro: dayjs('2026-10-01T05:25'),
  calificacionPromedio: 3.62,
  activo: false,
  usuarioCreacion: 'miserly keenly devoted',
  fechaCreacion: dayjs('2026-10-01T21:35'),
  usuarioModificacion: 'psst',
  fechaModificacion: dayjs('2026-10-01T02:57'),
};

export const sampleWithNewData: NewUsuario = {
  nombre: 'beside pish strait',
  apellido: 'programme electrify',
  email: 'AnaLuisa75@gmail.com',
  contrasena: 'mechanically',
  fechaRegistro: dayjs('2026-10-01T04:23'),
  activo: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
