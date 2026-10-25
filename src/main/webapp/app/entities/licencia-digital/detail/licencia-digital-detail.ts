import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ILicenciaDigital } from '../licencia-digital.model';

@Component({
  selector: 'jhi-licencia-digital-detail',
  templateUrl: './licencia-digital-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class LicenciaDigitalDetail {
  readonly licenciaDigital = input<ILicenciaDigital | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
