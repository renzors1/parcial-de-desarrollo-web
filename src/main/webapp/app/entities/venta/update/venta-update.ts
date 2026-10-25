import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { VentaService } from '../service/venta.service';
import { IVenta } from '../venta.model';

import { VentaFormGroup, VentaFormService } from './venta-form.service';

@Component({
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class VentaUpdate implements OnInit {
  readonly isSaving = signal(false);
  venta: IVenta | null = null;

  usuariosSharedCollection = signal<IUsuario[]>([]);

  protected ventaService = inject(VentaService);
  protected ventaFormService = inject(VentaFormService);
  protected usuarioService = inject(UsuarioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VentaFormGroup = this.ventaFormService.createVentaFormGroup();

  compareUsuario = (o1: IUsuario | null, o2: IUsuario | null): boolean => this.usuarioService.compareUsuario(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.venta = venta;
      if (venta) {
        this.updateForm(venta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const venta = this.ventaFormService.getVenta(this.editForm);
    if (venta.id === null) {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IVenta | null>): void {
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

  protected updateForm(venta: IVenta): void {
    this.venta = venta;
    this.ventaFormService.resetForm(this.editForm, venta);

    this.usuariosSharedCollection.update(usuarios =>
      this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(usuarios, venta.cliente),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(usuarios, this.venta?.cliente)))
      .subscribe((usuarios: IUsuario[]) => this.usuariosSharedCollection.set(usuarios));
  }
}
