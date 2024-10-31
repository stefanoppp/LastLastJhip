import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDispositivo } from 'app/entities/dispositivo/dispositivo.model';
import { DispositivoService } from 'app/entities/dispositivo/service/dispositivo.service';
import { IPersonalizacion } from 'app/entities/personalizacion/personalizacion.model';
import { PersonalizacionService } from 'app/entities/personalizacion/service/personalizacion.service';
import { IAdicional } from 'app/entities/adicional/adicional.model';
import { AdicionalService } from 'app/entities/adicional/service/adicional.service';
import { VentaService } from '../service/venta.service';
import { IVenta } from '../venta.model';
import { VentaFormGroup, VentaFormService } from './venta-form.service';

@Component({
  standalone: true,
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;
  venta: IVenta | null = null;

  dispositivosSharedCollection: IDispositivo[] = [];
  personalizacionsSharedCollection: IPersonalizacion[] = [];
  adicionalsSharedCollection: IAdicional[] = [];

  protected ventaService = inject(VentaService);
  protected ventaFormService = inject(VentaFormService);
  protected dispositivoService = inject(DispositivoService);
  protected personalizacionService = inject(PersonalizacionService);
  protected adicionalService = inject(AdicionalService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VentaFormGroup = this.ventaFormService.createVentaFormGroup();

  compareDispositivo = (o1: IDispositivo | null, o2: IDispositivo | null): boolean => this.dispositivoService.compareDispositivo(o1, o2);

  comparePersonalizacion = (o1: IPersonalizacion | null, o2: IPersonalizacion | null): boolean =>
    this.personalizacionService.comparePersonalizacion(o1, o2);

  compareAdicional = (o1: IAdicional | null, o2: IAdicional | null): boolean => this.adicionalService.compareAdicional(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.venta = venta;
      if (venta) {
        this.updateForm(venta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venta = this.ventaFormService.getVenta(this.editForm);
    if (venta.id !== null) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(venta: IVenta): void {
    this.venta = venta;
    this.ventaFormService.resetForm(this.editForm, venta);

    this.dispositivosSharedCollection = this.dispositivoService.addDispositivoToCollectionIfMissing<IDispositivo>(
      this.dispositivosSharedCollection,
      venta.dispositivo,
    );
    this.personalizacionsSharedCollection = this.personalizacionService.addPersonalizacionToCollectionIfMissing<IPersonalizacion>(
      this.personalizacionsSharedCollection,
      ...(venta.personalizaciones ?? []),
    );
    this.adicionalsSharedCollection = this.adicionalService.addAdicionalToCollectionIfMissing<IAdicional>(
      this.adicionalsSharedCollection,
      ...(venta.adicionales ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dispositivoService
      .query()
      .pipe(map((res: HttpResponse<IDispositivo[]>) => res.body ?? []))
      .pipe(
        map((dispositivos: IDispositivo[]) =>
          this.dispositivoService.addDispositivoToCollectionIfMissing<IDispositivo>(dispositivos, this.venta?.dispositivo),
        ),
      )
      .subscribe((dispositivos: IDispositivo[]) => (this.dispositivosSharedCollection = dispositivos));

    this.personalizacionService
      .query()
      .pipe(map((res: HttpResponse<IPersonalizacion[]>) => res.body ?? []))
      .pipe(
        map((personalizacions: IPersonalizacion[]) =>
          this.personalizacionService.addPersonalizacionToCollectionIfMissing<IPersonalizacion>(
            personalizacions,
            ...(this.venta?.personalizaciones ?? []),
          ),
        ),
      )
      .subscribe((personalizacions: IPersonalizacion[]) => (this.personalizacionsSharedCollection = personalizacions));

    this.adicionalService
      .query()
      .pipe(map((res: HttpResponse<IAdicional[]>) => res.body ?? []))
      .pipe(
        map((adicionals: IAdicional[]) =>
          this.adicionalService.addAdicionalToCollectionIfMissing<IAdicional>(adicionals, ...(this.venta?.adicionales ?? [])),
        ),
      )
      .subscribe((adicionals: IAdicional[]) => (this.adicionalsSharedCollection = adicionals));
  }
}
