import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { addDays, startOfToday } from 'date-fns';

@Component({
  selector: 'app-select-date',
  templateUrl: './select-date.component.html',
  styleUrls: ['./select-date.component.css']
})
export class SelectDateComponent implements OnInit {
  @Input() companyId!: number;
  @Output() dateSelected = new EventEmitter<any>();
  @Input() worker: any;
  @Input() service: any;
  @Input() buttonShape: 'PILL' | 'SQUARE' = 'PILL'; 
  
  today: Date = startOfToday();
  currentStartDate: Date = startOfToday();
  displayedDates: Date[] = [];
  selectedDate: Date | null = null;
  timeSlots: string[] = [];

  ngOnInit(): void {
    this.updateDisplayedDates();
  }

  updateDisplayedDates(): void {
    this.displayedDates = [];
    for (let i = 0; i < 14; i++) {
      this.displayedDates.push(addDays(this.currentStartDate, i));
    }
  }

  previousWeek(): void {
    const newStart = addDays(this.currentStartDate, -7);
    if (newStart >= this.today) {
      this.currentStartDate = newStart;
      this.updateDisplayedDates();
    }
  }

  nextWeek(): void {
    this.currentStartDate = addDays(this.currentStartDate, 7);
    this.updateDisplayedDates();
  }

  isPastDate(date: Date): boolean {
    const today = startOfToday();
    return date < today;
  }

  isPastTime(time: string): boolean {
    if (!this.selectedDate) return false;
    const now = new Date();
    const [hours, minutes] = time.split(':').map(Number);
    const slotDate = new Date(this.selectedDate);
    slotDate.setHours(hours, minutes, 0, 0);
    return slotDate < now;
  }

  selectDate(date: Date): void {
    this.selectedDate = date;
    this.generateTimeSlots(date);
  }

  generateTimeSlots(date: Date): void {
    this.timeSlots = [];
    for (let hour = 8; hour <= 20; hour++) {
      this.timeSlots.push(`${hour.toString().padStart(2, '0')}:00`);
      this.timeSlots.push(`${hour.toString().padStart(2, '0')}:30`);
    }
  }

  selectTime(time: string): void {
    this.dateSelected.emit({ date: this.selectedDate, time });
  }

  get weekLabel(): string {
    const start = this.displayedDates[0];
    const end = this.displayedDates[this.displayedDates.length - 1];
    return `${start.toLocaleDateString()} - ${end.toLocaleDateString()}`;
  }
}
