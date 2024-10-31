import { IPersonalizacion, NewPersonalizacion } from './personalizacion.model';

export const sampleWithRequiredData: IPersonalizacion = {
  id: 18251,
  idExterno: 27166,
  nombre: 'favorable lighthearted if',
  descripcion: 'remarkable while or',
};

export const sampleWithPartialData: IPersonalizacion = {
  id: 32044,
  idExterno: 23156,
  nombre: 'knit alienated',
  descripcion: 'cheerful moral exactly',
};

export const sampleWithFullData: IPersonalizacion = {
  id: 5656,
  idExterno: 4489,
  nombre: 'defendant floodlight',
  descripcion: 'barracks',
};

export const sampleWithNewData: NewPersonalizacion = {
  idExterno: 4601,
  nombre: 'behind sad',
  descripcion: 'behind',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
