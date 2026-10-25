import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../rol.test-samples';

import { RolFormService } from './rol-form.service';

describe('Rol Form Service', () => {
  let service: RolFormService;

  beforeEach(() => {
    service = TestBed.inject(RolFormService);
  });

  describe('Service methods', () => {
    describe('createRolFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRolFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigoIdentificador: expect.any(Object),
            nombreRol: expect.any(Object),
            descripcion: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
          }),
        );
      });

      it('passing IRol should create a new form with FormGroup', () => {
        const formGroup = service.createRolFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigoIdentificador: expect.any(Object),
            nombreRol: expect.any(Object),
            descripcion: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
          }),
        );
      });
    });

    describe('getRol', () => {
      it('should return NewRol for default Rol initial value', () => {
        const formGroup = service.createRolFormGroup(sampleWithNewData);

        const rol = service.getRol(formGroup);

        expect(rol).toMatchObject(sampleWithNewData);
      });

      it('should return NewRol for empty Rol initial value', () => {
        const formGroup = service.createRolFormGroup();

        const rol = service.getRol(formGroup);

        expect(rol).toMatchObject({});
      });

      it('should return IRol', () => {
        const formGroup = service.createRolFormGroup(sampleWithRequiredData);

        const rol = service.getRol(formGroup);

        expect(rol).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRol should not enable id FormControl', () => {
        const formGroup = service.createRolFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRol should disable id FormControl', () => {
        const formGroup = service.createRolFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
