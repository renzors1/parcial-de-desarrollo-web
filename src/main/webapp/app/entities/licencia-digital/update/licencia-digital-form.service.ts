import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ILicenciaDigital, NewLicenciaDigital } from '../licencia-digital.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILicenciaDigital for edit and NewLicenciaDigitalFormGroupInput for create.
 */
type LicenciaDigitalFormGroupInput = ILicenciaDigital | PartialWithRequiredKeyOf<NewLicenciaDigital>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILicenciaDigital | NewLicenciaDigital> = Omit<T, 'fechaAsignacion' | 'fechaCreacion' | 'fechaModificacion'> & {
  fechaAsignacion?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type LicenciaDigitalFormRawValue = FormValueOf<ILicenciaDigital>;

type NewLicenciaDigitalFormRawValue = FormValueOf<NewLicenciaDigital>;

type LicenciaDigitalFormDefaults = Pick<NewLicenciaDigital, 'id' | 'fechaAsignacion' | 'fechaCreacion' | 'fechaModificacion'>;

type LicenciaDigitalFormGroupContent = {
  id: FormControl<LicenciaDigitalFormRawValue['id'] | NewLicenciaDigital['id']>;
  codigoLicencia: FormControl<LicenciaDigitalFormRawValue['codigoLicencia']>;
  enlaceDescarga: FormControl<LicenciaDigitalFormRawValue['enlaceDescarga']>;
  fechaAsignacion: FormControl<LicenciaDigitalFormRawValue['fechaAsignacion']>;
  estado: FormControl<LicenciaDigitalFormRawValue['estado']>;
  usuarioCreacion: FormControl<LicenciaDigitalFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<LicenciaDigitalFormRawValue['fechaCreacion']>;
  usuarioModificacion: FormControl<LicenciaDigitalFormRawValue['usuarioModificacion']>;
  fechaModificacion: FormControl<LicenciaDigitalFormRawValue['fechaModificacion']>;
  libro: FormControl<LicenciaDigitalFormRawValue['libro']>;
  detalleVenta: FormControl<LicenciaDigitalFormRawValue['detalleVenta']>;
};

export type LicenciaDigitalFormGroup = FormGroup<LicenciaDigitalFormGroupContent>;

@Service()
export class LicenciaDigitalFormService {
  createLicenciaDigitalFormGroup(licenciaDigital?: LicenciaDigitalFormGroupInput): LicenciaDigitalFormGroup {
    const licenciaDigitalRawValue = this.convertLicenciaDigitalToLicenciaDigitalRawValue({
      ...this.getFormDefaults(),
      ...(licenciaDigital ?? { id: null }),
    });

    return new FormGroup<LicenciaDigitalFormGroupContent>({
      id: new FormControl(
        { value: licenciaDigitalRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigoLicencia: new FormControl(licenciaDigitalRawValue.codigoLicencia, {
        validators: [Validators.required],
      }),
      enlaceDescarga: new FormControl(licenciaDigitalRawValue.enlaceDescarga),
      fechaAsignacion: new FormControl(licenciaDigitalRawValue.fechaAsignacion),
      estado: new FormControl(licenciaDigitalRawValue.estado, {
        validators: [Validators.required],
      }),
      usuarioCreacion: new FormControl(licenciaDigitalRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(licenciaDigitalRawValue.fechaCreacion),
      usuarioModificacion: new FormControl(licenciaDigitalRawValue.usuarioModificacion),
      fechaModificacion: new FormControl(licenciaDigitalRawValue.fechaModificacion),
      libro: new FormControl(licenciaDigitalRawValue.libro),
      detalleVenta: new FormControl(licenciaDigitalRawValue.detalleVenta),
    });
  }

  getLicenciaDigital(form: LicenciaDigitalFormGroup): ILicenciaDigital | NewLicenciaDigital {
    return this.convertLicenciaDigitalRawValueToLicenciaDigital(form.getRawValue());
  }

  resetForm(form: LicenciaDigitalFormGroup, licenciaDigital: LicenciaDigitalFormGroupInput): void {
    const licenciaDigitalRawValue = this.convertLicenciaDigitalToLicenciaDigitalRawValue({ ...this.getFormDefaults(), ...licenciaDigital });
    form.reset({
      ...licenciaDigitalRawValue,
      id: { value: licenciaDigitalRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): LicenciaDigitalFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaAsignacion: currentTime,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertLicenciaDigitalRawValueToLicenciaDigital(
    rawLicenciaDigital: LicenciaDigitalFormRawValue | NewLicenciaDigitalFormRawValue,
  ): ILicenciaDigital | NewLicenciaDigital {
    return {
      ...rawLicenciaDigital,
      fechaAsignacion: dayjs(rawLicenciaDigital.fechaAsignacion, DATE_TIME_FORMAT),
      fechaCreacion: dayjs(rawLicenciaDigital.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawLicenciaDigital.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertLicenciaDigitalToLicenciaDigitalRawValue(
    licenciaDigital: ILicenciaDigital | (Partial<NewLicenciaDigital> & LicenciaDigitalFormDefaults),
  ): LicenciaDigitalFormRawValue | PartialWithRequiredKeyOf<NewLicenciaDigitalFormRawValue> {
    return {
      ...licenciaDigital,
      fechaAsignacion: licenciaDigital.fechaAsignacion ? licenciaDigital.fechaAsignacion.format(DATE_TIME_FORMAT) : undefined,
      fechaCreacion: licenciaDigital.fechaCreacion ? licenciaDigital.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: licenciaDigital.fechaModificacion ? licenciaDigital.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
