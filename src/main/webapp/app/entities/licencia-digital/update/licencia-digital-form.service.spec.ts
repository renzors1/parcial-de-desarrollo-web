import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../licencia-digital.test-samples';

import { LicenciaDigitalFormService } from './licencia-digital-form.service';

describe('LicenciaDigital Form Service', () => {
  let service: LicenciaDigitalFormService;

  beforeEach(() => {
    service = TestBed.inject(LicenciaDigitalFormService);
  });

  describe('Service methods', () => {
    describe('createLicenciaDigitalFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLicenciaDigitalFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigoLicencia: expect.any(Object),
            enlaceDescarga: expect.any(Object),
            fechaAsignacion: expect.any(Object),
            estado: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            libro: expect.any(Object),
            detalleVenta: expect.any(Object),
          }),
        );
      });

      it('passing ILicenciaDigital should create a new form with FormGroup', () => {
        const formGroup = service.createLicenciaDigitalFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigoLicencia: expect.any(Object),
            enlaceDescarga: expect.any(Object),
            fechaAsignacion: expect.any(Object),
            estado: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            libro: expect.any(Object),
            detalleVenta: expect.any(Object),
          }),
        );
      });
    });

    describe('getLicenciaDigital', () => {
      it('should return NewLicenciaDigital for default LicenciaDigital initial value', () => {
        const formGroup = service.createLicenciaDigitalFormGroup(sampleWithNewData);

        const licenciaDigital = service.getLicenciaDigital(formGroup);

        expect(licenciaDigital).toMatchObject(sampleWithNewData);
      });

      it('should return NewLicenciaDigital for empty LicenciaDigital initial value', () => {
        const formGroup = service.createLicenciaDigitalFormGroup();

        const licenciaDigital = service.getLicenciaDigital(formGroup);

        expect(licenciaDigital).toMatchObject({});
      });

      it('should return ILicenciaDigital', () => {
        const formGroup = service.createLicenciaDigitalFormGroup(sampleWithRequiredData);

        const licenciaDigital = service.getLicenciaDigital(formGroup);

        expect(licenciaDigital).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILicenciaDigital should not enable id FormControl', () => {
        const formGroup = service.createLicenciaDigitalFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLicenciaDigital should disable id FormControl', () => {
        const formGroup = service.createLicenciaDigitalFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
