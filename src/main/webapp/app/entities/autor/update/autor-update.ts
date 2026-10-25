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
import { IAutor } from '../autor.model';
import { AutorService } from '../service/autor.service';

import { AutorFormGroup, AutorFormService } from './autor-form.service';

@Component({
  selector: 'jhi-autor-update',
  templateUrl: './autor-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class AutorUpdate implements OnInit {
  readonly isSaving = signal(false);
  autor: IAutor | null = null;

  librosSharedCollection = signal<ILibro[]>([]);

  protected autorService = inject(AutorService);
  protected autorFormService = inject(AutorFormService);
  protected libroService = inject(LibroService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AutorFormGroup = this.autorFormService.createAutorFormGroup();

  compareLibro = (o1: ILibro | null, o2: ILibro | null): boolean => this.libroService.compareLibro(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ autor }) => {
      this.autor = autor;
      if (autor) {
        this.updateForm(autor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const autor = this.autorFormService.getAutor(this.editForm);
    if (autor.id === null) {
      this.subscribeToSaveResponse(this.autorService.create(autor));
    } else {
      this.subscribeToSaveResponse(this.autorService.update(autor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAutor | null>): void {
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

  protected updateForm(autor: IAutor): void {
    this.autor = autor;
    this.autorFormService.resetForm(this.editForm, autor);

    this.librosSharedCollection.update(libros =>
      this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, ...(autor.libroses ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.libroService
      .query()
      .pipe(map((res: HttpResponse<ILibro[]>) => res.body ?? []))
      .pipe(map((libros: ILibro[]) => this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, ...(this.autor?.libroses ?? []))))
      .subscribe((libros: ILibro[]) => this.librosSharedCollection.set(libros));
  }
}
