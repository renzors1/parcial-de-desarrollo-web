import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../editorial.test-samples';

import { EditorialFormService } from './editorial-form.service';

describe('Editorial Form Service', () => {
  let service: EditorialFormService;

  beforeEach(() => {
    service = TestBed.inject(EditorialFormService);
  });

  describe('Service methods', () => {
    describe('createEditorialFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEditorialFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreEditorial: expect.any(Object),
            pais: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
          }),
        );
      });

      it('passing IEditorial should create a new form with FormGroup', () => {
        const formGroup = service.createEditorialFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreEditorial: expect.any(Object),
            pais: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
          }),
        );
      });
    });

    describe('getEditorial', () => {
      it('should return NewEditorial for default Editorial initial value', () => {
        const formGroup = service.createEditorialFormGroup(sampleWithNewData);

        const editorial = service.getEditorial(formGroup);

        expect(editorial).toMatchObject(sampleWithNewData);
      });

      it('should return NewEditorial for empty Editorial initial value', () => {
        const formGroup = service.createEditorialFormGroup();

        const editorial = service.getEditorial(formGroup);

        expect(editorial).toMatchObject({});
      });

      it('should return IEditorial', () => {
        const formGroup = service.createEditorialFormGroup(sampleWithRequiredData);

        const editorial = service.getEditorial(formGroup);

        expect(editorial).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEditorial should not enable id FormControl', () => {
        const formGroup = service.createEditorialFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEditorial should disable id FormControl', () => {
        const formGroup = service.createEditorialFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
