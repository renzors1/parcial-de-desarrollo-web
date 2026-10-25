import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ILicenciaDigital } from '../licencia-digital.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../licencia-digital.test-samples';

import { LicenciaDigitalService, RestLicenciaDigital } from './licencia-digital.service';

const requireRestSample: RestLicenciaDigital = {
  ...sampleWithRequiredData,
  fechaAsignacion: sampleWithRequiredData.fechaAsignacion?.toJSON(),
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('LicenciaDigital Service', () => {
  let service: LicenciaDigitalService;
  let httpMock: HttpTestingController;
  let expectedResult: ILicenciaDigital | ILicenciaDigital[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LicenciaDigitalService);
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

    it('should create a LicenciaDigital', () => {
      const licenciaDigital = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(licenciaDigital).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LicenciaDigital', () => {
      const licenciaDigital = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(licenciaDigital).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LicenciaDigital', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LicenciaDigital', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LicenciaDigital', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addLicenciaDigitalToCollectionIfMissing', () => {
      it('should add a LicenciaDigital to an empty array', () => {
        const licenciaDigital: ILicenciaDigital = sampleWithRequiredData;
        expectedResult = service.addLicenciaDigitalToCollectionIfMissing([], licenciaDigital);
        expect(expectedResult).toEqual([licenciaDigital]);
      });

      it('should not add a LicenciaDigital to an array that contains it', () => {
        const licenciaDigital: ILicenciaDigital = sampleWithRequiredData;
        const licenciaDigitalCollection: ILicenciaDigital[] = [
          {
            ...licenciaDigital,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLicenciaDigitalToCollectionIfMissing(licenciaDigitalCollection, licenciaDigital);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LicenciaDigital to an array that doesn't contain it", () => {
        const licenciaDigital: ILicenciaDigital = sampleWithRequiredData;
        const licenciaDigitalCollection: ILicenciaDigital[] = [sampleWithPartialData];
        expectedResult = service.addLicenciaDigitalToCollectionIfMissing(licenciaDigitalCollection, licenciaDigital);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(licenciaDigital);
      });

      it('should add only unique LicenciaDigital to an array', () => {
        const licenciaDigitalArray: ILicenciaDigital[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const licenciaDigitalCollection: ILicenciaDigital[] = [sampleWithRequiredData];
        expectedResult = service.addLicenciaDigitalToCollectionIfMissing(licenciaDigitalCollection, ...licenciaDigitalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const licenciaDigital: ILicenciaDigital = sampleWithRequiredData;
        const licenciaDigital2: ILicenciaDigital = sampleWithPartialData;
        expectedResult = service.addLicenciaDigitalToCollectionIfMissing([], licenciaDigital, licenciaDigital2);
        expect(expectedResult).toEqual([licenciaDigital, licenciaDigital2]);
      });

      it('should accept null and undefined values', () => {
        const licenciaDigital: ILicenciaDigital = sampleWithRequiredData;
        expectedResult = service.addLicenciaDigitalToCollectionIfMissing([], null, licenciaDigital, undefined);
        expect(expectedResult).toEqual([licenciaDigital]);
      });

      it('should return initial array if no LicenciaDigital is added', () => {
        const licenciaDigitalCollection: ILicenciaDigital[] = [sampleWithRequiredData];
        expectedResult = service.addLicenciaDigitalToCollectionIfMissing(licenciaDigitalCollection, undefined, null);
        expect(expectedResult).toEqual(licenciaDigitalCollection);
      });
    });

    describe('compareLicenciaDigital', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLicenciaDigital(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21243 };
        const entity2 = null;

        const compareResult1 = service.compareLicenciaDigital(entity1, entity2);
        const compareResult2 = service.compareLicenciaDigital(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21243 };
        const entity2 = { id: 25185 };

        const compareResult1 = service.compareLicenciaDigital(entity1, entity2);
        const compareResult2 = service.compareLicenciaDigital(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 21243 };
        const entity2 = { id: 21243 };

        const compareResult1 = service.compareLicenciaDigital(entity1, entity2);
        const compareResult2 = service.compareLicenciaDigital(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
