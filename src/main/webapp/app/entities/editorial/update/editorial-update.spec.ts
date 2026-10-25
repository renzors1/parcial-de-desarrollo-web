import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IEditorial } from '../editorial.model';
import { EditorialService } from '../service/editorial.service';

import { EditorialFormService } from './editorial-form.service';
import { EditorialUpdate } from './editorial-update';

describe('Editorial Management Update Component', () => {
  let comp: EditorialUpdate;
  let fixture: ComponentFixture<EditorialUpdate>;
  let activatedRoute: ActivatedRoute;
  let editorialFormService: EditorialFormService;
  let editorialService: EditorialService;

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

    fixture = TestBed.createComponent(EditorialUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    editorialFormService = TestBed.inject(EditorialFormService);
    editorialService = TestBed.inject(EditorialService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const editorial: IEditorial = { id: 5819 };

      activatedRoute.data = of({ editorial });
      comp.ngOnInit();

      expect(comp.editorial).toEqual(editorial);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEditorial>();
      const editorial = { id: 12270 };
      vi.spyOn(editorialFormService, 'getEditorial').mockReturnValue(editorial);
      vi.spyOn(editorialService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ editorial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(editorial);
      saveSubject.complete();

      // THEN
      expect(editorialFormService.getEditorial).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(editorialService.update).toHaveBeenCalledWith(expect.objectContaining(editorial));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEditorial>();
      const editorial = { id: 12270 };
      vi.spyOn(editorialFormService, 'getEditorial').mockReturnValue({ id: null });
      vi.spyOn(editorialService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ editorial: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(editorial);
      saveSubject.complete();

      // THEN
      expect(editorialFormService.getEditorial).toHaveBeenCalled();
      expect(editorialService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEditorial>();
      const editorial = { id: 12270 };
      vi.spyOn(editorialService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ editorial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(editorialService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
