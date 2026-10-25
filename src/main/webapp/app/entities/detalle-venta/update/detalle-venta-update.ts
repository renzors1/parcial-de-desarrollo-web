import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { IPromocion } from 'app/entities/promocion/promocion.model';
import { PromocionService } from 'app/entities/promocion/service/promocion.service';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IDetalleVenta } from '../detalle-venta.model';
import { DetalleVentaService } from '../service/detalle-venta.service';

import { DetalleVentaFormGroup, DetalleVentaFormService } from './detalle-venta-form.service';

@Component({
  selector: 'jhi-detalle-venta-update',
  templateUrl: './detalle-venta-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class DetalleVentaUpdate implements OnInit {
  readonly isSaving = signal(false);
  detalleVenta: IDetalleVenta | null = null;

  ventasSharedCollection = signal<IVenta[]>([]);
  librosSharedCollection = signal<ILibro[]>([]);
  promocionsSharedCollection = signal<IPromocion[]>([]);

  protected detalleVentaService = inject(DetalleVentaService);
  protected detalleVentaFormService = inject(DetalleVentaFormService);
  protected ventaService = inject(VentaService);
  protected libroService = inject(LibroService);
  protected promocionService = inject(PromocionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DetalleVentaFormGroup = this.detalleVentaFormService.createDetalleVentaFormGroup();

  compareVenta = (o1: IVenta | null, o2: IVenta | null): boolean => this.ventaService.compareVenta(o1, o2);

  compareLibro = (o1: ILibro | null, o2: ILibro | null): boolean => this.libroService.compareLibro(o1, o2);

  comparePromocion = (o1: IPromocion | null, o2: IPromocion | null): boolean => this.promocionService.comparePromocion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleVenta }) => {
      this.detalleVenta = detalleVenta;
      if (detalleVenta) {
        this.updateForm(detalleVenta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const detalleVenta = this.detalleVentaFormService.getDetalleVenta(this.editForm);
    if (detalleVenta.id === null) {
      this.subscribeToSaveResponse(this.detalleVentaService.create(detalleVenta));
    } else {
      this.subscribeToSaveResponse(this.detalleVentaService.update(detalleVenta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IDetalleVenta | null>): void {
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

  protected updateForm(detalleVenta: IDetalleVenta): void {
    this.detalleVenta = detalleVenta;
    this.detalleVentaFormService.resetForm(this.editForm, detalleVenta);

    this.ventasSharedCollection.update(ventas => this.ventaService.addVentaToCollectionIfMissing<IVenta>(ventas, detalleVenta.venta));
    this.librosSharedCollection.update(libros => this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, detalleVenta.libro));
    this.promocionsSharedCollection.update(promocions =>
      this.promocionService.addPromocionToCollectionIfMissing<IPromocion>(promocions, detalleVenta.promocionAplicada),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing<IVenta>(ventas, this.detalleVenta?.venta)))
      .subscribe((ventas: IVenta[]) => this.ventasSharedCollection.set(ventas));

    this.libroService
      .query()
      .pipe(map((res: HttpResponse<ILibro[]>) => res.body ?? []))
      .pipe(map((libros: ILibro[]) => this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, this.detalleVenta?.libro)))
      .subscribe((libros: ILibro[]) => this.librosSharedCollection.set(libros));

    this.promocionService
      .query()
      .pipe(map((res: HttpResponse<IPromocion[]>) => res.body ?? []))
      .pipe(
        map((promocions: IPromocion[]) =>
          this.promocionService.addPromocionToCollectionIfMissing<IPromocion>(promocions, this.detalleVenta?.promocionAplicada),
        ),
      )
      .subscribe((promocions: IPromocion[]) => this.promocionsSharedCollection.set(promocions));
  }
}
