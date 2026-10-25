import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ICalificacionUsuario, NewCalificacionUsuario } from '../calificacion-usuario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICalificacionUsuario for edit and NewCalificacionUsuarioFormGroupInput for create.
 */
type CalificacionUsuarioFormGroupInput = ICalificacionUsuario | PartialWithRequiredKeyOf<NewCalificacionUsuario>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICalificacionUsuario | NewCalificacionUsuario> = Omit<
  T,
  'fechaCalificacion' | 'fechaCreacion' | 'fechaModificacion'
> & {
  fechaCalificacion?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type CalificacionUsuarioFormRawValue = FormValueOf<ICalificacionUsuario>;

type NewCalificacionUsuarioFormRawValue = FormValueOf<NewCalificacionUsuario>;

type CalificacionUsuarioFormDefaults = Pick<
  NewCalificacionUsuario,
  'id' | 'fechaCalificacion' | 'activo' | 'fechaCreacion' | 'fechaModificacion'
>;

type CalificacionUsuarioFormGroupContent = {
  id: FormControl<CalificacionUsuarioFormRawValue['id'] | NewCalificacionUsuario['id']>;
  puntuacion: FormControl<CalificacionUsuarioFormRawValue['puntuacion']>;
  comentario: FormControl<CalificacionUsuarioFormRawValue['comentario']>;
  fechaCalificacion: FormControl<CalificacionUsuarioFormRawValue['fechaCalificacion']>;
  activo: FormControl<CalificacionUsuarioFormRawValue['activo']>;
  usuarioCreacion: FormControl<CalificacionUsuarioFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<CalificacionUsuarioFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<CalificacionUsuarioFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<CalificacionUsuarioFormRawValue['fechaModificacion']>;
  clienteEvaluado: FormControl<CalificacionUsuarioFormRawValue['clienteEvaluado']>;
  evaluadorTienda: FormControl<CalificacionUsuarioFormRawValue['evaluadorTienda']>;
  venta: FormControl<CalificacionUsuarioFormRawValue['venta']>;
};

export type CalificacionUsuarioFormGroup = FormGroup<CalificacionUsuarioFormGroupContent>;

@Service()
export class CalificacionUsuarioFormService {
  createCalificacionUsuarioFormGroup(calificacionUsuario?: CalificacionUsuarioFormGroupInput): CalificacionUsuarioFormGroup {
    const calificacionUsuarioRawValue = this.convertCalificacionUsuarioToCalificacionUsuarioRawValue({
      ...this.getFormDefaults(),
      ...(calificacionUsuario ?? { id: null }),
    });

    return new FormGroup<CalificacionUsuarioFormGroupContent>({
      id: new FormControl(
        { value: calificacionUsuarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      puntuacion: new FormControl(calificacionUsuarioRawValue.puntuacion, {
        validators: [Validators.required, Validators.min(1), Validators.max(5)],
      }),
      comentario: new FormControl(calificacionUsuarioRawValue.comentario, {
        validators: [Validators.maxLength(500)],
      }),
      fechaCalificacion: new FormControl(calificacionUsuarioRawValue.fechaCalificacion, {
        validators: [Validators.required],
      }),
      activo: new FormControl(calificacionUsuarioRawValue.activo, {
        validators: [Validators.required],
      }),
      usuarioCreacion: new FormControl(calificacionUsuarioRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(calificacionUsuarioRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(calificacionUsuarioRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(calificacionUsuarioRawValue.fechaModificacion),
      clienteEvaluado: new FormControl(calificacionUsuarioRawValue.clienteEvaluado, {
        validators: [Validators.required],
      }),
      evaluadorTienda: new FormControl(calificacionUsuarioRawValue.evaluadorTienda),
      venta: new FormControl(calificacionUsuarioRawValue.venta),
    });
  }

  getCalificacionUsuario(form: CalificacionUsuarioFormGroup): ICalificacionUsuario | NewCalificacionUsuario {
    return this.convertCalificacionUsuarioRawValueToCalificacionUsuario(form.getRawValue());
  }

  resetForm(form: CalificacionUsuarioFormGroup, calificacionUsuario: CalificacionUsuarioFormGroupInput): void {
    const calificacionUsuarioRawValue = this.convertCalificacionUsuarioToCalificacionUsuarioRawValue({
      ...this.getFormDefaults(),
      ...calificacionUsuario,
    });
    form.reset({
      ...calificacionUsuarioRawValue,
      id: { value: calificacionUsuarioRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CalificacionUsuarioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCalificacion: currentTime,
      activo: false,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertCalificacionUsuarioRawValueToCalificacionUsuario(
    rawCalificacionUsuario: CalificacionUsuarioFormRawValue | NewCalificacionUsuarioFormRawValue,
  ): ICalificacionUsuario | NewCalificacionUsuario {
    return {
      ...rawCalificacionUsuario,
      fechaCalificacion: dayjs(rawCalificacionUsuario.fechaCalificacion, DATE_TIME_FORMAT),
      fechaCreacion: dayjs(rawCalificacionUsuario.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawCalificacionUsuario.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertCalificacionUsuarioToCalificacionUsuarioRawValue(
    calificacionUsuario: ICalificacionUsuario | (Partial<NewCalificacionUsuario> & CalificacionUsuarioFormDefaults),
  ): CalificacionUsuarioFormRawValue | PartialWithRequiredKeyOf<NewCalificacionUsuarioFormRawValue> {
    return {
      ...calificacionUsuario,
      fechaCalificacion: calificacionUsuario.fechaCalificacion ? calificacionUsuario.fechaCalificacion.format(DATE_TIME_FORMAT) : undefined,
      fechaCreacion: calificacionUsuario.fechaCreacion ? calificacionUsuario.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: calificacionUsuario.fechaModificacion ? calificacionUsuario.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
