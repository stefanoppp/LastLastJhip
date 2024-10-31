import { ICaracteristica, NewCaracteristica } from './caracteristica.model';

export const sampleWithRequiredData: ICaracteristica = {
  id: 1229,
  idExterno: 11968,
  nombre: 'aboard',
  descripcion: 'closely',
};

export const sampleWithPartialData: ICaracteristica = {
  id: 18631,
  idExterno: 27999,
  nombre: 'cloudy geez save',
  descripcion: 'sandy',
};

export const sampleWithFullData: ICaracteristica = {
  id: 28629,
  idExterno: 16819,
  nombre: 'reopen saturate',
  descripcion: 'enormously whoa oxidize',
};

export const sampleWithNewData: NewCaracteristica = {
  idExterno: 21752,
  nombre: 'unit now drug',
  descripcion: 'that wearily',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
