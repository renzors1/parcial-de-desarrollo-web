import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { ICalificacionUsuario } from '../calificacion-usuario.model';
import { CalificacionUsuarioService } from '../service/calificacion-usuario.service';

import { CalificacionUsuarioFormService } from './calificacion-usuario-form.service';
import { CalificacionUsuarioUpdate } from './calificacion-usuario-update';

describe('CalificacionUsuario Management Update Component', () => {
  let comp: CalificacionUsuarioUpdate;
  let fixture: ComponentFixture<CalificacionUsuarioUpdate>;
  let activatedRoute: ActivatedRoute;
  let calificacionUsuarioFormService: CalificacionUsuarioFormService;
  let calificacionUsuarioService: CalificacionUsuarioService;
  let usuarioService: UsuarioService;
  let ventaService: VentaService;

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

    fixture = TestBed.createComponent(CalificacionUsuarioUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    calificacionUsuarioFormService = TestBed.inject(CalificacionUsuarioFormService);
    calificacionUsuarioService = TestBed.inject(CalificacionUsuarioService);
    usuarioService = TestBed.inject(UsuarioService);
    ventaService = TestBed.inject(VentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Usuario query and add missing value', () => {
      const calificacionUsuario: ICalificacionUsuario = { id: 4870 };
      const clienteEvaluado: IUsuario = { id: 544 };
      calificacionUsuario.clienteEvaluado = clienteEvaluado;
      const evaluadorTienda: IUsuario = { id: 544 };
      calificacionUsuario.evaluadorTienda = evaluadorTienda;

      const usuarioCollection: IUsuario[] = [{ id: 544 }];
      vi.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [clienteEvaluado, evaluadorTienda];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      vi.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ calificacionUsuario });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(
        usuarioCollection,
        ...additionalUsuarios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usuariosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Venta query and add missing value', () => {
      const calificacionUsuario: ICalificacionUsuario = { id: 4870 };
      const venta: IVenta = { id: 10395 };
      calificacionUsuario.venta = venta;

      const ventaCollection: IVenta[] = [{ id: 10395 }];
      vi.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
      const additionalVentas = [venta];
      const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
      vi.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ calificacionUsuario });
      comp.ngOnInit();

      expect(ventaService.query).toHaveBeenCalled();
      expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(
        ventaCollection,
        ...additionalVentas.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ventasSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const calificacionUsuario: ICalificacionUsuario = { id: 4870 };
      const clienteEvaluado: IUsuario = { id: 544 };
      calificacionUsuario.clienteEvaluado = clienteEvaluado;
      const evaluadorTienda: IUsuario = { id: 544 };
      calificacionUsuario.evaluadorTienda = evaluadorTienda;
      const venta: IVenta = { id: 10395 };
      calificacionUsuario.venta = venta;

      activatedRoute.data = of({ calificacionUsuario });
      comp.ngOnInit();

      expect(comp.usuariosSharedCollection()).toContainEqual(clienteEvaluado);
      expect(comp.usuariosSharedCollection()).toContainEqual(evaluadorTienda);
      expect(comp.ventasSharedCollection()).toContainEqual(venta);
      expect(comp.calificacionUsuario).toEqual(calificacionUsuario);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICalificacionUsuario>();
      const calificacionUsuario = { id: 8270 };
      vi.spyOn(calificacionUsuarioFormService, 'getCalificacionUsuario').mockReturnValue(calificacionUsuario);
      vi.spyOn(calificacionUsuarioService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calificacionUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(calificacionUsuario);
      saveSubject.complete();

      // THEN
      expect(calificacionUsuarioFormService.getCalificacionUsuario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(calificacionUsuarioService.update).toHaveBeenCalledWith(expect.objectContaining(calificacionUsuario));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICalificacionUsuario>();
      const calificacionUsuario = { id: 8270 };
      vi.spyOn(calificacionUsuarioFormService, 'getCalificacionUsuario').mockReturnValue({ id: null });
      vi.spyOn(calificacionUsuarioService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calificacionUsuario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(calificacionUsuario);
      saveSubject.complete();

      // THEN
      expect(calificacionUsuarioFormService.getCalificacionUsuario).toHaveBeenCalled();
      expect(calificacionUsuarioService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICalificacionUsuario>();
      const calificacionUsuario = { id: 8270 };
      vi.spyOn(calificacionUsuarioService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calificacionUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(calificacionUsuarioService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUsuario', () => {
      it('should forward to usuarioService', () => {
        const entity = { id: 544 };
        const entity2 = { id: 13162 };
        vi.spyOn(usuarioService, 'compareUsuario');
        comp.compareUsuario(entity, entity2);
        expect(usuarioService.compareUsuario).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVenta', () => {
      it('should forward to ventaService', () => {
        const entity = { id: 10395 };
        const entity2 = { id: 27589 };
        vi.spyOn(ventaService, 'compareVenta');
        comp.compareVenta(entity, entity2);
        expect(ventaService.compareVenta).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
