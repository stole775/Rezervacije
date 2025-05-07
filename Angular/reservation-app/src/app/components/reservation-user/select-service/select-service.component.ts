import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ReservationService } from 'src/app/services/Reservation/reservation.service';

@Component({
  selector: 'app-select-service',
  templateUrl: './select-service.component.html',
  styleUrls: ['./select-service.component.css']
})
export class SelectServiceComponent implements OnInit {
  @Input() worker!: any;
  @Input() prikaziCene: boolean = false;
  @Input() cenovnik: boolean = false;
  @Input() buttonShape: 'PILL' | 'SQUARE' = 'PILL';
  @Output() serviceSelected = new EventEmitter<any>();

  services: any[] = [];
  loading: boolean = true;

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    if (this.worker && this.worker.id) {
      this.reservationService.getUserServices(this.worker.id).subscribe({
        next: (data) => {
          this.services = data;
          this.loading = false;
        },
        error: (err) => {
          console.error('Greška pri učitavanju usluga:', err);
          this.loading = false;
        }
      });
    }
  }

  selectService(service: any) {
    this.serviceSelected.emit(service);
  }
}
