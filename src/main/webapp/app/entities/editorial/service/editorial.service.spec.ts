import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IEditorial } from '../editorial.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../editorial.test-samples';

import { EditorialService, RestEditorial } from './editorial.service';

const requireRestSample: RestEditorial = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('Editorial Service', () => {
  let service: EditorialService;
  let httpMock: HttpTestingController;
  let expectedResult: IEditorial | IEditorial[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EditorialService);
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

    it('should create a Editorial', () => {
      const editorial = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(editorial).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Editorial', () => {
      const editorial = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(editorial).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Editorial', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Editorial', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Editorial', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addEditorialToCollectionIfMissing', () => {
      it('should add a Editorial to an empty array', () => {
        const editorial: IEditorial = sampleWithRequiredData;
        expectedResult = service.addEditorialToCollectionIfMissing([], editorial);
        expect(expectedResult).toEqual([editorial]);
      });

      it('should not add a Editorial to an array that contains it', () => {
        const editorial: IEditorial = sampleWithRequiredData;
        const editorialCollection: IEditorial[] = [
          {
            ...editorial,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEditorialToCollectionIfMissing(editorialCollection, editorial);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Editorial to an array that doesn't contain it", () => {
        const editorial: IEditorial = sampleWithRequiredData;
        const editorialCollection: IEditorial[] = [sampleWithPartialData];
        expectedResult = service.addEditorialToCollectionIfMissing(editorialCollection, editorial);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(editorial);
      });

      it('should add only unique Editorial to an array', () => {
        const editorialArray: IEditorial[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const editorialCollection: IEditorial[] = [sampleWithRequiredData];
        expectedResult = service.addEditorialToCollectionIfMissing(editorialCollection, ...editorialArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const editorial: IEditorial = sampleWithRequiredData;
        const editorial2: IEditorial = sampleWithPartialData;
        expectedResult = service.addEditorialToCollectionIfMissing([], editorial, editorial2);
        expect(expectedResult).toEqual([editorial, editorial2]);
      });

      it('should accept null and undefined values', () => {
        const editorial: IEditorial = sampleWithRequiredData;
        expectedResult = service.addEditorialToCollectionIfMissing([], null, editorial, undefined);
        expect(expectedResult).toEqual([editorial]);
      });

      it('should return initial array if no Editorial is added', () => {
        const editorialCollection: IEditorial[] = [sampleWithRequiredData];
        expectedResult = service.addEditorialToCollectionIfMissing(editorialCollection, undefined, null);
        expect(expectedResult).toEqual(editorialCollection);
      });
    });

    describe('compareEditorial', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEditorial(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12270 };
        const entity2 = null;

        const compareResult1 = service.compareEditorial(entity1, entity2);
        const compareResult2 = service.compareEditorial(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12270 };
        const entity2 = { id: 5819 };

        const compareResult1 = service.compareEditorial(entity1, entity2);
        const compareResult2 = service.compareEditorial(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 12270 };
        const entity2 = { id: 12270 };

        const compareResult1 = service.compareEditorial(entity1, entity2);
        const compareResult2 = service.compareEditorial(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
