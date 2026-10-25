import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICategoria } from '../categoria.model';
import { CategoriaService } from '../service/categoria.service';

import { CategoriaFormGroup, CategoriaFormService } from './categoria-form.service';

@Component({
  selector: 'jhi-categoria-update',
  templateUrl: './categoria-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CategoriaUpdate implements OnInit {
  readonly isSaving = signal(false);
  categoria: ICategoria | null = null;

  librosSharedCollection = signal<ILibro[]>([]);

  protected categoriaService = inject(CategoriaService);
  protected categoriaFormService = inject(CategoriaFormService);
  protected libroService = inject(LibroService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CategoriaFormGroup = this.categoriaFormService.createCategoriaFormGroup();

  compareLibro = (o1: ILibro | null, o2: ILibro | null): boolean => this.libroService.compareLibro(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoria }) => {
      this.categoria = categoria;
      if (categoria) {
        this.updateForm(categoria);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const categoria = this.categoriaFormService.getCategoria(this.editForm);
    if (categoria.id === null) {
      this.subscribeToSaveResponse(this.categoriaService.create(categoria));
    } else {
      this.subscribeToSaveResponse(this.categoriaService.update(categoria));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICategoria | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(categoria: ICategoria): void {
    this.categoria = categoria;
    this.categoriaFormService.resetForm(this.editForm, categoria);

    this.librosSharedCollection.update(libros =>
      this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, ...(categoria.libroses ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.libroService
      .query()
      .pipe(map((res: HttpResponse<ILibro[]>) => res.body ?? []))
      .pipe(map((libros: ILibro[]) => this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, ...(this.categoria?.libroses ?? []))))
      .subscribe((libros: ILibro[]) => this.librosSharedCollection.set(libros));
  }
}
