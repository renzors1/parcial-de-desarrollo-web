import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IEditorial, NewEditorial } from '../editorial.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEditorial for edit and NewEditorialFormGroupInput for create.
 */
type EditorialFormGroupInput = IEditorial | PartialWithRequiredKeyOf<NewEditorial>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEditorial | NewEditorial> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type EditorialFormRawValue = FormValueOf<IEditorial>;

type NewEditorialFormRawValue = FormValueOf<NewEditorial>;

type EditorialFormDefaults = Pick<NewEditorial, 'id' | 'activo' | 'fechaCreacion' | 'fechaModificacion'>;

type EditorialFormGroupContent = {
  id: FormControl<EditorialFormRawValue['id'] | NewEditorial['id']>;
  nombreEditorial: FormControl<EditorialFormRawValue['nombreEditorial']>;
  pais: FormControl<EditorialFormRawValue['pais']>;
  activo: FormControl<EditorialFormRawValue['activo']>;
  usuarioCreacion: FormControl<EditorialFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<EditorialFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<EditorialFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<EditorialFormRawValue['fechaModificacion']>;
};

export type EditorialFormGroup = FormGroup<EditorialFormGroupContent>;

@Service()
export class EditorialFormService {
  createEditorialFormGroup(editorial?: EditorialFormGroupInput): EditorialFormGroup {
    const editorialRawValue = this.convertEditorialToEditorialRawValue({
      ...this.getFormDefaults(),
      ...(editorial ?? { id: null }),
    });

    return new FormGroup<EditorialFormGroupContent>({
      id: new FormControl(
        { value: editorialRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombreEditorial: new FormControl(editorialRawValue.nombreEditorial, {
        validators: [Validators.required],
      }),
      pais: new FormControl(editorialRawValue.pais),
      activo: new FormControl(editorialRawValue.activo, {
        validators: [Validators.required],
      }),
      usuarioCreacion: new FormControl(editorialRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(editorialRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(editorialRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(editorialRawValue.fechaModificacion),
    });
  }

  getEditorial(form: EditorialFormGroup): IEditorial | NewEditorial {
    return this.convertEditorialRawValueToEditorial(form.getRawValue());
  }

  resetForm(form: EditorialFormGroup, editorial: EditorialFormGroupInput): void {
    const editorialRawValue = this.convertEditorialToEditorialRawValue({ ...this.getFormDefaults(), ...editorial });
    form.reset({
      ...editorialRawValue,
      id: { value: editorialRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EditorialFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      activo: false,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertEditorialRawValueToEditorial(rawEditorial: EditorialFormRawValue | NewEditorialFormRawValue): IEditorial | NewEditorial {
    return {
      ...rawEditorial,
      fechaCreacion: dayjs(rawEditorial.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawEditorial.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertEditorialToEditorialRawValue(
    editorial: IEditorial | (Partial<NewEditorial> & EditorialFormDefaults),
  ): EditorialFormRawValue | PartialWithRequiredKeyOf<NewEditorialFormRawValue> {
    return {
      ...editorial,
      fechaCreacion: editorial.fechaCreacion ? editorial.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: editorial.fechaModificacion ? editorial.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
