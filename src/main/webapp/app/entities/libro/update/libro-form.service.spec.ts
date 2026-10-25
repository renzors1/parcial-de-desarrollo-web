import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../libro.test-samples';

import { LibroFormService } from './libro-form.service';

describe('Libro Form Service', () => {
  let service: LibroFormService;

  beforeEach(() => {
    service = TestBed.inject(LibroFormService);
  });

  describe('Service methods', () => {
    describe('createLibroFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLibroFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isbn: expect.any(Object),
            titulo: expect.any(Object),
            sinopsis: expect.any(Object),
            urlPortada: expect.any(Object),
            fechaPublicacion: expect.any(Object),
            precio: expect.any(Object),
            idioma: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            categoriases: expect.any(Object),
            autoreses: expect.any(Object),
            editorial: expect.any(Object),
            promocioneses: expect.any(Object),
          }),
        );
      });

      it('passing ILibro should create a new form with FormGroup', () => {
        const formGroup = service.createLibroFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isbn: expect.any(Object),
            titulo: expect.any(Object),
            sinopsis: expect.any(Object),
            urlPortada: expect.any(Object),
            fechaPublicacion: expect.any(Object),
            precio: expect.any(Object),
            idioma: expect.any(Object),
            activo: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            categoriases: expect.any(Object),
            autoreses: expect.any(Object),
            editorial: expect.any(Object),
            promocioneses: expect.any(Object),
          }),
        );
      });
    });

    describe('getLibro', () => {
      it('should return NewLibro for default Libro initial value', () => {
        const formGroup = service.createLibroFormGroup(sampleWithNewData);

        const libro = service.getLibro(formGroup);

        expect(libro).toMatchObject(sampleWithNewData);
      });

      it('should return NewLibro for empty Libro initial value', () => {
        const formGroup = service.createLibroFormGroup();

        const libro = service.getLibro(formGroup);

        expect(libro).toMatchObject({});
      });

      it('should return ILibro', () => {
        const formGroup = service.createLibroFormGroup(sampleWithRequiredData);

        const libro = service.getLibro(formGroup);

        expect(libro).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILibro should not enable id FormControl', () => {
        const formGroup = service.createLibroFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLibro should disable id FormControl', () => {
        const formGroup = service.createLibroFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
