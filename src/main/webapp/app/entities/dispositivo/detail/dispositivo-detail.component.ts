import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDispositivo } from '../dispositivo.model';

@Component({
  standalone: true,
  selector: 'jhi-dispositivo-detail',
  templateUrl: './dispositivo-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DispositivoDetailComponent {
  dispositivo = input<IDispositivo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
