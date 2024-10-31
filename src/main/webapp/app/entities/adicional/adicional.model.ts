import { IDispositivo } from 'app/entities/dispositivo/dispositivo.model';
import { IVenta } from 'app/entities/venta/venta.model';

export interface IAdicional {
  id: number;
  idExterno?: number | null;
  nombre?: string | null;
  descripcion?: string | null;
  precio?: number | null;
  precioGratis?: number | null;
  dispositivo?: Pick<IDispositivo, 'id'> | null;
  ventas?: Pick<IVenta, 'id'>[] | null;
}

export type NewAdicional = Omit<IAdicional, 'id'> & { id: null };
