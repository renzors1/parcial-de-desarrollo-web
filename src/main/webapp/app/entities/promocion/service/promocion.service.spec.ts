import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPromocion } from '../promocion.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../promocion.test-samples';

import { PromocionService, RestPromocion } from './promocion.service';

const requireRestSample: RestPromocion = {
  ...sampleWithRequiredData,
  fechaInicio: sampleWithRequiredData.fechaInicio?.toJSON(),
  fechaFin: sampleWithRequiredData.fechaFin?.toJSON(),
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('Promocion Service', () => {
  let service: PromocionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPromocion | IPromocion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PromocionService);
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

    it('should create a Promocion', () => {
      const promocion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(promocion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Promocion', () => {
      const promocion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(promocion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Promocion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Promocion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Promocion', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPromocionToCollectionIfMissing', () => {
      it('should add a Promocion to an empty array', () => {
        const promocion: IPromocion = sampleWithRequiredData;
        expectedResult = service.addPromocionToCollectionIfMissing([], promocion);
        expect(expectedResult).toEqual([promocion]);
      });

      it('should not add a Promocion to an array that contains it', () => {
        const promocion: IPromocion = sampleWithRequiredData;
        const promocionCollection: IPromocion[] = [
          {
            ...promocion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPromocionToCollectionIfMissing(promocionCollection, promocion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Promocion to an array that doesn't contain it", () => {
        const promocion: IPromocion = sampleWithRequiredData;
        const promocionCollection: IPromocion[] = [sampleWithPartialData];
        expectedResult = service.addPromocionToCollectionIfMissing(promocionCollection, promocion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promocion);
      });

      it('should add only unique Promocion to an array', () => {
        const promocionArray: IPromocion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const promocionCollection: IPromocion[] = [sampleWithRequiredData];
        expectedResult = service.addPromocionToCollectionIfMissing(promocionCollection, ...promocionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const promocion: IPromocion = sampleWithRequiredData;
        const promocion2: IPromocion = sampleWithPartialData;
        expectedResult = service.addPromocionToCollectionIfMissing([], promocion, promocion2);
        expect(expectedResult).toEqual([promocion, promocion2]);
      });

      it('should accept null and undefined values', () => {
        const promocion: IPromocion = sampleWithRequiredData;
        expectedResult = service.addPromocionToCollectionIfMissing([], null, promocion, undefined);
        expect(expectedResult).toEqual([promocion]);
      });

      it('should return initial array if no Promocion is added', () => {
        const promocionCollection: IPromocion[] = [sampleWithRequiredData];
        expectedResult = service.addPromocionToCollectionIfMissing(promocionCollection, undefined, null);
        expect(expectedResult).toEqual(promocionCollection);
      });
    });

    describe('comparePromocion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePromocion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28562 };
        const entity2 = null;

        const compareResult1 = service.comparePromocion(entity1, entity2);
        const compareResult2 = service.comparePromocion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28562 };
        const entity2 = { id: 14882 };

        const compareResult1 = service.comparePromocion(entity1, entity2);
        const compareResult2 = service.comparePromocion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 28562 };
        const entity2 = { id: 28562 };

        const compareResult1 = service.comparePromocion(entity1, entity2);
        const compareResult2 = service.comparePromocion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
