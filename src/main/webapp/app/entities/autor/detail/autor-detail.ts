import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IAutor } from '../autor.model';

@Component({
  selector: 'jhi-autor-detail',
  templateUrl: './autor-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class AutorDetail {
  readonly autor = input<IAutor | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
