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
import { IPromocion } from '../promocion.model';
import { PromocionService } from '../service/promocion.service';

import { PromocionFormGroup, PromocionFormService } from './promocion-form.service';

@Component({
  selector: 'jhi-promocion-update',
  templateUrl: './promocion-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PromocionUpdate implements OnInit {
  readonly isSaving = signal(false);
  promocion: IPromocion | null = null;

  librosSharedCollection = signal<ILibro[]>([]);

  protected promocionService = inject(PromocionService);
  protected promocionFormService = inject(PromocionFormService);
  protected libroService = inject(LibroService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PromocionFormGroup = this.promocionFormService.createPromocionFormGroup();

  compareLibro = (o1: ILibro | null, o2: ILibro | null): boolean => this.libroService.compareLibro(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promocion }) => {
      this.promocion = promocion;
      if (promocion) {
        this.updateForm(promocion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const promocion = this.promocionFormService.getPromocion(this.editForm);
    if (promocion.id === null) {
      this.subscribeToSaveResponse(this.promocionService.create(promocion));
    } else {
      this.subscribeToSaveResponse(this.promocionService.update(promocion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPromocion | null>): void {
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

  protected updateForm(promocion: IPromocion): void {
    this.promocion = promocion;
    this.promocionFormService.resetForm(this.editForm, promocion);

    this.librosSharedCollection.update(libros =>
      this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, ...(promocion.libroses ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.libroService
      .query()
      .pipe(map((res: HttpResponse<ILibro[]>) => res.body ?? []))
      .pipe(map((libros: ILibro[]) => this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, ...(this.promocion?.libroses ?? []))))
      .subscribe((libros: ILibro[]) => this.librosSharedCollection.set(libros));
  }
}
