import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IRol } from '../rol.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../rol.test-samples';

import { RestRol, RolService } from './rol.service';

const requireRestSample: RestRol = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('Rol Service', () => {
  let service: RolService;
  let httpMock: HttpTestingController;
  let expectedResult: IRol | IRol[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RolService);
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

    it('should create a Rol', () => {
      const rol = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rol).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Rol', () => {
      const rol = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rol).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Rol', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Rol', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Rol', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addRolToCollectionIfMissing', () => {
      it('should add a Rol to an empty array', () => {
        const rol: IRol = sampleWithRequiredData;
        expectedResult = service.addRolToCollectionIfMissing([], rol);
        expect(expectedResult).toEqual([rol]);
      });

      it('should not add a Rol to an array that contains it', () => {
        const rol: IRol = sampleWithRequiredData;
        const rolCollection: IRol[] = [
          {
            ...rol,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRolToCollectionIfMissing(rolCollection, rol);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Rol to an array that doesn't contain it", () => {
        const rol: IRol = sampleWithRequiredData;
        const rolCollection: IRol[] = [sampleWithPartialData];
        expectedResult = service.addRolToCollectionIfMissing(rolCollection, rol);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rol);
      });

      it('should add only unique Rol to an array', () => {
        const rolArray: IRol[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rolCollection: IRol[] = [sampleWithRequiredData];
        expectedResult = service.addRolToCollectionIfMissing(rolCollection, ...rolArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rol: IRol = sampleWithRequiredData;
        const rol2: IRol = sampleWithPartialData;
        expectedResult = service.addRolToCollectionIfMissing([], rol, rol2);
        expect(expectedResult).toEqual([rol, rol2]);
      });

      it('should accept null and undefined values', () => {
        const rol: IRol = sampleWithRequiredData;
        expectedResult = service.addRolToCollectionIfMissing([], null, rol, undefined);
        expect(expectedResult).toEqual([rol]);
      });

      it('should return initial array if no Rol is added', () => {
        const rolCollection: IRol[] = [sampleWithRequiredData];
        expectedResult = service.addRolToCollectionIfMissing(rolCollection, undefined, null);
        expect(expectedResult).toEqual(rolCollection);
      });
    });

    describe('compareRol', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRol(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28479 };
        const entity2 = null;

        const compareResult1 = service.compareRol(entity1, entity2);
        const compareResult2 = service.compareRol(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28479 };
        const entity2 = { id: 1064 };

        const compareResult1 = service.compareRol(entity1, entity2);
        const compareResult2 = service.compareRol(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 28479 };
        const entity2 = { id: 28479 };

        const compareResult1 = service.compareRol(entity1, entity2);
        const compareResult2 = service.compareRol(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
