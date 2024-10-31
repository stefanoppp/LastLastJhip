import dayjs from 'dayjs/esm';
import { IDispositivo } from 'app/entities/dispositivo/dispositivo.model';
import { IPersonalizacion } from 'app/entities/personalizacion/personalizacion.model';
import { IAdicional } from 'app/entities/adicional/adicional.model';

export interface IVenta {
  id: number;
  fechaVenta?: dayjs.Dayjs | null;
  precioFinal?: number | null;
  dispositivo?: Pick<IDispositivo, 'id'> | null;
  personalizaciones?: Pick<IPersonalizacion, 'id'>[] | null;
  adicionales?: Pick<IAdicional, 'id'>[] | null;
}

export type NewVenta = Omit<IVenta, 'id'> & { id: null };
