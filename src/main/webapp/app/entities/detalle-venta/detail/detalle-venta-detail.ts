import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IDetalleVenta } from '../detalle-venta.model';

@Component({
  selector: 'jhi-detalle-venta-detail',
  templateUrl: './detalle-venta-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class DetalleVentaDetail {
  readonly detalleVenta = input<IDetalleVenta | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
