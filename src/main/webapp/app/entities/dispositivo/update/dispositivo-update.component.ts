import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Moneda } from 'app/entities/enumerations/moneda.model';
import { IDispositivo } from '../dispositivo.model';
import { DispositivoService } from '../service/dispositivo.service';
import { DispositivoFormGroup, DispositivoFormService } from './dispositivo-form.service';

@Component({
  standalone: true,
  selector: 'jhi-dispositivo-update',
  templateUrl: './dispositivo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DispositivoUpdateComponent implements OnInit {
  isSaving = false;
  dispositivo: IDispositivo | null = null;
  monedaValues = Object.keys(Moneda);

  protected dispositivoService = inject(DispositivoService);
  protected dispositivoFormService = inject(DispositivoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DispositivoFormGroup = this.dispositivoFormService.createDispositivoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dispositivo }) => {
      this.dispositivo = dispositivo;
      if (dispositivo) {
        this.updateForm(dispositivo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dispositivo = this.dispositivoFormService.getDispositivo(this.editForm);
    if (dispositivo.id !== null) {
      this.subscribeToSaveResponse(this.dispositivoService.update(dispositivo));
    } else {
      this.subscribeToSaveResponse(this.dispositivoService.create(dispositivo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDispositivo>>): void {
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

  protected updateForm(dispositivo: IDispositivo): void {
    this.dispositivo = dispositivo;
    this.dispositivoFormService.resetForm(this.editForm, dispositivo);
  }
}
