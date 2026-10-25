import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IRol } from 'app/entities/rol/rol.model';
import { RolService } from 'app/entities/rol/service/rol.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { UsuarioService } from '../service/usuario.service';
import { IUsuario } from '../usuario.model';

import { UsuarioFormGroup, UsuarioFormService } from './usuario-form.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class UsuarioUpdate implements OnInit {
  readonly isSaving = signal(false);
  usuario: IUsuario | null = null;

  rolsSharedCollection = signal<IRol[]>([]);

  protected usuarioService = inject(UsuarioService);
  protected usuarioFormService = inject(UsuarioFormService);
  protected rolService = inject(RolService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UsuarioFormGroup = this.usuarioFormService.createUsuarioFormGroup();

  compareRol = (o1: IRol | null, o2: IRol | null): boolean => this.rolService.compareRol(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      this.usuario = usuario;
      if (usuario) {
        this.updateForm(usuario);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const usuario = this.usuarioFormService.getUsuario(this.editForm);
    if (usuario.id === null) {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IUsuario | null>): void {
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

  protected updateForm(usuario: IUsuario): void {
    this.usuario = usuario;
    this.usuarioFormService.resetForm(this.editForm, usuario);

    this.rolsSharedCollection.update(rols => this.rolService.addRolToCollectionIfMissing<IRol>(rols, usuario.rol));
  }

  protected loadRelationshipsOptions(): void {
    this.rolService
      .query()
      .pipe(map((res: HttpResponse<IRol[]>) => res.body ?? []))
      .pipe(map((rols: IRol[]) => this.rolService.addRolToCollectionIfMissing<IRol>(rols, this.usuario?.rol)))
      .subscribe((rols: IRol[]) => this.rolsSharedCollection.set(rols));
  }
}
