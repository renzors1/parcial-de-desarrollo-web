import dayjs from 'dayjs/esm';

import { ILibro, NewLibro } from './libro.model';

export const sampleWithRequiredData: ILibro = {
  id: 21667,
  isbn: 'boo pish',
  titulo: 'boo',
  precio: 20653.28,
  idioma: 'worth vein disloyal',
  activo: true,
};

export const sampleWithPartialData: ILibro = {
  id: 30583,
  isbn: 'meanwhile',
  titulo: 'mechanically',
  sinopsis: '../fake-data/blob/hipster.txt',
  precio: 1590.73,
  idioma: 'offensively lest queasy',
  activo: false,
  fechaCreacion: dayjs('2026-10-01T14:21'),
  fechaModificacion: dayjs('2026-09-30T22:20'),
};

export const sampleWithFullData: ILibro = {
  id: 32539,
  isbn: 'zowie',
  titulo: 'natural',
  sinopsis: '../fake-data/blob/hipster.txt',
  urlPortada: 'mid',
  fechaPublicacion: dayjs('2026-10-01'),
  precio: 3581.04,
  idioma: 'grandson',
  activo: false,
  usuarioCreacion: 'bah near onto',
  fechaCreacion: dayjs('2026-10-01T19:35'),
  usuarioModificacion: 'yum direct',
  fechaModificacion: dayjs('2026-10-01T13:19'),
};

export const sampleWithNewData: NewLibro = {
  isbn: 'once',
  titulo: 'tall',
  precio: 4712.41,
  idioma: 'debit for over',
  activo: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
