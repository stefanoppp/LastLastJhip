import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDispositivo } from 'app/entities/dispositivo/dispositivo.model';
import { DispositivoService } from 'app/entities/dispositivo/service/dispositivo.service';
import { IPersonalizacion } from 'app/entities/personalizacion/personalizacion.model';
import { PersonalizacionService } from 'app/entities/personalizacion/service/personalizacion.service';
import { IAdicional } from 'app/entities/adicional/adicional.model';
import { AdicionalService } from 'app/entities/adicional/service/adicional.service';
import { IVenta } from '../venta.model';
import { VentaService } from '../service/venta.service';
import { VentaFormService } from './venta-form.service';

import { VentaUpdateComponent } from './venta-update.component';

describe('Venta Management Update Component', () => {
  let comp: VentaUpdateComponent;
  let fixture: ComponentFixture<VentaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ventaFormService: VentaFormService;
  let ventaService: VentaService;
  let dispositivoService: DispositivoService;
  let personalizacionService: PersonalizacionService;
  let adicionalService: AdicionalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VentaUpdateComponent],
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
      .overrideTemplate(VentaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VentaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ventaFormService = TestBed.inject(VentaFormService);
    ventaService = TestBed.inject(VentaService);
    dispositivoService = TestBed.inject(DispositivoService);
    personalizacionService = TestBed.inject(PersonalizacionService);
    adicionalService = TestBed.inject(AdicionalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dispositivo query and add missing value', () => {
      const venta: IVenta = { id: 456 };
      const dispositivo: IDispositivo = { id: 16389 };
      venta.dispositivo = dispositivo;

      const dispositivoCollection: IDispositivo[] = [{ id: 30185 }];
      jest.spyOn(dispositivoService, 'query').mockReturnValue(of(new HttpResponse({ body: dispositivoCollection })));
      const additionalDispositivos = [dispositivo];
      const expectedCollection: IDispositivo[] = [...additionalDispositivos, ...dispositivoCollection];
      jest.spyOn(dispositivoService, 'addDispositivoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      expect(dispositivoService.query).toHaveBeenCalled();
      expect(dispositivoService.addDispositivoToCollectionIfMissing).toHaveBeenCalledWith(
        dispositivoCollection,
        ...additionalDispositivos.map(expect.objectContaining),
      );
      expect(comp.dispositivosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Personalizacion query and add missing value', () => {
      const venta: IVenta = { id: 456 };
      const personalizaciones: IPersonalizacion[] = [{ id: 21591 }];
      venta.personalizaciones = personalizaciones;

      const personalizacionCollection: IPersonalizacion[] = [{ id: 1459 }];
      jest.spyOn(personalizacionService, 'query').mockReturnValue(of(new HttpResponse({ body: personalizacionCollection })));
      const additionalPersonalizacions = [...personalizaciones];
      const expectedCollection: IPersonalizacion[] = [...additionalPersonalizacions, ...personalizacionCollection];
      jest.spyOn(personalizacionService, 'addPersonalizacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      expect(personalizacionService.query).toHaveBeenCalled();
      expect(personalizacionService.addPersonalizacionToCollectionIfMissing).toHaveBeenCalledWith(
        personalizacionCollection,
        ...additionalPersonalizacions.map(expect.objectContaining),
      );
      expect(comp.personalizacionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Adicional query and add missing value', () => {
      const venta: IVenta = { id: 456 };
      const adicionales: IAdicional[] = [{ id: 26647 }];
      venta.adicionales = adicionales;

      const adicionalCollection: IAdicional[] = [{ id: 16527 }];
      jest.spyOn(adicionalService, 'query').mockReturnValue(of(new HttpResponse({ body: adicionalCollection })));
      const additionalAdicionals = [...adicionales];
      const expectedCollection: IAdicional[] = [...additionalAdicionals, ...adicionalCollection];
      jest.spyOn(adicionalService, 'addAdicionalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      expect(adicionalService.query).toHaveBeenCalled();
      expect(adicionalService.addAdicionalToCollectionIfMissing).toHaveBeenCalledWith(
        adicionalCollection,
        ...additionalAdicionals.map(expect.objectContaining),
      );
      expect(comp.adicionalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const venta: IVenta = { id: 456 };
      const dispositivo: IDispositivo = { id: 21055 };
      venta.dispositivo = dispositivo;
      const personalizaciones: IPersonalizacion = { id: 12900 };
      venta.personalizaciones = [personalizaciones];
      const adicionales: IAdicional = { id: 4024 };
      venta.adicionales = [adicionales];

      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      expect(comp.dispositivosSharedCollection).toContain(dispositivo);
      expect(comp.personalizacionsSharedCollection).toContain(personalizaciones);
      expect(comp.adicionalsSharedCollection).toContain(adicionales);
      expect(comp.venta).toEqual(venta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenta>>();
      const venta = { id: 123 };
      jest.spyOn(ventaFormService, 'getVenta').mockReturnValue(venta);
      jest.spyOn(ventaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venta }));
      saveSubject.complete();

      // THEN
      expect(ventaFormService.getVenta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ventaService.update).toHaveBeenCalledWith(expect.objectContaining(venta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenta>>();
      const venta = { id: 123 };
      jest.spyOn(ventaFormService, 'getVenta').mockReturnValue({ id: null });
      jest.spyOn(ventaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venta }));
      saveSubject.complete();

      // THEN
      expect(ventaFormService.getVenta).toHaveBeenCalled();
      expect(ventaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenta>>();
      const venta = { id: 123 };
      jest.spyOn(ventaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ventaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDispositivo', () => {
      it('Should forward to dispositivoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(dispositivoService, 'compareDispositivo');
        comp.compareDispositivo(entity, entity2);
        expect(dispositivoService.compareDispositivo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePersonalizacion', () => {
      it('Should forward to personalizacionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(personalizacionService, 'comparePersonalizacion');
        comp.comparePersonalizacion(entity, entity2);
        expect(personalizacionService.comparePersonalizacion).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAdicional', () => {
      it('Should forward to adicionalService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(adicionalService, 'compareAdicional');
        comp.compareAdicional(entity, entity2);
        expect(adicionalService.compareAdicional).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
