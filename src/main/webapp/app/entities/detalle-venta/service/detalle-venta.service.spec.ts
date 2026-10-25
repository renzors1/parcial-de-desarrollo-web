import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDetalleVenta } from '../detalle-venta.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../detalle-venta.test-samples';

import { DetalleVentaService, RestDetalleVenta } from './detalle-venta.service';

const requireRestSample: RestDetalleVenta = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('DetalleVenta Service', () => {
  let service: DetalleVentaService;
  let httpMock: HttpTestingController;
  let expectedResult: IDetalleVenta | IDetalleVenta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DetalleVentaService);
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

    it('should create a DetalleVenta', () => {
      const detalleVenta = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(detalleVenta).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetalleVenta', () => {
      const detalleVenta = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(detalleVenta).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DetalleVenta', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DetalleVenta', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DetalleVenta', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addDetalleVentaToCollectionIfMissing', () => {
      it('should add a DetalleVenta to an empty array', () => {
        const detalleVenta: IDetalleVenta = sampleWithRequiredData;
        expectedResult = service.addDetalleVentaToCollectionIfMissing([], detalleVenta);
        expect(expectedResult).toEqual([detalleVenta]);
      });

      it('should not add a DetalleVenta to an array that contains it', () => {
        const detalleVenta: IDetalleVenta = sampleWithRequiredData;
        const detalleVentaCollection: IDetalleVenta[] = [
          {
            ...detalleVenta,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDetalleVentaToCollectionIfMissing(detalleVentaCollection, detalleVenta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetalleVenta to an array that doesn't contain it", () => {
        const detalleVenta: IDetalleVenta = sampleWithRequiredData;
        const detalleVentaCollection: IDetalleVenta[] = [sampleWithPartialData];
        expectedResult = service.addDetalleVentaToCollectionIfMissing(detalleVentaCollection, detalleVenta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detalleVenta);
      });

      it('should add only unique DetalleVenta to an array', () => {
        const detalleVentaArray: IDetalleVenta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const detalleVentaCollection: IDetalleVenta[] = [sampleWithRequiredData];
        expectedResult = service.addDetalleVentaToCollectionIfMissing(detalleVentaCollection, ...detalleVentaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detalleVenta: IDetalleVenta = sampleWithRequiredData;
        const detalleVenta2: IDetalleVenta = sampleWithPartialData;
        expectedResult = service.addDetalleVentaToCollectionIfMissing([], detalleVenta, detalleVenta2);
        expect(expectedResult).toEqual([detalleVenta, detalleVenta2]);
      });

      it('should accept null and undefined values', () => {
        const detalleVenta: IDetalleVenta = sampleWithRequiredData;
        expectedResult = service.addDetalleVentaToCollectionIfMissing([], null, detalleVenta, undefined);
        expect(expectedResult).toEqual([detalleVenta]);
      });

      it('should return initial array if no DetalleVenta is added', () => {
        const detalleVentaCollection: IDetalleVenta[] = [sampleWithRequiredData];
        expectedResult = service.addDetalleVentaToCollectionIfMissing(detalleVentaCollection, undefined, null);
        expect(expectedResult).toEqual(detalleVentaCollection);
      });
    });

    describe('compareDetalleVenta', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDetalleVenta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26482 };
        const entity2 = null;

        const compareResult1 = service.compareDetalleVenta(entity1, entity2);
        const compareResult2 = service.compareDetalleVenta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26482 };
        const entity2 = { id: 5030 };

        const compareResult1 = service.compareDetalleVenta(entity1, entity2);
        const compareResult2 = service.compareDetalleVenta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 26482 };
        const entity2 = { id: 26482 };

        const compareResult1 = service.compareDetalleVenta(entity1, entity2);
        const compareResult2 = service.compareDetalleVenta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
