import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IDetalleVenta } from 'app/entities/detalle-venta/detalle-venta.model';
import { DetalleVentaService } from 'app/entities/detalle-venta/service/detalle-venta.service';
import { EstadoLicencia } from 'app/entities/enumerations/estado-licencia.model';
import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ILicenciaDigital } from '../licencia-digital.model';
import { LicenciaDigitalService } from '../service/licencia-digital.service';

import { LicenciaDigitalFormGroup, LicenciaDigitalFormService } from './licencia-digital-form.service';

@Component({
  selector: 'jhi-licencia-digital-update',
  templateUrl: './licencia-digital-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class LicenciaDigitalUpdate implements OnInit {
  readonly isSaving = signal(false);
  licenciaDigital: ILicenciaDigital | null = null;
  estadoLicenciaValues = Object.keys(EstadoLicencia);

  librosSharedCollection = signal<ILibro[]>([]);
  detalleVentasSharedCollection = signal<IDetalleVenta[]>([]);

  protected licenciaDigitalService = inject(LicenciaDigitalService);
  protected licenciaDigitalFormService = inject(LicenciaDigitalFormService);
  protected libroService = inject(LibroService);
  protected detalleVentaService = inject(DetalleVentaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LicenciaDigitalFormGroup = this.licenciaDigitalFormService.createLicenciaDigitalFormGroup();

  compareLibro = (o1: ILibro | null, o2: ILibro | null): boolean => this.libroService.compareLibro(o1, o2);

  compareDetalleVenta = (o1: IDetalleVenta | null, o2: IDetalleVenta | null): boolean =>
    this.detalleVentaService.compareDetalleVenta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ licenciaDigital }) => {
      this.licenciaDigital = licenciaDigital;
      if (licenciaDigital) {
        this.updateForm(licenciaDigital);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const licenciaDigital = this.licenciaDigitalFormService.getLicenciaDigital(this.editForm);
    if (licenciaDigital.id === null) {
      this.subscribeToSaveResponse(this.licenciaDigitalService.create(licenciaDigital));
    } else {
      this.subscribeToSaveResponse(this.licenciaDigitalService.update(licenciaDigital));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ILicenciaDigital | null>): void {
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

  protected updateForm(licenciaDigital: ILicenciaDigital): void {
    this.licenciaDigital = licenciaDigital;
    this.licenciaDigitalFormService.resetForm(this.editForm, licenciaDigital);

    this.librosSharedCollection.update(libros => this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, licenciaDigital.libro));
    this.detalleVentasSharedCollection.update(detalleVentas =>
      this.detalleVentaService.addDetalleVentaToCollectionIfMissing<IDetalleVenta>(detalleVentas, licenciaDigital.detalleVenta),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.libroService
      .query()
      .pipe(map((res: HttpResponse<ILibro[]>) => res.body ?? []))
      .pipe(map((libros: ILibro[]) => this.libroService.addLibroToCollectionIfMissing<ILibro>(libros, this.licenciaDigital?.libro)))
      .subscribe((libros: ILibro[]) => this.librosSharedCollection.set(libros));

    this.detalleVentaService
      .query()
      .pipe(map((res: HttpResponse<IDetalleVenta[]>) => res.body ?? []))
      .pipe(
        map((detalleVentas: IDetalleVenta[]) =>
          this.detalleVentaService.addDetalleVentaToCollectionIfMissing<IDetalleVenta>(detalleVentas, this.licenciaDigital?.detalleVenta),
        ),
      )
      .subscribe((detalleVentas: IDetalleVenta[]) => this.detalleVentasSharedCollection.set(detalleVentas));
  }
}
