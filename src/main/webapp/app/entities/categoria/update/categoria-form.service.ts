import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ICategoria, NewCategoria } from '../categoria.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategoria for edit and NewCategoriaFormGroupInput for create.
 */
type CategoriaFormGroupInput = ICategoria | PartialWithRequiredKeyOf<NewCategoria>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICategoria | NewCategoria> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type CategoriaFormRawValue = FormValueOf<ICategoria>;

type NewCategoriaFormRawValue = FormValueOf<NewCategoria>;

type CategoriaFormDefaults = Pick<NewCategoria, 'id' | 'activo' | 'fechaCreacion' | 'fechaModificacion' | 'libroses'>;

type CategoriaFormGroupContent = {
  id: FormControl<CategoriaFormRawValue['id'] | NewCategoria['id']>;
  nombreCategoria: FormControl<CategoriaFormRawValue['nombreCategoria']>;
  activo: FormControl<CategoriaFormRawValue['activo']>;
  usuarioCreacion: FormControl<CategoriaFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<CategoriaFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<CategoriaFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<CategoriaFormRawValue['fechaModificacion']>;
  libroses: FormControl<CategoriaFormRawValue['libroses']>;
};

export type CategoriaFormGroup = FormGroup<CategoriaFormGroupContent>;

@Service()
export class CategoriaFormService {
  createCategoriaFormGroup(categoria?: CategoriaFormGroupInput): CategoriaFormGroup {
    const categoriaRawValue = this.convertCategoriaToCategoriaRawValue({
      ...this.getFormDefaults(),
      ...(categoria ?? { id: null }),
    });

    return new FormGroup<CategoriaFormGroupContent>({
      id: new FormControl(
        { value: categoriaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombreCategoria: new FormControl(categoriaRawValue.nombreCategoria, {
        validators: [Validators.required],
      }),
      activo: new FormControl(categoriaRawValue.activo, {
        validators: [Validators.required],
      }),
      usuarioCreacion: new FormControl(categoriaRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(categoriaRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(categoriaRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(categoriaRawValue.fechaModificacion),
      libroses: new FormControl(categoriaRawValue.libroses ?? []),
    });
  }

  getCategoria(form: CategoriaFormGroup): ICategoria | NewCategoria {
    return this.convertCategoriaRawValueToCategoria(form.getRawValue());
  }

  resetForm(form: CategoriaFormGroup, categoria: CategoriaFormGroupInput): void {
    const categoriaRawValue = this.convertCategoriaToCategoriaRawValue({ ...this.getFormDefaults(), ...categoria });
    form.reset({
      ...categoriaRawValue,
      id: { value: categoriaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CategoriaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      activo: false,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
      libroses: [],
    };
  }

  private convertCategoriaRawValueToCategoria(rawCategoria: CategoriaFormRawValue | NewCategoriaFormRawValue): ICategoria | NewCategoria {
    return {
      ...rawCategoria,
      fechaCreacion: dayjs(rawCategoria.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawCategoria.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertCategoriaToCategoriaRawValue(
    categoria: ICategoria | (Partial<NewCategoria> & CategoriaFormDefaults),
  ): CategoriaFormRawValue | PartialWithRequiredKeyOf<NewCategoriaFormRawValue> {
    return {
      ...categoria,
      fechaCreacion: categoria.fechaCreacion ? categoria.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: categoria.fechaModificacion ? categoria.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
      libroses: categoria.libroses ?? [],
    };
  }
}
