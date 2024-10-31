import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DispositivoDetailComponent } from './dispositivo-detail.component';

describe('Dispositivo Management Detail Component', () => {
  let comp: DispositivoDetailComponent;
  let fixture: ComponentFixture<DispositivoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DispositivoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./dispositivo-detail.component').then(m => m.DispositivoDetailComponent),
              resolve: { dispositivo: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DispositivoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DispositivoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dispositivo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DispositivoDetailComponent);

      // THEN
      expect(instance.dispositivo()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
