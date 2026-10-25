import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAutor } from 'app/entities/autor/autor.model';
import { AutorService } from 'app/entities/autor/service/autor.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IEditorial } from 'app/entities/editorial/editorial.model';
import { EditorialService } from 'app/entities/editorial/service/editorial.service';
import { IPromocion } from 'app/entities/promocion/promocion.model';
import { PromocionService } from 'app/entities/promocion/service/promocion.service';
import { ILibro } from '../libro.model';
import { LibroService } from '../service/libro.service';

import { LibroFormService } from './libro-form.service';
import { LibroUpdate } from './libro-update';

describe('Libro Management Update Component', () => {
  let comp: LibroUpdate;
  let fixture: ComponentFixture<LibroUpdate>;
  let activatedRoute: ActivatedRoute;
  let libroFormService: LibroFormService;
  let libroService: LibroService;
  let categoriaService: CategoriaService;
  let autorService: AutorService;
  let editorialService: EditorialService;
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

    fixture = TestBed.createComponent(LibroUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    libroFormService = TestBed.inject(LibroFormService);
    libroService = TestBed.inject(LibroService);
    categoriaService = TestBed.inject(CategoriaService);
    autorService = TestBed.inject(AutorService);
    editorialService = TestBed.inject(EditorialService);
    promocionService = TestBed.inject(PromocionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Categoria query and add missing value', () => {
      const libro: ILibro = { id: 9205 };
      const categoriases: ICategoria[] = [{ id: 24962 }];
      libro.categoriases = categoriases;

      const categoriaCollection: ICategoria[] = [{ id: 24962 }];
      vi.spyOn(categoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaCollection })));
      const additionalCategorias = [...categoriases];
      const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
      vi.spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ libro });
      comp.ngOnInit();

      expect(categoriaService.query).toHaveBeenCalled();
      expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaCollection,
        ...additionalCategorias.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.categoriasSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Autor query and add missing value', () => {
      const libro: ILibro = { id: 9205 };
      const autoreses: IAutor[] = [{ id: 29313 }];
      libro.autoreses = autoreses;

      const autorCollection: IAutor[] = [{ id: 29313 }];
      vi.spyOn(autorService, 'query').mockReturnValue(of(new HttpResponse({ body: autorCollection })));
      const additionalAutors = [...autoreses];
      const expectedCollection: IAutor[] = [...additionalAutors, ...autorCollection];
      vi.spyOn(autorService, 'addAutorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ libro });
      comp.ngOnInit();

      expect(autorService.query).toHaveBeenCalled();
      expect(autorService.addAutorToCollectionIfMissing).toHaveBeenCalledWith(
        autorCollection,
        ...additionalAutors.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.autorsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Editorial query and add missing value', () => {
      const libro: ILibro = { id: 9205 };
      const editorial: IEditorial = { id: 12270 };
      libro.editorial = editorial;

      const editorialCollection: IEditorial[] = [{ id: 12270 }];
      vi.spyOn(editorialService, 'query').mockReturnValue(of(new HttpResponse({ body: editorialCollection })));
      const additionalEditorials = [editorial];
      const expectedCollection: IEditorial[] = [...additionalEditorials, ...editorialCollection];
      vi.spyOn(editorialService, 'addEditorialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ libro });
      comp.ngOnInit();

      expect(editorialService.query).toHaveBeenCalled();
      expect(editorialService.addEditorialToCollectionIfMissing).toHaveBeenCalledWith(
        editorialCollection,
        ...additionalEditorials.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.editorialsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Promocion query and add missing value', () => {
      const libro: ILibro = { id: 9205 };
      const promocioneses: IPromocion[] = [{ id: 28562 }];
      libro.promocioneses = promocioneses;

      const promocionCollection: IPromocion[] = [{ id: 28562 }];
      vi.spyOn(promocionService, 'query').mockReturnValue(of(new HttpResponse({ body: promocionCollection })));
      const additionalPromocions = [...promocioneses];
      const expectedCollection: IPromocion[] = [...additionalPromocions, ...promocionCollection];
      vi.spyOn(promocionService, 'addPromocionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ libro });
      comp.ngOnInit();

      expect(promocionService.query).toHaveBeenCalled();
      expect(promocionService.addPromocionToCollectionIfMissing).toHaveBeenCalledWith(
        promocionCollection,
        ...additionalPromocions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.promocionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const libro: ILibro = { id: 9205 };
      const categorias: ICategoria = { id: 24962 };
      libro.categoriases = [categorias];
      const autores: IAutor = { id: 29313 };
      libro.autoreses = [autores];
      const editorial: IEditorial = { id: 12270 };
      libro.editorial = editorial;
      const promociones: IPromocion = { id: 28562 };
      libro.promocioneses = [promociones];

      activatedRoute.data = of({ libro });
      comp.ngOnInit();

      expect(comp.categoriasSharedCollection()).toContainEqual(categorias);
      expect(comp.autorsSharedCollection()).toContainEqual(autores);
      expect(comp.editorialsSharedCollection()).toContainEqual(editorial);
      expect(comp.promocionsSharedCollection()).toContainEqual(promociones);
      expect(comp.libro).toEqual(libro);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILibro>();
      const libro = { id: 15988 };
      vi.spyOn(libroFormService, 'getLibro').mockReturnValue(libro);
      vi.spyOn(libroService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ libro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(libro);
      saveSubject.complete();

      // THEN
      expect(libroFormService.getLibro).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(libroService.update).toHaveBeenCalledWith(expect.objectContaining(libro));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILibro>();
      const libro = { id: 15988 };
      vi.spyOn(libroFormService, 'getLibro').mockReturnValue({ id: null });
      vi.spyOn(libroService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ libro: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(libro);
      saveSubject.complete();

      // THEN
      expect(libroFormService.getLibro).toHaveBeenCalled();
      expect(libroService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ILibro>();
      const libro = { id: 15988 };
      vi.spyOn(libroService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ libro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(libroService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategoria', () => {
      it('should forward to categoriaService', () => {
        const entity = { id: 24962 };
        const entity2 = { id: 11537 };
        vi.spyOn(categoriaService, 'compareCategoria');
        comp.compareCategoria(entity, entity2);
        expect(categoriaService.compareCategoria).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAutor', () => {
      it('should forward to autorService', () => {
        const entity = { id: 29313 };
        const entity2 = { id: 27814 };
        vi.spyOn(autorService, 'compareAutor');
        comp.compareAutor(entity, entity2);
        expect(autorService.compareAutor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEditorial', () => {
      it('should forward to editorialService', () => {
        const entity = { id: 12270 };
        const entity2 = { id: 5819 };
        vi.spyOn(editorialService, 'compareEditorial');
        comp.compareEditorial(entity, entity2);
        expect(editorialService.compareEditorial).toHaveBeenCalledWith(entity, entity2);
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
