import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { ILibro } from 'app/entities/libro/libro.model';

@Component({
  selector: 'jhi-libro-card',
  templateUrl: './libro-card.component.html',
  styleUrl: './libro-card.component.scss',
  imports: [CommonModule, RouterLink, FontAwesomeModule],
})
export class LibroCardComponent {
  public readonly libro = input.required<ILibro>();
  public readonly isProcesando = input<boolean>(false);

  public readonly comprar = output<ILibro>();

  public onComprar(): void {
    this.comprar.emit(this.libro());
  }
}
