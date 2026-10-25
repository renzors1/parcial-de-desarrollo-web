import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IEditorial } from '../editorial.model';

@Component({
  selector: 'jhi-editorial-detail',
  templateUrl: './editorial-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class EditorialDetail {
  readonly editorial = input<IEditorial | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
