import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { CompraExitosa } from '../../model/tienda.model';

@Component({
  selector: 'jhi-compra-alerta',
  templateUrl: './compra-alerta.component.html',
  styleUrl: './compra-alerta.component.scss',
  imports: [CommonModule, RouterLink, FontAwesomeModule],
})
export class CompraAlertaComponent {
  public readonly compra = input.required<CompraExitosa>();
  public readonly cerrarAlerta = output();

  public onCerrar(): void {
    this.cerrarAlerta.emit();
  }
}
