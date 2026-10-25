import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IRol } from '../rol.model';
import { RolService } from '../service/rol.service';

import { RolFormGroup, RolFormService } from './rol-form.service';

@Component({
  selector: 'jhi-rol-update',
  templateUrl: './rol-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class RolUpdate implements OnInit {
  readonly isSaving = signal(false);
  rol: IRol | null = null;

  protected rolService = inject(RolService);
  protected rolFormService = inject(RolFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RolFormGroup = this.rolFormService.createRolFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rol }) => {
      this.rol = rol;
      if (rol) {
        this.updateForm(rol);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const rol = this.rolFormService.getRol(this.editForm);
    if (rol.id === null) {
      this.subscribeToSaveResponse(this.rolService.create(rol));
    } else {
      this.subscribeToSaveResponse(this.rolService.update(rol));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IRol | null>): void {
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

  protected updateForm(rol: IRol): void {
    this.rol = rol;
    this.rolFormService.resetForm(this.editForm, rol);
  }
}
