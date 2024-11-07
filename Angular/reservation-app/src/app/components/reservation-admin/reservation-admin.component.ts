import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReservationService } from 'src/app/services/Reservation/reservation.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CompanyService } from 'src/app/services/company/company.service';
import { UserService } from 'src/app/services/user/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reservation-admin',
  templateUrl: './reservation-admin.component.html',
  styleUrls: ['./reservation-admin.component.css']
})
export class ReservationAdminComponent implements OnInit {
  reservationForm: FormGroup;
  userId: number | null = null;
  companyId: number | null = null;
  services: any[] = [];
  companies: any[] = [];
  workers: any[] = [];
  availableSlots: any[] = [];
  isSAdmin: boolean = false;
  isCAdmin: boolean = false;
  isCustomer: boolean = false;

  constructor(
    private fb: FormBuilder,
    private reservationService: ReservationService,
    private authService: AuthService,
    private companyService: CompanyService,
    private userService: UserService
  ) {
    this.reservationForm = this.fb.group({
      uslugaId: ['', Validators.required],
      name: ['', Validators.required],
      phone: ['+3816', [Validators.required, Validators.pattern(/^(\+3816)\d{7,9}$/)]],
      appointmentDate: ['', Validators.required],
      vremeTrajanja: [30, Validators.required],
      companyId: [''],
      workerId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
    }

    this.userId = this.authService.getUserId();
    const userRole = this.authService.getUserRole();
    this.isSAdmin = userRole === 'SADMIN';
    this.isCAdmin = userRole === 'CADMIN';
    this.isCustomer = userRole === 'CUSTOMER';
    this.companyId = this.authService.getCompanyId();

    if (this.isSAdmin) {
      this.loadAllReservations();
      this.loadCompanies();
    } else if (this.isCAdmin && this.companyId) {
      this.loadWorkersByCompany(this.companyId);
      this.loadUserReservations();
    } else if (this.isCustomer) {
      this.loadUserReservations();
    }

    if (this.isCAdmin || this.isCustomer) {
      this.reservationForm.patchValue({
        companyId: this.companyId
      });

      if (this.isCustomer) {
        this.reservationForm.patchValue({
          workerId: this.userId,
          vremeTrajanja: 30
        });
      }
    }

    this.loadUserServices(this.userId!);

    this.reservationForm.get('companyId')?.valueChanges.subscribe(companyId => {
      if (companyId) {
        this.loadWorkersByCompany(companyId);
      }
    });
/*
    this.reservationForm.get('workerId')?.valueChanges.subscribe(workerId => {
      if (workerId && this.reservationForm.get('appointmentDate')?.value) {
        this.loadAvailableSlots(workerId, this.reservationForm.get('appointmentDate')?.value);
      }
    });

    this.reservationForm.get('appointmentDate')?.valueChanges.subscribe(appointmentDate => {
      if (appointmentDate && this.reservationForm.get('workerId')?.value) {
        this.loadAvailableSlots(this.reservationForm.get('workerId')?.value, appointmentDate);
      }
    });
  }
*/}
  loadUserServices(userId: number): void {
    this.reservationService.getUserServices(userId).subscribe(services => {
      this.services = services;
    });
  }

  loadAllReservations(): void {
    this.reservationService.getAllReservations().subscribe(reservations => {
      console.log('Sve rezervacije:', reservations);
    });
  }

  loadUserReservations(): void {
    this.reservationService.getUserReservations(this.userId!).subscribe(reservations => {
      console.log(`Rezervacije za korisnika ${this.userId}:`, reservations);
    });
  }

  loadCompanies(): void {
    this.companyService.getAllCompanies().subscribe(companies => {
      this.companies = companies;
    });
  }

  loadWorkersByCompany(companyId: number): void {
    this.userService.getUsersByCompanyId(companyId).subscribe(workers => {
      this.workers = workers;
    });
  }
/*
  loadAvailableSlots(workerId: number, date: string): void {
    this.reservationService.getAvailableSlots(workerId, date).subscribe(slots => {
      this.availableSlots = slots;
    });
  }
*/
  calculateEndTime(appointmentDate: string, vremeTrajanja: number): string {
    const appointment = new Date(appointmentDate);
    appointment.setMinutes(appointment.getMinutes() + vremeTrajanja);
    return appointment.toISOString().slice(0, 16);
  }

  onSubmit(): void {
    if (this.reservationForm.valid) {
      const formValue = this.reservationForm.value;
      const reservationData: any = {
        usluga: { id: formValue.uslugaId },
        name: formValue.name,
        phone: formValue.phone,
        appointmentDate: formValue.appointmentDate,
        vremeTrajanja: formValue.vremeTrajanja,
        vremeZavrsetka: this.calculateEndTime(formValue.appointmentDate, formValue.vremeTrajanja),
        confirmed: this.isSAdmin,
        user: { id: this.userId }
      };

      if (this.isSAdmin) {
        reservationData.companyId = formValue.companyId;
        reservationData.worker = { id: formValue.workerId };
      } else if (this.isCAdmin) {
        reservationData.companyId = this.companyId;
        reservationData.worker = { id: formValue.workerId };
      } else if (this.isCustomer) {
        reservationData.companyId = this.companyId;
        reservationData.worker = { id: this.userId };
        reservationData.vremeTrajanja = 30;
        reservationData.vremeZavrsetka = this.calculateEndTime(formValue.appointmentDate, 30);
      }

      this.reservationService.createReservation(reservationData).subscribe({
        next: () => {
          alert(this.isSAdmin ? 'Rezervacija uspešno kreirana' : 'Termin uspešno dodan');
          this.reservationForm.reset({
            uslugaId: '',
            name: '',
            phone: '',
            appointmentDate: '',
            vremeTrajanja: 30,
            companyId: this.isSAdmin ? '' : this.companyId,
            workerId: this.isCustomer ? this.userId : ''
          });
        },
        error: () => {
          alert('Došlo je do greške prilikom kreiranja rezervacije.');
        }
      });
    } else {
      alert('Molimo vas da popunite sva obavezna polja.');
    }
  }
}
