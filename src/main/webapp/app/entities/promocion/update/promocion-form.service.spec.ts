import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../promocion.test-samples';

import { PromocionFormService } from './promocion-form.service';

describe('Promocion Form Service', () => {
  let service: PromocionFormService;

  beforeEach(() => {
    service = TestBed.inject(PromocionFormService);
  });

  describe('Service methods', () => {
    describe('createPromocionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPromocionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            porcentajeDescuento: expect.any(Object),
            cantidadDefinida: expect.any(Object),
            cantidadDisponible: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            libroses: expect.any(Object),
          }),
        );
      });

      it('passing IPromocion should create a new form with FormGroup', () => {
        const formGroup = service.createPromocionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            porcentajeDescuento: expect.any(Object),
            cantidadDefinida: expect.any(Object),
            cantidadDisponible: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            libroses: expect.any(Object),
          }),
        );
      });
    });

    describe('getPromocion', () => {
      it('should return NewPromocion for default Promocion initial value', () => {
        const formGroup = service.createPromocionFormGroup(sampleWithNewData);

        const promocion = service.getPromocion(formGroup);

        expect(promocion).toMatchObject(sampleWithNewData);
      });

      it('should return NewPromocion for empty Promocion initial value', () => {
        const formGroup = service.createPromocionFormGroup();

        const promocion = service.getPromocion(formGroup);

        expect(promocion).toMatchObject({});
      });

      it('should return IPromocion', () => {
        const formGroup = service.createPromocionFormGroup(sampleWithRequiredData);

        const promocion = service.getPromocion(formGroup);

        expect(promocion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPromocion should not enable id FormControl', () => {
        const formGroup = service.createPromocionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPromocion should disable id FormControl', () => {
        const formGroup = service.createPromocionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
