import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICalificacionUsuario } from '../calificacion-usuario.model';
import { CalificacionUsuarioService } from '../service/calificacion-usuario.service';

import { CalificacionUsuarioFormGroup, CalificacionUsuarioFormService } from './calificacion-usuario-form.service';

@Component({
  selector: 'jhi-calificacion-usuario-update',
  templateUrl: './calificacion-usuario-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CalificacionUsuarioUpdate implements OnInit {
  readonly isSaving = signal(false);
  calificacionUsuario: ICalificacionUsuario | null = null;

  usuariosSharedCollection = signal<IUsuario[]>([]);
  ventasSharedCollection = signal<IVenta[]>([]);

  protected calificacionUsuarioService = inject(CalificacionUsuarioService);
  protected calificacionUsuarioFormService = inject(CalificacionUsuarioFormService);
  protected usuarioService = inject(UsuarioService);
  protected ventaService = inject(VentaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CalificacionUsuarioFormGroup = this.calificacionUsuarioFormService.createCalificacionUsuarioFormGroup();

  compareUsuario = (o1: IUsuario | null, o2: IUsuario | null): boolean => this.usuarioService.compareUsuario(o1, o2);

  compareVenta = (o1: IVenta | null, o2: IVenta | null): boolean => this.ventaService.compareVenta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calificacionUsuario }) => {
      this.calificacionUsuario = calificacionUsuario;
      if (calificacionUsuario) {
        this.updateForm(calificacionUsuario);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const calificacionUsuario = this.calificacionUsuarioFormService.getCalificacionUsuario(this.editForm);
    if (calificacionUsuario.id === null) {
      this.subscribeToSaveResponse(this.calificacionUsuarioService.create(calificacionUsuario));
    } else {
      this.subscribeToSaveResponse(this.calificacionUsuarioService.update(calificacionUsuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICalificacionUsuario | null>): void {
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

  protected updateForm(calificacionUsuario: ICalificacionUsuario): void {
    this.calificacionUsuario = calificacionUsuario;
    this.calificacionUsuarioFormService.resetForm(this.editForm, calificacionUsuario);

    this.usuariosSharedCollection.update(usuarios =>
      this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(
        usuarios,
        calificacionUsuario.clienteEvaluado,
        calificacionUsuario.evaluadorTienda,
      ),
    );
    this.ventasSharedCollection.update(ventas =>
      this.ventaService.addVentaToCollectionIfMissing<IVenta>(ventas, calificacionUsuario.venta),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(
            usuarios,
            this.calificacionUsuario?.clienteEvaluado,
            this.calificacionUsuario?.evaluadorTienda,
          ),
        ),
      )
      .subscribe((usuarios: IUsuario[]) => this.usuariosSharedCollection.set(usuarios));

    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing<IVenta>(ventas, this.calificacionUsuario?.venta)))
      .subscribe((ventas: IVenta[]) => this.ventasSharedCollection.set(ventas));
  }
}
