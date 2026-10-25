import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IPromocion } from '../promocion.model';

@Component({
  selector: 'jhi-promocion-detail',
  templateUrl: './promocion-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class PromocionDetail {
  readonly promocion = input<IPromocion | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
