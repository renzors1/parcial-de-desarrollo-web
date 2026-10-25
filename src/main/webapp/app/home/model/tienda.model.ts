import { ILibro } from 'app/entities/libro/libro.model';

export const IGV_PORCENTAJE = 0.18;
export const PRECIO_DEFAULT = 25.0;
export const LIMITE_LIBROS_HOME = 100;
export const LIMITE_CATEGORIAS_HOME = 50;
export const LIMITE_PROMOCIONES_HOME = 10;

export interface CompraExitosa {
  libro: ILibro;
  numeroComprobante: string;
  codigoLicencia: string;
  enlaceDescarga: string;
  total: number;
}

export interface CalculoTotales {
  precioBase: number;
  descuento: number;
  subtotal: number;
  igv: number;
  totalVenta: number;
}
