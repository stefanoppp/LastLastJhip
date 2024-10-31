import { IOpcion, NewOpcion } from './opcion.model';

export const sampleWithRequiredData: IOpcion = {
  id: 13109,
  idExterno: 15868,
  codigo: 'substantiate if',
  nombre: 'fumigate',
  descripcion: 'repeatedly papa',
  precioAdicional: 4946.77,
};

export const sampleWithPartialData: IOpcion = {
  id: 2015,
  idExterno: 12242,
  codigo: 'cruelty',
  nombre: 'charlatan insistent',
  descripcion: 'down pick joint',
  precioAdicional: 28753.97,
};

export const sampleWithFullData: IOpcion = {
  id: 5567,
  idExterno: 18485,
  codigo: 'sizzling',
  nombre: 'unlike until unless',
  descripcion: 'utilization',
  precioAdicional: 5845.1,
};

export const sampleWithNewData: NewOpcion = {
  idExterno: 18172,
  codigo: 'what ruddy produce',
  nombre: 'firm',
  descripcion: 'too resort',
  precioAdicional: 7495.02,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
