import { IDispositivo, NewDispositivo } from './dispositivo.model';

export const sampleWithRequiredData: IDispositivo = {
  id: 2128,
  idExterno: 22643,
  codigo: 'joyous kookily aching',
  nombre: 'pessimistic ill',
  descripcion: 'pillbox mmm',
  precioBase: 12268.94,
  moneda: 'USD',
};

export const sampleWithPartialData: IDispositivo = {
  id: 14022,
  idExterno: 21807,
  codigo: 'excluding from woot',
  nombre: 'fairly blah unethically',
  descripcion: 'after within why',
  precioBase: 10176.82,
  moneda: 'EUR',
};

export const sampleWithFullData: IDispositivo = {
  id: 9005,
  idExterno: 11015,
  codigo: 'rag preregister',
  nombre: 'seemingly garage',
  descripcion: 'exasperation',
  precioBase: 5192.78,
  moneda: 'USD',
};

export const sampleWithNewData: NewDispositivo = {
  idExterno: 11307,
  codigo: 'unimpressively selfishly',
  nombre: 'ack knowledgeably worldly',
  descripcion: 'psst nor',
  precioBase: 7247.42,
  moneda: 'EUR',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
