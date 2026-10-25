import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDetalleVenta } from 'app/entities/detalle-venta/detalle-venta.model';
import { DetalleVentaService } from 'app/entities/detalle-venta/service/detalle-venta.service';
import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { ILicenciaDigital } from '../licencia-digital.model';
import { LicenciaDigitalService } from '../service/licencia-digital.service';

import { LicenciaDigitalFormService } from './licencia-digital-form.service';
import { LicenciaDigitalUpdate } from './licencia-digital-update';

describe('LicenciaDigital Management Update Component', () => {
  let comp: LicenciaDigitalUpdate;
  let fixture: ComponentFixture<LicenciaDigitalUpdate>;
  let activatedRoute: ActivatedRoute;
  let licenciaDigitalFormService: LicenciaDigitalFormService;
  let licenciaDigitalService: LicenciaDigitalService;
  let libroService: LibroService;
  let detalleVentaService: DetalleVentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(LicenciaDigitalUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    licenciaDigitalFormService = TestBed.inject(LicenciaDigitalFormService);
    licenciaDigitalService = TestBed.inject(LicenciaDigitalService);
    libroService = TestBed.inject(LibroService);
    detalleVentaService = TestBed.inject(DetalleVentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Libro query and add missing value', () => {
      const licenciaDigital: ILicenciaDigital = { id: 25185 };
      const libro: ILibro = { id: 15988 };
      licenciaDigital.libro = libro;

      const libroCollection: ILibro[] = [{ id: 15988 }];
      vi.spyOn(libroService, 'query').mockReturnValue(of(new HttpResponse({ body: libroCollection })));
      const additionalLibros = [libro];
      const expectedCollection: ILibro[] = [...additionalLibros, ...libroCollection];
      vi.spyOn(libroService, 'addLibroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ licenciaDigital });
      comp.ngOnInit();

      expect(libroService.query).toHaveBeenCalled();
      expect(libroService.addLibroToCollectionIfMissing).toHaveBeenCalledWith(
        libroCollection,
        ...additionalLibros.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.librosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call DetalleVenta query and add missing value', () => {
      const licenciaDigital: ILicenciaDigital = { id: 25185 };
      const detalleVenta: IDetalleVenta = { id: 26482 };
      licenciaDigital.detalleVenta = detalleVenta;

      const detalleVentaCollection: IDetalleVenta[] = [{ id: 26482 }];
      vi.spyOn(detalleVentaService, 'query').mockReturnValue(of(new HttpResponse({ body: detalleVentaCollection })));
      const additionalDetalleVentas = [detalleVenta];
      const expectedCollection: IDetalleVenta[] = [...additionalDetalleVentas, ...detalleVentaCollection];
      vi.spyOn(detalleVentaService, 'addDetalleVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ licenciaDigital });
      comp.ngOnInit();

      expect(detalleVentaService.query).toHaveBeenCalled();
      expect(detalleVentaService.addDetalleVentaToCollectionIfMissing).toHaveBeenCalledWith(
        detalleVentaCollection,
        ...additionalDetalleVentas.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.detalleVentasSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const licenciaDigital: ILicenciaDigital = { id: 25185 };
      const libro: ILibro = { id: 15988 };
      licenciaDigital.libro = libro;
      const detalleVenta: IDetalleVenta = { id: 26482 };
      licenciaDigital.detalleVenta = detalleVenta;

      activatedRoute.data = of({ licenciaDigital });
      comp.ngOnInit();

      expect(comp.librosSharedCollection()).toContainEqual(libro);
      expect(comp.detalleVentasSharedCollection()).toContainEqual(detalleVenta);
      expect(comp.licenciaDigital).toEqual(licenciaDigital);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILicenciaDigital>();
      const licenciaDigital = { id: 21243 };
      vi.spyOn(licenciaDigitalFormService, 'getLicenciaDigital').mockReturnValue(licenciaDigital);
      vi.spyOn(licenciaDigitalService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ licenciaDigital });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(licenciaDigital);
      saveSubject.complete();

      // THEN
      expect(licenciaDigitalFormService.getLicenciaDigital).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(licenciaDigitalService.update).toHaveBeenCalledWith(expect.objectContaining(licenciaDigital));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILicenciaDigital>();
      const licenciaDigital = { id: 21243 };
      vi.spyOn(licenciaDigitalFormService, 'getLicenciaDigital').mockReturnValue({ id: null });
      vi.spyOn(licenciaDigitalService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ licenciaDigital: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(licenciaDigital);
      saveSubject.complete();

      // THEN
      expect(licenciaDigitalFormService.getLicenciaDigital).toHaveBeenCalled();
      expect(licenciaDigitalService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ILicenciaDigital>();
      const licenciaDigital = { id: 21243 };
      vi.spyOn(licenciaDigitalService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ licenciaDigital });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(licenciaDigitalService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLibro', () => {
      it('should forward to libroService', () => {
        const entity = { id: 15988 };
        const entity2 = { id: 9205 };
        vi.spyOn(libroService, 'compareLibro');
        comp.compareLibro(entity, entity2);
        expect(libroService.compareLibro).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDetalleVenta', () => {
      it('should forward to detalleVentaService', () => {
        const entity = { id: 26482 };
        const entity2 = { id: 5030 };
        vi.spyOn(detalleVentaService, 'compareDetalleVenta');
        comp.compareDetalleVenta(entity, entity2);
        expect(detalleVentaService.compareDetalleVenta).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
