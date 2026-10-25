import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IPromocion, NewPromocion } from '../promocion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPromocion for edit and NewPromocionFormGroupInput for create.
 */
type PromocionFormGroupInput = IPromocion | PartialWithRequiredKeyOf<NewPromocion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPromocion | NewPromocion> = Omit<T, 'fechaInicio' | 'fechaFin' | 'fechaCreacion' | 'fechaModificacion'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type PromocionFormRawValue = FormValueOf<IPromocion>;

type NewPromocionFormRawValue = FormValueOf<NewPromocion>;

type PromocionFormDefaults = Pick<
  NewPromocion,
  'id' | 'fechaInicio' | 'fechaFin' | 'activo' | 'fechaCreacion' | 'fechaModificacion' | 'libroses'
>;

type PromocionFormGroupContent = {
  id: FormControl<PromocionFormRawValue['id'] | NewPromocion['id']>;
  nombre: FormControl<PromocionFormRawValue['nombre']>;
  descripcion: FormControl<PromocionFormRawValue['descripcion']>;
  porcentajeDescuento: FormControl<PromocionFormRawValue['porcentajeDescuento']>;
  cantidadDefinida: FormControl<PromocionFormRawValue['cantidadDefinida']>;
  cantidadDisponible: FormControl<PromocionFormRawValue['cantidadDisponible']>;
  fechaInicio: FormControl<PromocionFormRawValue['fechaInicio']>;
  fechaFin: FormControl<PromocionFormRawValue['fechaFin']>;
  activo: FormControl<PromocionFormRawValue['activo']>;
  usuarioCreacion: FormControl<PromocionFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<PromocionFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<PromocionFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<PromocionFormRawValue['fechaModificacion']>;
  libroses: FormControl<PromocionFormRawValue['libroses']>;
};

export type PromocionFormGroup = FormGroup<PromocionFormGroupContent>;

@Service()
export class PromocionFormService {
  createPromocionFormGroup(promocion?: PromocionFormGroupInput): PromocionFormGroup {
    const promocionRawValue = this.convertPromocionToPromocionRawValue({
      ...this.getFormDefaults(),
      ...(promocion ?? { id: null }),
    });

    return new FormGroup<PromocionFormGroupContent>({
      id: new FormControl(
        { value: promocionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(promocionRawValue.nombre, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(promocionRawValue.descripcion),
      porcentajeDescuento: new FormControl(promocionRawValue.porcentajeDescuento, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      cantidadDefinida: new FormControl(promocionRawValue.cantidadDefinida, {
        validators: [Validators.required, Validators.min(1)],
      }),
      cantidadDisponible: new FormControl(promocionRawValue.cantidadDisponible, {
        validators: [Validators.required, Validators.min(0)],
      }),
      fechaInicio: new FormControl(promocionRawValue.fechaInicio, {
        validators: [Validators.required],
      }),
      fechaFin: new FormControl(promocionRawValue.fechaFin, {
        validators: [Validators.required],
      }),
      activo: new FormControl(promocionRawValue.activo, {
        validators: [Validators.required],
      }),
      usuarioCreacion: new FormControl(promocionRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(promocionRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(promocionRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(promocionRawValue.fechaModificacion),
      libroses: new FormControl(promocionRawValue.libroses ?? []),
    });
  }

  getPromocion(form: PromocionFormGroup): IPromocion | NewPromocion {
    return this.convertPromocionRawValueToPromocion(form.getRawValue());
  }

  resetForm(form: PromocionFormGroup, promocion: PromocionFormGroupInput): void {
    const promocionRawValue = this.convertPromocionToPromocionRawValue({ ...this.getFormDefaults(), ...promocion });
    form.reset({
      ...promocionRawValue,
      id: { value: promocionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PromocionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaInicio: currentTime,
      fechaFin: currentTime,
      activo: false,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
      libroses: [],
    };
  }

  private convertPromocionRawValueToPromocion(rawPromocion: PromocionFormRawValue | NewPromocionFormRawValue): IPromocion | NewPromocion {
    return {
      ...rawPromocion,
      fechaInicio: dayjs(rawPromocion.fechaInicio, DATE_TIME_FORMAT),
      fechaFin: dayjs(rawPromocion.fechaFin, DATE_TIME_FORMAT),
      fechaCreacion: dayjs(rawPromocion.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawPromocion.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertPromocionToPromocionRawValue(
    promocion: IPromocion | (Partial<NewPromocion> & PromocionFormDefaults),
  ): PromocionFormRawValue | PartialWithRequiredKeyOf<NewPromocionFormRawValue> {
    return {
      ...promocion,
      fechaInicio: promocion.fechaInicio ? promocion.fechaInicio.format(DATE_TIME_FORMAT) : undefined,
      fechaFin: promocion.fechaFin ? promocion.fechaFin.format(DATE_TIME_FORMAT) : undefined,
      fechaCreacion: promocion.fechaCreacion ? promocion.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: promocion.fechaModificacion ? promocion.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
      libroses: promocion.libroses ?? [],
    };
  }
}
