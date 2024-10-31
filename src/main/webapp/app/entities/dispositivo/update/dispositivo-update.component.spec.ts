import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DispositivoService } from '../service/dispositivo.service';
import { IDispositivo } from '../dispositivo.model';
import { DispositivoFormService } from './dispositivo-form.service';

import { DispositivoUpdateComponent } from './dispositivo-update.component';

describe('Dispositivo Management Update Component', () => {
  let comp: DispositivoUpdateComponent;
  let fixture: ComponentFixture<DispositivoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dispositivoFormService: DispositivoFormService;
  let dispositivoService: DispositivoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DispositivoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DispositivoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DispositivoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dispositivoFormService = TestBed.inject(DispositivoFormService);
    dispositivoService = TestBed.inject(DispositivoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dispositivo: IDispositivo = { id: 456 };

      activatedRoute.data = of({ dispositivo });
      comp.ngOnInit();

      expect(comp.dispositivo).toEqual(dispositivo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDispositivo>>();
      const dispositivo = { id: 123 };
      jest.spyOn(dispositivoFormService, 'getDispositivo').mockReturnValue(dispositivo);
      jest.spyOn(dispositivoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dispositivo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dispositivo }));
      saveSubject.complete();

      // THEN
      expect(dispositivoFormService.getDispositivo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dispositivoService.update).toHaveBeenCalledWith(expect.objectContaining(dispositivo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDispositivo>>();
      const dispositivo = { id: 123 };
      jest.spyOn(dispositivoFormService, 'getDispositivo').mockReturnValue({ id: null });
      jest.spyOn(dispositivoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dispositivo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dispositivo }));
      saveSubject.complete();

      // THEN
      expect(dispositivoFormService.getDispositivo).toHaveBeenCalled();
      expect(dispositivoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDispositivo>>();
      const dispositivo = { id: 123 };
      jest.spyOn(dispositivoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dispositivo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dispositivoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
