import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IEditorial } from '../editorial.model';
import { EditorialService } from '../service/editorial.service';

import { EditorialFormGroup, EditorialFormService } from './editorial-form.service';

@Component({
  selector: 'jhi-editorial-update',
  templateUrl: './editorial-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EditorialUpdate implements OnInit {
  readonly isSaving = signal(false);
  editorial: IEditorial | null = null;

  protected editorialService = inject(EditorialService);
  protected editorialFormService = inject(EditorialFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EditorialFormGroup = this.editorialFormService.createEditorialFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ editorial }) => {
      this.editorial = editorial;
      if (editorial) {
        this.updateForm(editorial);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const editorial = this.editorialFormService.getEditorial(this.editForm);
    if (editorial.id === null) {
      this.subscribeToSaveResponse(this.editorialService.create(editorial));
    } else {
      this.subscribeToSaveResponse(this.editorialService.update(editorial));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEditorial | null>): void {
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

  protected updateForm(editorial: IEditorial): void {
    this.editorial = editorial;
    this.editorialFormService.resetForm(this.editForm, editorial);
  }
}
