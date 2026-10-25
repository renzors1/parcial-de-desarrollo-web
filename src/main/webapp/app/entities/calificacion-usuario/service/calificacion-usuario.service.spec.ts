import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICalificacionUsuario } from '../calificacion-usuario.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../calificacion-usuario.test-samples';

import { CalificacionUsuarioService, RestCalificacionUsuario } from './calificacion-usuario.service';

const requireRestSample: RestCalificacionUsuario = {
  ...sampleWithRequiredData,
  fechaCalificacion: sampleWithRequiredData.fechaCalificacion?.toJSON(),
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('CalificacionUsuario Service', () => {
  let service: CalificacionUsuarioService;
  let httpMock: HttpTestingController;
  let expectedResult: ICalificacionUsuario | ICalificacionUsuario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CalificacionUsuarioService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CalificacionUsuario', () => {
      const calificacionUsuario = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(calificacionUsuario).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CalificacionUsuario', () => {
      const calificacionUsuario = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(calificacionUsuario).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CalificacionUsuario', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CalificacionUsuario', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CalificacionUsuario', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCalificacionUsuarioToCollectionIfMissing', () => {
      it('should add a CalificacionUsuario to an empty array', () => {
        const calificacionUsuario: ICalificacionUsuario = sampleWithRequiredData;
        expectedResult = service.addCalificacionUsuarioToCollectionIfMissing([], calificacionUsuario);
        expect(expectedResult).toEqual([calificacionUsuario]);
      });

      it('should not add a CalificacionUsuario to an array that contains it', () => {
        const calificacionUsuario: ICalificacionUsuario = sampleWithRequiredData;
        const calificacionUsuarioCollection: ICalificacionUsuario[] = [
          {
            ...calificacionUsuario,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCalificacionUsuarioToCollectionIfMissing(calificacionUsuarioCollection, calificacionUsuario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CalificacionUsuario to an array that doesn't contain it", () => {
        const calificacionUsuario: ICalificacionUsuario = sampleWithRequiredData;
        const calificacionUsuarioCollection: ICalificacionUsuario[] = [sampleWithPartialData];
        expectedResult = service.addCalificacionUsuarioToCollectionIfMissing(calificacionUsuarioCollection, calificacionUsuario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(calificacionUsuario);
      });

      it('should add only unique CalificacionUsuario to an array', () => {
        const calificacionUsuarioArray: ICalificacionUsuario[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const calificacionUsuarioCollection: ICalificacionUsuario[] = [sampleWithRequiredData];
        expectedResult = service.addCalificacionUsuarioToCollectionIfMissing(calificacionUsuarioCollection, ...calificacionUsuarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const calificacionUsuario: ICalificacionUsuario = sampleWithRequiredData;
        const calificacionUsuario2: ICalificacionUsuario = sampleWithPartialData;
        expectedResult = service.addCalificacionUsuarioToCollectionIfMissing([], calificacionUsuario, calificacionUsuario2);
        expect(expectedResult).toEqual([calificacionUsuario, calificacionUsuario2]);
      });

      it('should accept null and undefined values', () => {
        const calificacionUsuario: ICalificacionUsuario = sampleWithRequiredData;
        expectedResult = service.addCalificacionUsuarioToCollectionIfMissing([], null, calificacionUsuario, undefined);
        expect(expectedResult).toEqual([calificacionUsuario]);
      });

      it('should return initial array if no CalificacionUsuario is added', () => {
        const calificacionUsuarioCollection: ICalificacionUsuario[] = [sampleWithRequiredData];
        expectedResult = service.addCalificacionUsuarioToCollectionIfMissing(calificacionUsuarioCollection, undefined, null);
        expect(expectedResult).toEqual(calificacionUsuarioCollection);
      });
    });

    describe('compareCalificacionUsuario', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCalificacionUsuario(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8270 };
        const entity2 = null;

        const compareResult1 = service.compareCalificacionUsuario(entity1, entity2);
        const compareResult2 = service.compareCalificacionUsuario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8270 };
        const entity2 = { id: 4870 };

        const compareResult1 = service.compareCalificacionUsuario(entity1, entity2);
        const compareResult2 = service.compareCalificacionUsuario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 8270 };
        const entity2 = { id: 8270 };

        const compareResult1 = service.compareCalificacionUsuario(entity1, entity2);
        const compareResult2 = service.compareCalificacionUsuario(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
