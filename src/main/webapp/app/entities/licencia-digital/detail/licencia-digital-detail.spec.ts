import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { LicenciaDigitalDetail } from './licencia-digital-detail';

describe('LicenciaDigital Management Detail Component', () => {
  let comp: LicenciaDigitalDetail;
  let fixture: ComponentFixture<LicenciaDigitalDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./licencia-digital-detail').then(m => m.LicenciaDigitalDetail),
              resolve: { licenciaDigital: () => of({ id: 21243 }) },
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
    fixture = TestBed.createComponent(LicenciaDigitalDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load licenciaDigital on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LicenciaDigitalDetail);

      // THEN
      expect(instance.licenciaDigital()).toEqual(expect.objectContaining({ id: 21243 }));
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
