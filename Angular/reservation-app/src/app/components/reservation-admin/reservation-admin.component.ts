import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReservationService } from 'src/app/services/Reservation/reservation.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CompanyService } from 'src/app/services/company/company.service';
import { UserService } from 'src/app/services/user/user.service';
import { formatDate } from '@angular/common';

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
  availableSlots: any[] = []; // Slobodni termini

  isSAdmin: boolean = false;
  isCAdmin: boolean = false;
  isUSER: boolean = false;

  constructor(
    private fb: FormBuilder,
    private reservationService: ReservationService,
    private authService: AuthService,
    private companyService: CompanyService,
    private userService: UserService
  ) {
    this.reservationForm = this.fb.group({
      companyId: ['', Validators.required],
      workerId: ['', Validators.required],
      uslugaId: ['', Validators.required],
      appointmentDate: ['', Validators.required], // Samo datum
      slot: ['', Validators.required], // Slobodan termin
      vremeTrajanja: [30, Validators.required],
      name: ['', Validators.required],
      phone: ['+3816', [Validators.required, Validators.pattern(/^(\+3816)\d{7,9}$/)]]
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
    this.isUSER = userRole === 'USER';
    this.companyId = this.authService.getCompanyId();

    if (this.isSAdmin) {
      this.loadCompanies();
    } else if (this.isCAdmin && this.companyId) {
      this.loadWorkersByCompany(this.companyId);
    }

    this.onFieldChanges();
  }

  onFieldChanges(): void {
    // Na promenu kompanije
    this.reservationForm.get('companyId')?.valueChanges.subscribe(companyId => {
      if (companyId) {
        this.loadWorkersByCompany(companyId);
        this.resetField('workerId');
        this.resetField('uslugaId');
        this.services = [];
        this.availableSlots = [];
      }
    });

    // Na promenu radnika
    this.reservationForm.get('workerId')?.valueChanges.subscribe(workerId => {
      if (workerId) {
        this.userId=workerId;
        this.loadUserServicesByWorker(workerId);
        this.resetField('uslugaId');
        this.resetField('appointmentDate');
        this.availableSlots = [];
      }
    });

    // Na promenu usluge
    this.reservationForm.get('uslugaId')?.valueChanges.subscribe(uslugaId => {
      if (uslugaId) {
        const selectedService = this.services.find(service => service.id === uslugaId);
        if (selectedService) {
          this.reservationForm.patchValue({ vremeTrajanja: selectedService.trajanje });
        }
        this.checkAvailability();
      }
    });

    // Na promenu datuma termina
    this.reservationForm.get('appointmentDate')?.valueChanges.subscribe(() => {
      this.checkAvailability();
    });

    // Na promenu trajanja termina
    this.reservationForm.get('vremeTrajanja')?.valueChanges.subscribe(() => {
      this.checkAvailability();
    });
  }

  checkAvailability(): void {
    const workerId = this.reservationForm.get('workerId')?.value;
    const trajanje = this.reservationForm.get('vremeTrajanja')?.value;
    const date = this.reservationForm.get('appointmentDate')?.value;

    if (workerId && trajanje && date) {
      this.loadAvailableSlots(workerId, date, trajanje);
    } else {
      this.availableSlots = []; // Resetuje slobodne termine ako neki od uslova nije ispunjen
    }
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

  loadUserServicesByWorker(workerId: number): void {
    this.reservationService.getUserServices(workerId).subscribe(services => {
      this.services = services;
    });
  }

  loadAvailableSlots(workerId: number, date: string, trajanje: number): void {
    this.reservationService.getAvailableSlots(workerId, date, trajanje).subscribe(slots => {
      this.availableSlots = slots;
    });
  }

  private resetField(fieldName: string): void {
    this.reservationForm.patchValue({ [fieldName]: '' });
  }

  onSubmit(): void {
    if (this.reservationForm.valid) {
      const formValue = this.reservationForm.value;
      const reservationData = {
        usluga: { id: formValue.uslugaId },
        name: formValue.name,
        phone: formValue.phone,
        appointmentDate: `${formValue.appointmentDate}T${formValue.slot}`,
        user: { id: this.userId },
        companyId: formValue.companyId,
        worker: { id: formValue.workerId }
      };

      this.reservationService.createReservation(reservationData).subscribe({
        next: () => {
          alert('Rezervacija uspešno kreirana');
          this.reservationForm.reset();
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
