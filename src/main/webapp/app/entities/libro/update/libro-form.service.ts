import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ILibro, NewLibro } from '../libro.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILibro for edit and NewLibroFormGroupInput for create.
 */
type LibroFormGroupInput = ILibro | PartialWithRequiredKeyOf<NewLibro>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILibro | NewLibro> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type LibroFormRawValue = FormValueOf<ILibro>;

type NewLibroFormRawValue = FormValueOf<NewLibro>;

type LibroFormDefaults = Pick<
  NewLibro,
  'id' | 'activo' | 'fechaCreacion' | 'fechaModificacion' | 'categoriases' | 'autoreses' | 'promocioneses'
>;

type LibroFormGroupContent = {
  id: FormControl<LibroFormRawValue['id'] | NewLibro['id']>;
  isbn: FormControl<LibroFormRawValue['isbn']>;
  titulo: FormControl<LibroFormRawValue['titulo']>;
  sinopsis: FormControl<LibroFormRawValue['sinopsis']>;
  urlPortada: FormControl<LibroFormRawValue['urlPortada']>;
  fechaPublicacion: FormControl<LibroFormRawValue['fechaPublicacion']>;
  precio: FormControl<LibroFormRawValue['precio']>;
  idioma: FormControl<LibroFormRawValue['idioma']>;
  activo: FormControl<LibroFormRawValue['activo']>;
  usuarioCreacion: FormControl<LibroFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<LibroFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<LibroFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<LibroFormRawValue['fechaModificacion']>;
  categoriases: FormControl<LibroFormRawValue['categoriases']>;
  autoreses: FormControl<LibroFormRawValue['autoreses']>;
  editorial: FormControl<LibroFormRawValue['editorial']>;
  promocioneses: FormControl<LibroFormRawValue['promocioneses']>;
};

export type LibroFormGroup = FormGroup<LibroFormGroupContent>;

@Service()
export class LibroFormService {
  createLibroFormGroup(libro?: LibroFormGroupInput): LibroFormGroup {
    const libroRawValue = this.convertLibroToLibroRawValue({
      ...this.getFormDefaults(),
      ...(libro ?? { id: null }),
    });

    return new FormGroup<LibroFormGroupContent>({
      id: new FormControl(
        { value: libroRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      isbn: new FormControl(libroRawValue.isbn, {
        validators: [Validators.required],
      }),
      titulo: new FormControl(libroRawValue.titulo, {
        validators: [Validators.required],
      }),
      sinopsis: new FormControl(libroRawValue.sinopsis),
      urlPortada: new FormControl(libroRawValue.urlPortada),
      fechaPublicacion: new FormControl(libroRawValue.fechaPublicacion),
      precio: new FormControl(libroRawValue.precio, {
        validators: [Validators.required, Validators.min(0)],
      }),
      idioma: new FormControl(libroRawValue.idioma, {
        validators: [Validators.required],
      }),
      activo: new FormControl(libroRawValue.activo, {
        validators: [Validators.required],
      }),
      usuarioCreacion: new FormControl(libroRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(libroRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(libroRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(libroRawValue.fechaModificacion),
      categoriases: new FormControl(libroRawValue.categoriases ?? []),
      autoreses: new FormControl(libroRawValue.autoreses ?? []),
      editorial: new FormControl(libroRawValue.editorial),
      promocioneses: new FormControl(libroRawValue.promocioneses ?? []),
    });
  }

  getLibro(form: LibroFormGroup): ILibro | NewLibro {
    return this.convertLibroRawValueToLibro(form.getRawValue());
  }

  resetForm(form: LibroFormGroup, libro: LibroFormGroupInput): void {
    const libroRawValue = this.convertLibroToLibroRawValue({ ...this.getFormDefaults(), ...libro });
    form.reset({
      ...libroRawValue,
      id: { value: libroRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): LibroFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      activo: false,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
      categoriases: [],
      autoreses: [],
      promocioneses: [],
    };
  }

  private convertLibroRawValueToLibro(rawLibro: LibroFormRawValue | NewLibroFormRawValue): ILibro | NewLibro {
    return {
      ...rawLibro,
      fechaCreacion: dayjs(rawLibro.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawLibro.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertLibroToLibroRawValue(
    libro: ILibro | (Partial<NewLibro> & LibroFormDefaults),
  ): LibroFormRawValue | PartialWithRequiredKeyOf<NewLibroFormRawValue> {
    return {
      ...libro,
      fechaCreacion: libro.fechaCreacion ? libro.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: libro.fechaModificacion ? libro.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
      categoriases: libro.categoriases ?? [],
      autoreses: libro.autoreses ?? [],
      promocioneses: libro.promocioneses ?? [],
    };
  }
}
