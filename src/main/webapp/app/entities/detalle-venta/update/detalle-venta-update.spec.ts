import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { IPromocion } from 'app/entities/promocion/promocion.model';
import { PromocionService } from 'app/entities/promocion/service/promocion.service';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { IDetalleVenta } from '../detalle-venta.model';
import { DetalleVentaService } from '../service/detalle-venta.service';

import { DetalleVentaFormService } from './detalle-venta-form.service';
import { DetalleVentaUpdate } from './detalle-venta-update';

describe('DetalleVenta Management Update Component', () => {
  let comp: DetalleVentaUpdate;
  let fixture: ComponentFixture<DetalleVentaUpdate>;
  let activatedRoute: ActivatedRoute;
  let detalleVentaFormService: DetalleVentaFormService;
  let detalleVentaService: DetalleVentaService;
  let ventaService: VentaService;
  let libroService: LibroService;
  let promocionService: PromocionService;

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

    fixture = TestBed.createComponent(DetalleVentaUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detalleVentaFormService = TestBed.inject(DetalleVentaFormService);
    detalleVentaService = TestBed.inject(DetalleVentaService);
    ventaService = TestBed.inject(VentaService);
    libroService = TestBed.inject(LibroService);
    promocionService = TestBed.inject(PromocionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Venta query and add missing value', () => {
      const detalleVenta: IDetalleVenta = { id: 5030 };
      const venta: IVenta = { id: 10395 };
      detalleVenta.venta = venta;

      const ventaCollection: IVenta[] = [{ id: 10395 }];
      vi.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
      const additionalVentas = [venta];
      const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
      vi.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(ventaService.query).toHaveBeenCalled();
      expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(
        ventaCollection,
        ...additionalVentas.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ventasSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Libro query and add missing value', () => {
      const detalleVenta: IDetalleVenta = { id: 5030 };
      const libro: ILibro = { id: 15988 };
      detalleVenta.libro = libro;

      const libroCollection: ILibro[] = [{ id: 15988 }];
      vi.spyOn(libroService, 'query').mockReturnValue(of(new HttpResponse({ body: libroCollection })));
      const additionalLibros = [libro];
      const expectedCollection: ILibro[] = [...additionalLibros, ...libroCollection];
      vi.spyOn(libroService, 'addLibroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(libroService.query).toHaveBeenCalled();
      expect(libroService.addLibroToCollectionIfMissing).toHaveBeenCalledWith(
        libroCollection,
        ...additionalLibros.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.librosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Promocion query and add missing value', () => {
      const detalleVenta: IDetalleVenta = { id: 5030 };
      const promocionAplicada: IPromocion = { id: 28562 };
      detalleVenta.promocionAplicada = promocionAplicada;

      const promocionCollection: IPromocion[] = [{ id: 28562 }];
      vi.spyOn(promocionService, 'query').mockReturnValue(of(new HttpResponse({ body: promocionCollection })));
      const additionalPromocions = [promocionAplicada];
      const expectedCollection: IPromocion[] = [...additionalPromocions, ...promocionCollection];
      vi.spyOn(promocionService, 'addPromocionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(promocionService.query).toHaveBeenCalled();
      expect(promocionService.addPromocionToCollectionIfMissing).toHaveBeenCalledWith(
        promocionCollection,
        ...additionalPromocions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.promocionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const detalleVenta: IDetalleVenta = { id: 5030 };
      const venta: IVenta = { id: 10395 };
      detalleVenta.venta = venta;
      const libro: ILibro = { id: 15988 };
      detalleVenta.libro = libro;
      const promocionAplicada: IPromocion = { id: 28562 };
      detalleVenta.promocionAplicada = promocionAplicada;

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(comp.ventasSharedCollection()).toContainEqual(venta);
      expect(comp.librosSharedCollection()).toContainEqual(libro);
      expect(comp.promocionsSharedCollection()).toContainEqual(promocionAplicada);
      expect(comp.detalleVenta).toEqual(detalleVenta);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDetalleVenta>();
      const detalleVenta = { id: 26482 };
      vi.spyOn(detalleVentaFormService, 'getDetalleVenta').mockReturnValue(detalleVenta);
      vi.spyOn(detalleVentaService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(detalleVenta);
      saveSubject.complete();

      // THEN
      expect(detalleVentaFormService.getDetalleVenta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(detalleVentaService.update).toHaveBeenCalledWith(expect.objectContaining(detalleVenta));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDetalleVenta>();
      const detalleVenta = { id: 26482 };
      vi.spyOn(detalleVentaFormService, 'getDetalleVenta').mockReturnValue({ id: null });
      vi.spyOn(detalleVentaService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(detalleVenta);
      saveSubject.complete();

      // THEN
      expect(detalleVentaFormService.getDetalleVenta).toHaveBeenCalled();
      expect(detalleVentaService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IDetalleVenta>();
      const detalleVenta = { id: 26482 };
      vi.spyOn(detalleVentaService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detalleVentaService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVenta', () => {
      it('should forward to ventaService', () => {
        const entity = { id: 10395 };
        const entity2 = { id: 27589 };
        vi.spyOn(ventaService, 'compareVenta');
        comp.compareVenta(entity, entity2);
        expect(ventaService.compareVenta).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLibro', () => {
      it('should forward to libroService', () => {
        const entity = { id: 15988 };
        const entity2 = { id: 9205 };
        vi.spyOn(libroService, 'compareLibro');
        comp.compareLibro(entity, entity2);
        expect(libroService.compareLibro).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePromocion', () => {
      it('should forward to promocionService', () => {
        const entity = { id: 28562 };
        const entity2 = { id: 14882 };
        vi.spyOn(promocionService, 'comparePromocion');
        comp.comparePromocion(entity, entity2);
        expect(promocionService.comparePromocion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
