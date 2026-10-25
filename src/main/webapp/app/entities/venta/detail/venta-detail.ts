import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IVenta } from '../venta.model';

@Component({
  selector: 'jhi-venta-detail',
  templateUrl: './venta-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class VentaDetail {
  readonly venta = input<IVenta | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
