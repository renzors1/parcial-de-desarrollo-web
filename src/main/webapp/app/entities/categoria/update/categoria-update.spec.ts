import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { ICategoria } from '../categoria.model';
import { CategoriaService } from '../service/categoria.service';

import { CategoriaFormService } from './categoria-form.service';
import { CategoriaUpdate } from './categoria-update';

describe('Categoria Management Update Component', () => {
  let comp: CategoriaUpdate;
  let fixture: ComponentFixture<CategoriaUpdate>;
  let activatedRoute: ActivatedRoute;
  let categoriaFormService: CategoriaFormService;
  let categoriaService: CategoriaService;
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

    fixture = TestBed.createComponent(CategoriaUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoriaFormService = TestBed.inject(CategoriaFormService);
    categoriaService = TestBed.inject(CategoriaService);
    libroService = TestBed.inject(LibroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Libro query and add missing value', () => {
      const categoria: ICategoria = { id: 11537 };
      const libroses: ILibro[] = [{ id: 15988 }];
      categoria.libroses = libroses;

      const libroCollection: ILibro[] = [{ id: 15988 }];
      vi.spyOn(libroService, 'query').mockReturnValue(of(new HttpResponse({ body: libroCollection })));
      const additionalLibros = [...libroses];
      const expectedCollection: ILibro[] = [...additionalLibros, ...libroCollection];
      vi.spyOn(libroService, 'addLibroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ categoria });
      comp.ngOnInit();

      expect(libroService.query).toHaveBeenCalled();
      expect(libroService.addLibroToCollectionIfMissing).toHaveBeenCalledWith(
        libroCollection,
        ...additionalLibros.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.librosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const categoria: ICategoria = { id: 11537 };
      const libros: ILibro = { id: 15988 };
      categoria.libroses = [libros];

      activatedRoute.data = of({ categoria });
      comp.ngOnInit();

      expect(comp.librosSharedCollection()).toContainEqual(libros);
      expect(comp.categoria).toEqual(categoria);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICategoria>();
      const categoria = { id: 24962 };
      vi.spyOn(categoriaFormService, 'getCategoria').mockReturnValue(categoria);
      vi.spyOn(categoriaService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(categoria);
      saveSubject.complete();

      // THEN
      expect(categoriaFormService.getCategoria).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoriaService.update).toHaveBeenCalledWith(expect.objectContaining(categoria));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICategoria>();
      const categoria = { id: 24962 };
      vi.spyOn(categoriaFormService, 'getCategoria').mockReturnValue({ id: null });
      vi.spyOn(categoriaService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoria: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(categoria);
      saveSubject.complete();

      // THEN
      expect(categoriaFormService.getCategoria).toHaveBeenCalled();
      expect(categoriaService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICategoria>();
      const categoria = { id: 24962 };
      vi.spyOn(categoriaService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoriaService.update).toHaveBeenCalled();
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
