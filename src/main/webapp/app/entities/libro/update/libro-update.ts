import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IAutor } from 'app/entities/autor/autor.model';
import { AutorService } from 'app/entities/autor/service/autor.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IEditorial } from 'app/entities/editorial/editorial.model';
import { EditorialService } from 'app/entities/editorial/service/editorial.service';
import { IPromocion } from 'app/entities/promocion/promocion.model';
import { PromocionService } from 'app/entities/promocion/service/promocion.service';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { TranslateDirective } from 'app/shared/language';
import { ILibro } from '../libro.model';
import { LibroService } from '../service/libro.service';

import { LibroFormGroup, LibroFormService } from './libro-form.service';

@Component({
  selector: 'jhi-libro-update',
  templateUrl: './libro-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class LibroUpdate implements OnInit {
  readonly isSaving = signal(false);
  libro: ILibro | null = null;

  categoriasSharedCollection = signal<ICategoria[]>([]);
  autorsSharedCollection = signal<IAutor[]>([]);
  editorialsSharedCollection = signal<IEditorial[]>([]);
  promocionsSharedCollection = signal<IPromocion[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected libroService = inject(LibroService);
  protected libroFormService = inject(LibroFormService);
  protected categoriaService = inject(CategoriaService);
  protected autorService = inject(AutorService);
  protected editorialService = inject(EditorialService);
  protected promocionService = inject(PromocionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LibroFormGroup = this.libroFormService.createLibroFormGroup();

  compareCategoria = (o1: ICategoria | null, o2: ICategoria | null): boolean => this.categoriaService.compareCategoria(o1, o2);

  compareAutor = (o1: IAutor | null, o2: IAutor | null): boolean => this.autorService.compareAutor(o1, o2);

  compareEditorial = (o1: IEditorial | null, o2: IEditorial | null): boolean => this.editorialService.compareEditorial(o1, o2);

  comparePromocion = (o1: IPromocion | null, o2: IPromocion | null): boolean => this.promocionService.comparePromocion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ libro }) => {
      this.libro = libro;
      if (libro) {
        this.updateForm(libro);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined, blobType?: BlobType): void {
    this.dataUtils.openFile(base64String, contentType, blobType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('ejemplo01App.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const libro = this.libroFormService.getLibro(this.editForm);
    if (libro.id === null) {
      this.subscribeToSaveResponse(this.libroService.create(libro));
    } else {
      this.subscribeToSaveResponse(this.libroService.update(libro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ILibro | null>): void {
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

  protected updateForm(libro: ILibro): void {
    this.libro = libro;
    this.libroFormService.resetForm(this.editForm, libro);

    this.categoriasSharedCollection.update(categorias =>
      this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(categorias, ...(libro.categoriases ?? [])),
    );
    this.autorsSharedCollection.update(autors =>
      this.autorService.addAutorToCollectionIfMissing<IAutor>(autors, ...(libro.autoreses ?? [])),
    );
    this.editorialsSharedCollection.update(editorials =>
      this.editorialService.addEditorialToCollectionIfMissing<IEditorial>(editorials, libro.editorial),
    );
    this.promocionsSharedCollection.update(promocions =>
      this.promocionService.addPromocionToCollectionIfMissing<IPromocion>(promocions, ...(libro.promocioneses ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaService
      .query()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(categorias, ...(this.libro?.categoriases ?? [])),
        ),
      )
      .subscribe((categorias: ICategoria[]) => this.categoriasSharedCollection.set(categorias));

    this.autorService
      .query()
      .pipe(map((res: HttpResponse<IAutor[]>) => res.body ?? []))
      .pipe(map((autors: IAutor[]) => this.autorService.addAutorToCollectionIfMissing<IAutor>(autors, ...(this.libro?.autoreses ?? []))))
      .subscribe((autors: IAutor[]) => this.autorsSharedCollection.set(autors));

    this.editorialService
      .query()
      .pipe(map((res: HttpResponse<IEditorial[]>) => res.body ?? []))
      .pipe(
        map((editorials: IEditorial[]) =>
          this.editorialService.addEditorialToCollectionIfMissing<IEditorial>(editorials, this.libro?.editorial),
        ),
      )
      .subscribe((editorials: IEditorial[]) => this.editorialsSharedCollection.set(editorials));

    this.promocionService
      .query()
      .pipe(map((res: HttpResponse<IPromocion[]>) => res.body ?? []))
      .pipe(
        map((promocions: IPromocion[]) =>
          this.promocionService.addPromocionToCollectionIfMissing<IPromocion>(promocions, ...(this.libro?.promocioneses ?? [])),
        ),
      )
      .subscribe((promocions: IPromocion[]) => this.promocionsSharedCollection.set(promocions));
  }
}
