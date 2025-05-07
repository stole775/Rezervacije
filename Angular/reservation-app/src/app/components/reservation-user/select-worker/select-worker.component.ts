import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ReservationService } from 'src/app/services/Reservation/reservation.service';

@Component({
  selector: 'app-select-worker',
  templateUrl: './select-worker.component.html',
  styleUrls: ['./select-worker.component.css']
})
export class SelectWorkerComponent implements OnInit {
  @Input() buttonShape: 'PILL' | 'SQUARE' = 'PILL';
  @Output() workerSelected = new EventEmitter<any>();

  workers: any[] = [];
  loading = true;

  constructor(private workerService: ReservationService) {}

  @Input() companyId!: number;

ngOnInit(): void {
  if (this.companyId) {
    this.workerService.getWorkersByCompanyId(this.companyId).subscribe({
      next: (data) => {
        this.workers = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Greška pri učitavanju radnika:', err);
        this.loading = false;
      }
    });
  }else{
    console.error('Greška pri učitavanju companyid:');
        this.loading = false;
  }
}


  selectWorker(worker: any) {
    this.workerSelected.emit(worker);
  }
}
