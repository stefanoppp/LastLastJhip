import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDispositivo } from 'app/entities/dispositivo/dispositivo.model';
import { DispositivoService } from 'app/entities/dispositivo/service/dispositivo.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { PersonalizacionService } from '../service/personalizacion.service';
import { IPersonalizacion } from '../personalizacion.model';
import { PersonalizacionFormGroup, PersonalizacionFormService } from './personalizacion-form.service';

@Component({
  standalone: true,
  selector: 'jhi-personalizacion-update',
  templateUrl: './personalizacion-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PersonalizacionUpdateComponent implements OnInit {
  isSaving = false;
  personalizacion: IPersonalizacion | null = null;

  dispositivosSharedCollection: IDispositivo[] = [];
  ventasSharedCollection: IVenta[] = [];

  protected personalizacionService = inject(PersonalizacionService);
  protected personalizacionFormService = inject(PersonalizacionFormService);
  protected dispositivoService = inject(DispositivoService);
  protected ventaService = inject(VentaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PersonalizacionFormGroup = this.personalizacionFormService.createPersonalizacionFormGroup();

  compareDispositivo = (o1: IDispositivo | null, o2: IDispositivo | null): boolean => this.dispositivoService.compareDispositivo(o1, o2);

  compareVenta = (o1: IVenta | null, o2: IVenta | null): boolean => this.ventaService.compareVenta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalizacion }) => {
      this.personalizacion = personalizacion;
      if (personalizacion) {
        this.updateForm(personalizacion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personalizacion = this.personalizacionFormService.getPersonalizacion(this.editForm);
    if (personalizacion.id !== null) {
      this.subscribeToSaveResponse(this.personalizacionService.update(personalizacion));
    } else {
      this.subscribeToSaveResponse(this.personalizacionService.create(personalizacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalizacion>>): void {
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

  protected updateForm(personalizacion: IPersonalizacion): void {
    this.personalizacion = personalizacion;
    this.personalizacionFormService.resetForm(this.editForm, personalizacion);

    this.dispositivosSharedCollection = this.dispositivoService.addDispositivoToCollectionIfMissing<IDispositivo>(
      this.dispositivosSharedCollection,
      personalizacion.dispositivo,
    );
    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing<IVenta>(
      this.ventasSharedCollection,
      ...(personalizacion.ventas ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dispositivoService
      .query()
      .pipe(map((res: HttpResponse<IDispositivo[]>) => res.body ?? []))
      .pipe(
        map((dispositivos: IDispositivo[]) =>
          this.dispositivoService.addDispositivoToCollectionIfMissing<IDispositivo>(dispositivos, this.personalizacion?.dispositivo),
        ),
      )
      .subscribe((dispositivos: IDispositivo[]) => (this.dispositivosSharedCollection = dispositivos));

    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(
        map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing<IVenta>(ventas, ...(this.personalizacion?.ventas ?? []))),
      )
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }
}
