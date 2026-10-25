import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, switchMap, map } from 'rxjs';

import { DetalleVentaService } from 'app/entities/detalle-venta/service/detalle-venta.service';
import { LicenciaDigitalService } from 'app/entities/licencia-digital/service/licencia-digital.service';
import { ILibro } from 'app/entities/libro/libro.model';
import { IPromocion } from 'app/entities/promocion/promocion.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { CalculoTotales, CompraExitosa, IGV_PORCENTAJE, PRECIO_DEFAULT } from '../model/tienda.model';

@Injectable({
  providedIn: 'root',
})
export class CheckoutTiendaService {
  private readonly ventaService = inject(VentaService);
  private readonly detalleVentaService = inject(DetalleVentaService);
  private readonly licenciaDigitalService = inject(LicenciaDigitalService);

  public calcularTotales(precioBase: number, promocion: IPromocion | null): CalculoTotales {
    let descuento = 0;
    if (promocion) {
      const porcentaje = promocion.porcentajeDescuento ?? 0;
      descuento = (precioBase * porcentaje) / 100;
    }

    const subtotal = Math.max(0, precioBase - descuento);
    const igv = subtotal * IGV_PORCENTAJE;
    const totalVenta = subtotal + igv;

    return {
      precioBase,
      descuento,
      subtotal,
      igv,
      totalVenta,
    };
  }

  public generarNumeroComprobante(): string {
    return `B001-${Date.now().toString().slice(-6)}`;
  }

  public generarCodigoLicencia(): string {
    const timestamp = Date.now().toString(36).toUpperCase();
    const aleatorio = Math.random().toString(36).substring(2, 6).toUpperCase();
    return `LIC-${timestamp}-${aleatorio}`;
  }

  public procesarAdquisicion(libro: ILibro, promocion: IPromocion | null, usuarioActual: string): Observable<CompraExitosa> {
    const precioBase = libro.precio ?? PRECIO_DEFAULT;
    const { descuento, subtotal, igv, totalVenta } = this.calcularTotales(precioBase, promocion);
    const numeroComprobante = this.generarNumeroComprobante();
    const fechaActual = dayjs();

    // 1. Registrar Cabecera de Venta
    return this.ventaService
      .create({
        id: null,
        numeroComprobante,
        fechaVenta: fechaActual,
        subtotalVenta: subtotal,
        totalDescuento: descuento,
        igv,
        totalVenta,
        usuarioCreacion: usuarioActual,
        fechaCreacion: fechaActual,
      })
      .pipe(
        // 2. Registrar Detalle de Venta
        switchMap(ventaCreada =>
          this.detalleVentaService.create({
            id: null,
            cantidadVenta: 1,
            precioUnitario: precioBase,
            descuento,
            subtotal,
            venta: { id: ventaCreada.id },
            libro: { id: libro.id },
            promocionAplicada: promocion?.id ? { id: promocion.id } : null,
            usuarioCreacion: usuarioActual,
            fechaCreacion: fechaActual,
          }),
        ),
        // 3. Emitir y Asignar Licencia Digital
        switchMap(detalleCreado => {
          const codigoLicencia = this.generarCodigoLicencia();
          const enlaceDescarga = `https://biblioteca-digital.local/descargas/${codigoLicencia}.epub`;

          return this.licenciaDigitalService
            .create({
              id: null,
              codigoLicencia,
              enlaceDescarga,
              fechaAsignacion: fechaActual,
              estado: 'VENDIDA',
              libro: { id: libro.id },
              detalleVenta: { id: detalleCreado.id },
              usuarioCreacion: usuarioActual,
              fechaCreacion: fechaActual,
            })
            .pipe(
              map(() => ({
                libro,
                numeroComprobante,
                codigoLicencia,
                enlaceDescarga,
                total: totalVenta,
              })),
            );
        }),
      );
  }
}
