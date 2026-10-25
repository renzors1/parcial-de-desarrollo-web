import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { ICategoria } from 'app/entities/categoria/categoria.model';

@Component({
  selector: 'jhi-catalogo-filtros',
  templateUrl: './catalogo-filtros.component.html',
  imports: [CommonModule, FormsModule, FontAwesomeModule],
})
export class CatalogoFiltrosComponent {
  public readonly texto = input<string>('');
  public readonly categorias = input<ICategoria[]>([]);
  public readonly categoriaSeleccionadaId = input<number | null>(null);

  public readonly textoChange = output<string>();
  public readonly categoriaSelect = output<number | null>();

  public onTextoChange(nuevoTexto: string): void {
    this.textoChange.emit(nuevoTexto);
  }

  public onSelectCategoria(id: number | null): void {
    this.categoriaSelect.emit(id);
  }
}
