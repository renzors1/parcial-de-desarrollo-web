import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICategoria } from '../categoria.model';

@Component({
  selector: 'jhi-categoria-detail',
  templateUrl: './categoria-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class CategoriaDetail {
  readonly categoria = input<ICategoria | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
