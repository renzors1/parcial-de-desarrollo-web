import dayjs from 'dayjs/esm';

import { IUserManagement, NewUserManagement } from './user-management.model';

export const sampleWithRequiredData: IUserManagement = {
  login: 'Diego.ManzanaresRomo',
  email: 'Margarita5@hotmail.com',
};

export const sampleWithPartialData: IUserManagement = {
  id: 18824,
  login: 'Guadalupe.LaboyMoreno4',
  firstName: 'Miguel',
  lastName: 'Navarrete Carrera',
  email: 'Graciela44@hotmail.com',
  activated: false,
  langKey: 'en',
  lastModifiedBy: 'upon around',
  lastModifiedDate: dayjs('2026-09-22T03:14'),
};

export const sampleWithFullData: IUserManagement = {
  id: 26558,
  login: 'Catalina72',
  firstName: 'Micaela',
  lastName: 'Apodaca Armendáriz',
  email: 'Alfredo_CardenasAdame65@hotmail.com',
  activated: false,
  langKey: 'en',
  imageUrl: 'the',
  createdBy: 'yahoo',
  createdDate: dayjs('2026-09-21T18:37'),
  lastModifiedBy: 'airline',
  lastModifiedDate: dayjs('2026-09-21T19:06'),
};

export const sampleWithNewData: NewUserManagement = {
  email: 'JuanCarlos82@yahoo.com',
  login: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
