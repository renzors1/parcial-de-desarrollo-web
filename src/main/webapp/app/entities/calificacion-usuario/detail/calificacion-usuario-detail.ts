import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICalificacionUsuario } from '../calificacion-usuario.model';

@Component({
  selector: 'jhi-calificacion-usuario-detail',
  templateUrl: './calificacion-usuario-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class CalificacionUsuarioDetail {
  readonly calificacionUsuario = input<ICalificacionUsuario | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
