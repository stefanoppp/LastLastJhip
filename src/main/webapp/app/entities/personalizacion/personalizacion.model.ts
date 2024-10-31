import { IDispositivo } from 'app/entities/dispositivo/dispositivo.model';
import { IVenta } from 'app/entities/venta/venta.model';

export interface IPersonalizacion {
  id: number;
  idExterno?: number | null;
  nombre?: string | null;
  descripcion?: string | null;
  dispositivo?: Pick<IDispositivo, 'id'> | null;
  ventas?: Pick<IVenta, 'id'>[] | null;
}

export type NewPersonalizacion = Omit<IPersonalizacion, 'id'> & { id: null };
