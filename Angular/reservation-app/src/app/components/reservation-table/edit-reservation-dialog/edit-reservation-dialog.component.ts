import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ReservationService } from 'src/app/services/Reservation/reservation.service';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-edit-reservation-dialog',
  templateUrl: './edit-reservation-dialog.component.html',
  styleUrls: ['./edit-reservation-dialog.component.css']
})
export class EditReservationDialogComponent implements OnInit {
  isAdmin: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<EditReservationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public reservation: any,  // Use the passed reservation object directly
    private reservationService: ReservationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.isUserAdmin(); // Check if user is admin
  }

  // Method to calculate the end time of the appointment
  calculateEndTime(appointmentDate: string, vremeTrajanja: number): string {
    const appointment = new Date(appointmentDate);
    appointment.setMinutes(appointment.getMinutes() + vremeTrajanja);

    const year = appointment.getFullYear();
    const month = String(appointment.getMonth() + 1).padStart(2, '0');
    const day = String(appointment.getDate()).padStart(2, '0');
    const hours = String(appointment.getHours()).padStart(2, '0');
    const minutes = String(appointment.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
  }

  // Save changes
  onSave(): void {
    if (this.reservation) {
      // Calculate the end time
      this.reservation.vremeZavrsetka = this.calculateEndTime(this.reservation.appointmentDate,  this.reservation.vremeTrajanja);
console.log(this.reservation);

      // Send updated reservation to the backend
      this.reservationService.updateReservation(this.reservation.id, this.reservation).subscribe({
        next: (response) => {
          this.dialogRef.close('updated');  // Close dialog after update
        },
        error: (error) => {
          console.error('Error updating reservation:', error);
        }
      });
    }
  }

  // Cancel the edit
  onCancel(): void {
    this.dialogRef.close();
  }
}
