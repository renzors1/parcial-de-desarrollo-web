import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { IAutor } from '../autor.model';
import { AutorService } from '../service/autor.service';

import { AutorFormService } from './autor-form.service';
import { AutorUpdate } from './autor-update';

describe('Autor Management Update Component', () => {
  let comp: AutorUpdate;
  let fixture: ComponentFixture<AutorUpdate>;
  let activatedRoute: ActivatedRoute;
  let autorFormService: AutorFormService;
  let autorService: AutorService;
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

    fixture = TestBed.createComponent(AutorUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    autorFormService = TestBed.inject(AutorFormService);
    autorService = TestBed.inject(AutorService);
    libroService = TestBed.inject(LibroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Libro query and add missing value', () => {
      const autor: IAutor = { id: 27814 };
      const libroses: ILibro[] = [{ id: 15988 }];
      autor.libroses = libroses;

      const libroCollection: ILibro[] = [{ id: 15988 }];
      vi.spyOn(libroService, 'query').mockReturnValue(of(new HttpResponse({ body: libroCollection })));
      const additionalLibros = [...libroses];
      const expectedCollection: ILibro[] = [...additionalLibros, ...libroCollection];
      vi.spyOn(libroService, 'addLibroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ autor });
      comp.ngOnInit();

      expect(libroService.query).toHaveBeenCalled();
      expect(libroService.addLibroToCollectionIfMissing).toHaveBeenCalledWith(
        libroCollection,
        ...additionalLibros.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.librosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const autor: IAutor = { id: 27814 };
      const libros: ILibro = { id: 15988 };
      autor.libroses = [libros];

      activatedRoute.data = of({ autor });
      comp.ngOnInit();

      expect(comp.librosSharedCollection()).toContainEqual(libros);
      expect(comp.autor).toEqual(autor);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAutor>();
      const autor = { id: 29313 };
      vi.spyOn(autorFormService, 'getAutor').mockReturnValue(autor);
      vi.spyOn(autorService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ autor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(autor);
      saveSubject.complete();

      // THEN
      expect(autorFormService.getAutor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(autorService.update).toHaveBeenCalledWith(expect.objectContaining(autor));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAutor>();
      const autor = { id: 29313 };
      vi.spyOn(autorFormService, 'getAutor').mockReturnValue({ id: null });
      vi.spyOn(autorService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ autor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(autor);
      saveSubject.complete();

      // THEN
      expect(autorFormService.getAutor).toHaveBeenCalled();
      expect(autorService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAutor>();
      const autor = { id: 29313 };
      vi.spyOn(autorService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ autor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(autorService.update).toHaveBeenCalled();
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
