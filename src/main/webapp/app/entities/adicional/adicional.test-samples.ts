import { IAdicional, NewAdicional } from './adicional.model';

export const sampleWithRequiredData: IAdicional = {
  id: 22727,
  idExterno: 24652,
  nombre: 'when',
  descripcion: 'psst lady brand',
  precio: 2440.45,
  precioGratis: 28879.19,
};

export const sampleWithPartialData: IAdicional = {
  id: 12695,
  idExterno: 1893,
  nombre: 'stake',
  descripcion: 'about dearly',
  precio: 13055.74,
  precioGratis: 12783.57,
};

export const sampleWithFullData: IAdicional = {
  id: 28248,
  idExterno: 30383,
  nombre: 'flight wherever fooey',
  descripcion: 'functional up',
  precio: 18334.21,
  precioGratis: 26122.52,
};

export const sampleWithNewData: NewAdicional = {
  idExterno: 25051,
  nombre: 'whoa ultimately',
  descripcion: 'necessary mid',
  precio: 13186.82,
  precioGratis: 4839.58,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
