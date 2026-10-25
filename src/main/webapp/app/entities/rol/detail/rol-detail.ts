import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IRol } from '../rol.model';

@Component({
  selector: 'jhi-rol-detail',
  templateUrl: './rol-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class RolDetail {
  readonly rol = input<IRol | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
