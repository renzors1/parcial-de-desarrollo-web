import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IRol } from '../rol.model';
import { RolService } from '../service/rol.service';

import { RolFormService } from './rol-form.service';
import { RolUpdate } from './rol-update';

describe('Rol Management Update Component', () => {
  let comp: RolUpdate;
  let fixture: ComponentFixture<RolUpdate>;
  let activatedRoute: ActivatedRoute;
  let rolFormService: RolFormService;
  let rolService: RolService;

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

    fixture = TestBed.createComponent(RolUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rolFormService = TestBed.inject(RolFormService);
    rolService = TestBed.inject(RolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const rol: IRol = { id: 1064 };

      activatedRoute.data = of({ rol });
      comp.ngOnInit();

      expect(comp.rol).toEqual(rol);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRol>();
      const rol = { id: 28479 };
      vi.spyOn(rolFormService, 'getRol').mockReturnValue(rol);
      vi.spyOn(rolService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(rol);
      saveSubject.complete();

      // THEN
      expect(rolFormService.getRol).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rolService.update).toHaveBeenCalledWith(expect.objectContaining(rol));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRol>();
      const rol = { id: 28479 };
      vi.spyOn(rolFormService, 'getRol').mockReturnValue({ id: null });
      vi.spyOn(rolService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rol: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(rol);
      saveSubject.complete();

      // THEN
      expect(rolFormService.getRol).toHaveBeenCalled();
      expect(rolService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IRol>();
      const rol = { id: 28479 };
      vi.spyOn(rolService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rolService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
