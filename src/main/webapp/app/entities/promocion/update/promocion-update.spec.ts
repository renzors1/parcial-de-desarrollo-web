import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { IPromocion } from '../promocion.model';
import { PromocionService } from '../service/promocion.service';

import { PromocionFormService } from './promocion-form.service';
import { PromocionUpdate } from './promocion-update';

describe('Promocion Management Update Component', () => {
  let comp: PromocionUpdate;
  let fixture: ComponentFixture<PromocionUpdate>;
  let activatedRoute: ActivatedRoute;
  let promocionFormService: PromocionFormService;
  let promocionService: PromocionService;
  let libroService: LibroService;

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

    fixture = TestBed.createComponent(PromocionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    promocionFormService = TestBed.inject(PromocionFormService);
    promocionService = TestBed.inject(PromocionService);
    libroService = TestBed.inject(LibroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Libro query and add missing value', () => {
      const promocion: IPromocion = { id: 14882 };
      const libroses: ILibro[] = [{ id: 15988 }];
      promocion.libroses = libroses;

      const libroCollection: ILibro[] = [{ id: 15988 }];
      vi.spyOn(libroService, 'query').mockReturnValue(of(new HttpResponse({ body: libroCollection })));
      const additionalLibros = [...libroses];
      const expectedCollection: ILibro[] = [...additionalLibros, ...libroCollection];
      vi.spyOn(libroService, 'addLibroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      expect(libroService.query).toHaveBeenCalled();
      expect(libroService.addLibroToCollectionIfMissing).toHaveBeenCalledWith(
        libroCollection,
        ...additionalLibros.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.librosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const promocion: IPromocion = { id: 14882 };
      const libros: ILibro = { id: 15988 };
      promocion.libroses = [libros];

      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      expect(comp.librosSharedCollection()).toContainEqual(libros);
      expect(comp.promocion).toEqual(promocion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPromocion>();
      const promocion = { id: 28562 };
      vi.spyOn(promocionFormService, 'getPromocion').mockReturnValue(promocion);
      vi.spyOn(promocionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(promocion);
      saveSubject.complete();

      // THEN
      expect(promocionFormService.getPromocion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(promocionService.update).toHaveBeenCalledWith(expect.objectContaining(promocion));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPromocion>();
      const promocion = { id: 28562 };
      vi.spyOn(promocionFormService, 'getPromocion').mockReturnValue({ id: null });
      vi.spyOn(promocionService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promocion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(promocion);
      saveSubject.complete();

      // THEN
      expect(promocionFormService.getPromocion).toHaveBeenCalled();
      expect(promocionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPromocion>();
      const promocion = { id: 28562 };
      vi.spyOn(promocionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(promocionService.update).toHaveBeenCalled();
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
  });
});
