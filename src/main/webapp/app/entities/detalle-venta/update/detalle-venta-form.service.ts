import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IDetalleVenta, NewDetalleVenta } from '../detalle-venta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDetalleVenta for edit and NewDetalleVentaFormGroupInput for create.
 */
type DetalleVentaFormGroupInput = IDetalleVenta | PartialWithRequiredKeyOf<NewDetalleVenta>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDetalleVenta | NewDetalleVenta> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type DetalleVentaFormRawValue = FormValueOf<IDetalleVenta>;

type NewDetalleVentaFormRawValue = FormValueOf<NewDetalleVenta>;

type DetalleVentaFormDefaults = Pick<NewDetalleVenta, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type DetalleVentaFormGroupContent = {
  id: FormControl<DetalleVentaFormRawValue['id'] | NewDetalleVenta['id']>;
  cantidadVenta: FormControl<DetalleVentaFormRawValue['cantidadVenta']>;
  precioUnitario: FormControl<DetalleVentaFormRawValue['precioUnitario']>;
  descuento: FormControl<DetalleVentaFormRawValue['descuento']>;
  subtotal: FormControl<DetalleVentaFormRawValue['subtotal']>;
  usuarioCreacion: FormControl<DetalleVentaFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<DetalleVentaFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<DetalleVentaFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<DetalleVentaFormRawValue['fechaModificacion']>;
  venta: FormControl<DetalleVentaFormRawValue['venta']>;
  libro: FormControl<DetalleVentaFormRawValue['libro']>;
  promocionAplicada: FormControl<DetalleVentaFormRawValue['promocionAplicada']>;
};

export type DetalleVentaFormGroup = FormGroup<DetalleVentaFormGroupContent>;

@Service()
export class DetalleVentaFormService {
  createDetalleVentaFormGroup(detalleVenta?: DetalleVentaFormGroupInput): DetalleVentaFormGroup {
    const detalleVentaRawValue = this.convertDetalleVentaToDetalleVentaRawValue({
      ...this.getFormDefaults(),
      ...(detalleVenta ?? { id: null }),
    });

    return new FormGroup<DetalleVentaFormGroupContent>({
      id: new FormControl(
        { value: detalleVentaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cantidadVenta: new FormControl(detalleVentaRawValue.cantidadVenta, {
        validators: [Validators.required, Validators.min(1)],
      }),
      precioUnitario: new FormControl(detalleVentaRawValue.precioUnitario, {
        validators: [Validators.required, Validators.min(0)],
      }),
      descuento: new FormControl(detalleVentaRawValue.descuento, {
        validators: [Validators.required, Validators.min(0)],
      }),
      subtotal: new FormControl(detalleVentaRawValue.subtotal, {
        validators: [Validators.required, Validators.min(0)],
      }),
      usuarioCreacion: new FormControl(detalleVentaRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(detalleVentaRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(detalleVentaRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(detalleVentaRawValue.fechaModificacion),
      venta: new FormControl(detalleVentaRawValue.venta),
      libro: new FormControl(detalleVentaRawValue.libro),
      promocionAplicada: new FormControl(detalleVentaRawValue.promocionAplicada),
    });
  }

  getDetalleVenta(form: DetalleVentaFormGroup): IDetalleVenta | NewDetalleVenta {
    return this.convertDetalleVentaRawValueToDetalleVenta(form.getRawValue());
  }

  resetForm(form: DetalleVentaFormGroup, detalleVenta: DetalleVentaFormGroupInput): void {
    const detalleVentaRawValue = this.convertDetalleVentaToDetalleVentaRawValue({ ...this.getFormDefaults(), ...detalleVenta });
    form.reset({
      ...detalleVentaRawValue,
      id: { value: detalleVentaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DetalleVentaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertDetalleVentaRawValueToDetalleVenta(
    rawDetalleVenta: DetalleVentaFormRawValue | NewDetalleVentaFormRawValue,
  ): IDetalleVenta | NewDetalleVenta {
    return {
      ...rawDetalleVenta,
      fechaCreacion: dayjs(rawDetalleVenta.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawDetalleVenta.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertDetalleVentaToDetalleVentaRawValue(
    detalleVenta: IDetalleVenta | (Partial<NewDetalleVenta> & DetalleVentaFormDefaults),
  ): DetalleVentaFormRawValue | PartialWithRequiredKeyOf<NewDetalleVentaFormRawValue> {
    return {
      ...detalleVenta,
      fechaCreacion: detalleVenta.fechaCreacion ? detalleVenta.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: detalleVenta.fechaModificacion ? detalleVenta.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
