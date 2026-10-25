import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IAutor, NewAutor } from '../autor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAutor for edit and NewAutorFormGroupInput for create.
 */
type AutorFormGroupInput = IAutor | PartialWithRequiredKeyOf<NewAutor>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAutor | NewAutor> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type AutorFormRawValue = FormValueOf<IAutor>;

type NewAutorFormRawValue = FormValueOf<NewAutor>;

type AutorFormDefaults = Pick<NewAutor, 'id' | 'fechaCreacion' | 'fechaModificacion' | 'libroses'>;

type AutorFormGroupContent = {
  id: FormControl<AutorFormRawValue['id'] | NewAutor['id']>;
  nombres: FormControl<AutorFormRawValue['nombres']>;
  apellidos: FormControl<AutorFormRawValue['apellidos']>;
  nacionalidad: FormControl<AutorFormRawValue['nacionalidad']>;
  usuarioCreacion: FormControl<AutorFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<AutorFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<AutorFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<AutorFormRawValue['fechaModificacion']>;
  libroses: FormControl<AutorFormRawValue['libroses']>;
};

export type AutorFormGroup = FormGroup<AutorFormGroupContent>;

@Service()
export class AutorFormService {
  createAutorFormGroup(autor?: AutorFormGroupInput): AutorFormGroup {
    const autorRawValue = this.convertAutorToAutorRawValue({
      ...this.getFormDefaults(),
      ...(autor ?? { id: null }),
    });

    return new FormGroup<AutorFormGroupContent>({
      id: new FormControl(
        { value: autorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombres: new FormControl(autorRawValue.nombres, {
        validators: [Validators.required],
      }),
      apellidos: new FormControl(autorRawValue.apellidos, {
        validators: [Validators.required],
      }),
      nacionalidad: new FormControl(autorRawValue.nacionalidad),
      usuarioCreacion: new FormControl(autorRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(autorRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(autorRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(autorRawValue.fechaModificacion),
      libroses: new FormControl(autorRawValue.libroses ?? []),
    });
  }

  getAutor(form: AutorFormGroup): IAutor | NewAutor {
    return this.convertAutorRawValueToAutor(form.getRawValue());
  }

  resetForm(form: AutorFormGroup, autor: AutorFormGroupInput): void {
    const autorRawValue = this.convertAutorToAutorRawValue({ ...this.getFormDefaults(), ...autor });
    form.reset({
      ...autorRawValue,
      id: { value: autorRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): AutorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
      libroses: [],
    };
  }

  private convertAutorRawValueToAutor(rawAutor: AutorFormRawValue | NewAutorFormRawValue): IAutor | NewAutor {
    return {
      ...rawAutor,
      fechaCreacion: dayjs(rawAutor.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawAutor.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertAutorToAutorRawValue(
    autor: IAutor | (Partial<NewAutor> & AutorFormDefaults),
  ): AutorFormRawValue | PartialWithRequiredKeyOf<NewAutorFormRawValue> {
    return {
      ...autor,
      fechaCreacion: autor.fechaCreacion ? autor.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: autor.fechaModificacion ? autor.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
      libroses: autor.libroses ?? [],
    };
  }
}
