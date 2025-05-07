import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ReservationService } from 'src/app/services/Reservation/reservation.service';

@Component({
  selector: 'app-select-date',
  templateUrl: './select-date.component.html',
  styleUrls: ['./select-date.component.scss']
})
export class SelectDateComponent implements OnInit {
  @Input() worker!: any;
  @Input() service!: any;
  @Input() buttonShape: 'PILL' | 'SQUARE' = 'PILL';
  @Output() dateSelected = new EventEmitter<string>();

  selectedDate: string = '';
  availableSlots: string[] = [];
  loadingSlots = false;
  calendarDays: Date[] = [];

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.generateCalendarDays();
  }

  generateCalendarDays() {
    const today = new Date();
    for (let i = 0; i < 14; i++) {
      const date = new Date();
      date.setDate(today.getDate() + i);
      this.calendarDays.push(date);
    }
  }

  formatDate(date: Date): string {
    return date.toISOString().split('T')[0]; // YYYY-MM-DD
  }

  isDateSelectable(date: Date): boolean {
    return true; // Ovde možeš dodati proveru praznika, zatvorenih dana itd.
  }

  selectDate(date: Date) {
    this.selectedDate = this.formatDate(date);
    this.loadingSlots = true;
    this.availableSlots = [];

    this.reservationService.getAvailableSlots(
      this.worker.id,
      this.selectedDate,
      this.service.duration || 30
    ).subscribe({
      next: (slots) => {
        this.availableSlots = slots;
        this.loadingSlots = false;
      },
      error: (err) => {
        console.error('Greška pri učitavanju termina:', err);
        this.loadingSlots = false;
      }
    });
  }

  selectTime(slot: string) {
    this.dateSelected.emit(`${this.selectedDate}T${slot}`);
  }
}
