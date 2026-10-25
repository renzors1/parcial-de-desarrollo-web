import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IRol } from 'app/entities/rol/rol.model';
import { RolService } from 'app/entities/rol/service/rol.service';
import { UsuarioService } from '../service/usuario.service';
import { IUsuario } from '../usuario.model';

import { UsuarioFormService } from './usuario-form.service';
import { UsuarioUpdate } from './usuario-update';

describe('Usuario Management Update Component', () => {
  let comp: UsuarioUpdate;
  let fixture: ComponentFixture<UsuarioUpdate>;
  let activatedRoute: ActivatedRoute;
  let usuarioFormService: UsuarioFormService;
  let usuarioService: UsuarioService;
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

    fixture = TestBed.createComponent(UsuarioUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usuarioFormService = TestBed.inject(UsuarioFormService);
    usuarioService = TestBed.inject(UsuarioService);
    rolService = TestBed.inject(RolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Rol query and add missing value', () => {
      const usuario: IUsuario = { id: 13162 };
      const rol: IRol = { id: 28479 };
      usuario.rol = rol;

      const rolCollection: IRol[] = [{ id: 28479 }];
      vi.spyOn(rolService, 'query').mockReturnValue(of(new HttpResponse({ body: rolCollection })));
      const additionalRols = [rol];
      const expectedCollection: IRol[] = [...additionalRols, ...rolCollection];
      vi.spyOn(rolService, 'addRolToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(rolService.query).toHaveBeenCalled();
      expect(rolService.addRolToCollectionIfMissing).toHaveBeenCalledWith(
        rolCollection,
        ...additionalRols.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.rolsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const usuario: IUsuario = { id: 13162 };
      const rol: IRol = { id: 28479 };
      usuario.rol = rol;

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(comp.rolsSharedCollection()).toContainEqual(rol);
      expect(comp.usuario).toEqual(usuario);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUsuario>();
      const usuario = { id: 544 };
      vi.spyOn(usuarioFormService, 'getUsuario').mockReturnValue(usuario);
      vi.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(usuario);
      saveSubject.complete();

      // THEN
      expect(usuarioFormService.getUsuario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(usuarioService.update).toHaveBeenCalledWith(expect.objectContaining(usuario));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUsuario>();
      const usuario = { id: 544 };
      vi.spyOn(usuarioFormService, 'getUsuario').mockReturnValue({ id: null });
      vi.spyOn(usuarioService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(usuario);
      saveSubject.complete();

      // THEN
      expect(usuarioFormService.getUsuario).toHaveBeenCalled();
      expect(usuarioService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IUsuario>();
      const usuario = { id: 544 };
      vi.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usuarioService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRol', () => {
      it('should forward to rolService', () => {
        const entity = { id: 28479 };
        const entity2 = { id: 1064 };
        vi.spyOn(rolService, 'compareRol');
        comp.compareRol(entity, entity2);
        expect(rolService.compareRol).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
