import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CalificacionUsuarioDetail } from './calificacion-usuario-detail';

describe('CalificacionUsuario Management Detail Component', () => {
  let comp: CalificacionUsuarioDetail;
  let fixture: ComponentFixture<CalificacionUsuarioDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./calificacion-usuario-detail').then(m => m.CalificacionUsuarioDetail),
              resolve: { calificacionUsuario: () => of({ id: 8270 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CalificacionUsuarioDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load calificacionUsuario on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CalificacionUsuarioDetail);

      // THEN
      expect(instance.calificacionUsuario()).toEqual(expect.objectContaining({ id: 8270 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
