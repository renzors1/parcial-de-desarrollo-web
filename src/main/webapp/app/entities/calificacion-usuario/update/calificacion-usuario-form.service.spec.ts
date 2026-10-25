import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../calificacion-usuario.test-samples';

import { CalificacionUsuarioFormService } from './calificacion-usuario-form.service';

describe('CalificacionUsuario Form Service', () => {
  let service: CalificacionUsuarioFormService;

  beforeEach(() => {
    service = TestBed.inject(CalificacionUsuarioFormService);
  });

  describe('Service methods', () => {
    describe('createCalificacionUsuarioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCalificacionUsuarioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            puntuacion: expect.any(Object),
            comentario: expect.any(Object),
            fechaCalificacion: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            clienteEvaluado: expect.any(Object),
            evaluadorTienda: expect.any(Object),
            venta: expect.any(Object),
          }),
        );
      });

      it('passing ICalificacionUsuario should create a new form with FormGroup', () => {
        const formGroup = service.createCalificacionUsuarioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            puntuacion: expect.any(Object),
            comentario: expect.any(Object),
            fechaCalificacion: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            clienteEvaluado: expect.any(Object),
            evaluadorTienda: expect.any(Object),
            venta: expect.any(Object),
          }),
        );
      });
    });

    describe('getCalificacionUsuario', () => {
      it('should return NewCalificacionUsuario for default CalificacionUsuario initial value', () => {
        const formGroup = service.createCalificacionUsuarioFormGroup(sampleWithNewData);

        const calificacionUsuario = service.getCalificacionUsuario(formGroup);

        expect(calificacionUsuario).toMatchObject(sampleWithNewData);
      });

      it('should return NewCalificacionUsuario for empty CalificacionUsuario initial value', () => {
        const formGroup = service.createCalificacionUsuarioFormGroup();

        const calificacionUsuario = service.getCalificacionUsuario(formGroup);

        expect(calificacionUsuario).toMatchObject({});
      });

      it('should return ICalificacionUsuario', () => {
        const formGroup = service.createCalificacionUsuarioFormGroup(sampleWithRequiredData);

        const calificacionUsuario = service.getCalificacionUsuario(formGroup);

        expect(calificacionUsuario).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICalificacionUsuario should not enable id FormControl', () => {
        const formGroup = service.createCalificacionUsuarioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCalificacionUsuario should disable id FormControl', () => {
        const formGroup = service.createCalificacionUsuarioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
