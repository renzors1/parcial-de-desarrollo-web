import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IVenta, NewVenta } from '../venta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVenta for edit and NewVentaFormGroupInput for create.
 */
type VentaFormGroupInput = IVenta | PartialWithRequiredKeyOf<NewVenta>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVenta | NewVenta> = Omit<T, 'fechaVenta' | 'fechaCreacion' | 'fechaModificacion'> & {
  fechaVenta?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type VentaFormRawValue = FormValueOf<IVenta>;

type NewVentaFormRawValue = FormValueOf<NewVenta>;

type VentaFormDefaults = Pick<NewVenta, 'id' | 'fechaVenta' | 'fechaCreacion' | 'fechaModificacion'>;

type VentaFormGroupContent = {
  id: FormControl<VentaFormRawValue['id'] | NewVenta['id']>;
  numeroComprobante: FormControl<VentaFormRawValue['numeroComprobante']>;
  fechaVenta: FormControl<VentaFormRawValue['fechaVenta']>;
  subtotalVenta: FormControl<VentaFormRawValue['subtotalVenta']>;
  totalDescuento: FormControl<VentaFormRawValue['totalDescuento']>;
  igv: FormControl<VentaFormRawValue['igv']>;
  totalVenta: FormControl<VentaFormRawValue['totalVenta']>;
  usuarioCreacion: FormControl<VentaFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<VentaFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<VentaFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<VentaFormRawValue['fechaModificacion']>;
  cliente: FormControl<VentaFormRawValue['cliente']>;
};

export type VentaFormGroup = FormGroup<VentaFormGroupContent>;

@Service()
export class VentaFormService {
  createVentaFormGroup(venta?: VentaFormGroupInput): VentaFormGroup {
    const ventaRawValue = this.convertVentaToVentaRawValue({
      ...this.getFormDefaults(),
      ...(venta ?? { id: null }),
    });

    return new FormGroup<VentaFormGroupContent>({
      id: new FormControl(
        { value: ventaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numeroComprobante: new FormControl(ventaRawValue.numeroComprobante, {
        validators: [Validators.required],
      }),
      fechaVenta: new FormControl(ventaRawValue.fechaVenta, {
        validators: [Validators.required],
      }),
      subtotalVenta: new FormControl(ventaRawValue.subtotalVenta, {
        validators: [Validators.required, Validators.min(0)],
      }),
      totalDescuento: new FormControl(ventaRawValue.totalDescuento, {
        validators: [Validators.required, Validators.min(0)],
      }),
      igv: new FormControl(ventaRawValue.igv, {
        validators: [Validators.required, Validators.min(0)],
      }),
      totalVenta: new FormControl(ventaRawValue.totalVenta, {
        validators: [Validators.required, Validators.min(0)],
      }),
      usuarioCreacion: new FormControl(ventaRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(ventaRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(ventaRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(ventaRawValue.fechaModificacion),
      cliente: new FormControl(ventaRawValue.cliente),
    });
  }

  getVenta(form: VentaFormGroup): IVenta | NewVenta {
    return this.convertVentaRawValueToVenta(form.getRawValue());
  }

  resetForm(form: VentaFormGroup, venta: VentaFormGroupInput): void {
    const ventaRawValue = this.convertVentaToVentaRawValue({ ...this.getFormDefaults(), ...venta });
    form.reset({
      ...ventaRawValue,
      id: { value: ventaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): VentaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaVenta: currentTime,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertVentaRawValueToVenta(rawVenta: VentaFormRawValue | NewVentaFormRawValue): IVenta | NewVenta {
    return {
      ...rawVenta,
      fechaVenta: dayjs(rawVenta.fechaVenta, DATE_TIME_FORMAT),
      fechaCreacion: dayjs(rawVenta.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawVenta.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertVentaToVentaRawValue(
    venta: IVenta | (Partial<NewVenta> & VentaFormDefaults),
  ): VentaFormRawValue | PartialWithRequiredKeyOf<NewVentaFormRawValue> {
    return {
      ...venta,
      fechaVenta: venta.fechaVenta ? venta.fechaVenta.format(DATE_TIME_FORMAT) : undefined,
      fechaCreacion: venta.fechaCreacion ? venta.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: venta.fechaModificacion ? venta.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
