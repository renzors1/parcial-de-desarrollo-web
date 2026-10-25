import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IUsuario, NewUsuario } from '../usuario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUsuario for edit and NewUsuarioFormGroupInput for create.
 */
type UsuarioFormGroupInput = IUsuario | PartialWithRequiredKeyOf<NewUsuario>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUsuario | NewUsuario> = Omit<T, 'fechaRegistro' | 'fechaCreacion' | 'fechaModificacion'> & {
  fechaRegistro?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type UsuarioFormRawValue = FormValueOf<IUsuario>;

type NewUsuarioFormRawValue = FormValueOf<NewUsuario>;

type UsuarioFormDefaults = Pick<NewUsuario, 'id' | 'fechaRegistro' | 'activo' | 'fechaCreacion' | 'fechaModificacion'>;

type UsuarioFormGroupContent = {
  id: FormControl<UsuarioFormRawValue['id'] | NewUsuario['id']>;
  nombre: FormControl<UsuarioFormRawValue['nombre']>;
  apellido: FormControl<UsuarioFormRawValue['apellido']>;
  email: FormControl<UsuarioFormRawValue['email']>;
  contrasena: FormControl<UsuarioFormRawValue['contrasena']>;
  direccion: FormControl<UsuarioFormRawValue['direccion']>;
  celular: FormControl<UsuarioFormRawValue['celular']>;
  fechaRegistro: FormControl<UsuarioFormRawValue['fechaRegistro']>;
  calificacionPromedio: FormControl<UsuarioFormRawValue['calificacionPromedio']>;
  activo: FormControl<UsuarioFormRawValue['activo']>;
  usuarioCreacion: FormControl<UsuarioFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<UsuarioFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<UsuarioFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<UsuarioFormRawValue['fechaModificacion']>;
  rol: FormControl<UsuarioFormRawValue['rol']>;
};

export type UsuarioFormGroup = FormGroup<UsuarioFormGroupContent>;

@Service()
export class UsuarioFormService {
  createUsuarioFormGroup(usuario?: UsuarioFormGroupInput): UsuarioFormGroup {
    const usuarioRawValue = this.convertUsuarioToUsuarioRawValue({
      ...this.getFormDefaults(),
      ...(usuario ?? { id: null }),
    });

    return new FormGroup<UsuarioFormGroupContent>({
      id: new FormControl(
        { value: usuarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(usuarioRawValue.nombre, {
        validators: [Validators.required],
      }),
      apellido: new FormControl(usuarioRawValue.apellido, {
        validators: [Validators.required],
      }),
      email: new FormControl(usuarioRawValue.email, {
        validators: [Validators.required],
      }),
      contrasena: new FormControl(usuarioRawValue.contrasena, {
        validators: [Validators.required],
      }),
      direccion: new FormControl(usuarioRawValue.direccion),
      celular: new FormControl(usuarioRawValue.celular),
      fechaRegistro: new FormControl(usuarioRawValue.fechaRegistro, {
        validators: [Validators.required],
      }),
      calificacionPromedio: new FormControl(usuarioRawValue.calificacionPromedio, {
        validators: [Validators.min(0), Validators.max(5)],
      }),
      activo: new FormControl(usuarioRawValue.activo, {
        validators: [Validators.required],
      }),
      usuarioCreacion: new FormControl(usuarioRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(usuarioRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(usuarioRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(usuarioRawValue.fechaModificacion),
      rol: new FormControl(usuarioRawValue.rol),
    });
  }

  getUsuario(form: UsuarioFormGroup): IUsuario | NewUsuario {
    return this.convertUsuarioRawValueToUsuario(form.getRawValue());
  }

  resetForm(form: UsuarioFormGroup, usuario: UsuarioFormGroupInput): void {
    const usuarioRawValue = this.convertUsuarioToUsuarioRawValue({ ...this.getFormDefaults(), ...usuario });
    form.reset({
      ...usuarioRawValue,
      id: { value: usuarioRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): UsuarioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaRegistro: currentTime,
      activo: false,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertUsuarioRawValueToUsuario(rawUsuario: UsuarioFormRawValue | NewUsuarioFormRawValue): IUsuario | NewUsuario {
    return {
      ...rawUsuario,
      fechaRegistro: dayjs(rawUsuario.fechaRegistro, DATE_TIME_FORMAT),
      fechaCreacion: dayjs(rawUsuario.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawUsuario.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertUsuarioToUsuarioRawValue(
    usuario: IUsuario | (Partial<NewUsuario> & UsuarioFormDefaults),
  ): UsuarioFormRawValue | PartialWithRequiredKeyOf<NewUsuarioFormRawValue> {
    return {
      ...usuario,
      fechaRegistro: usuario.fechaRegistro ? usuario.fechaRegistro.format(DATE_TIME_FORMAT) : undefined,
      fechaCreacion: usuario.fechaCreacion ? usuario.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: usuario.fechaModificacion ? usuario.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
