import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ReservationService } from 'src/app/services/Reservation/reservation.service'; 
import { EditReservationDialogComponent } from './edit-reservation-dialog/edit-reservation-dialog.component';
import { DeleteConfirmationDialogComponent } from './delete-confirmation-dialog/delete-confirmation-dialog.component';
import { AuthService } from 'src/app/services/auth/auth.service'; 
import { CompanyService } from 'src/app/services/company/company.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-reservation-table',
  templateUrl: './reservation-table.component.html',
  styleUrls: ['./reservation-table.component.css']
})
export class ReservationTableComponent implements OnInit { 
    
    selectedReservationIndex: number | null = null; // Pratimo koji red je selektovan
   
  reservations: any[] = [];
  filteredReservations: any[] = [];
  filterTerm: string = '';
  confirmationStatus: string = ''; 
  dateFrom: string = ''; 
  dateTo: string = ''; 
  showAdditionalFilters: boolean = false; 
  userRole: string = ''; 
  userId: number | null = null; 
  isAdmin: boolean = false;
  isUser: boolean = false;  
  companyid: number | null = null;
  services: any[] = []; 
  companies: any[] = []; 
  workers: any[] = []; 
  isSAdmin: boolean = false;
  isCAdmin: boolean = false;
  isCustomer: boolean = false;

  constructor(
    private reservationService: ReservationService, 
    private dialog: MatDialog,
    private authService: AuthService,
    private companyService: CompanyService,
    private userService: UserService,
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
    }

    this.userId = this.authService.getUserId();
    const userRole = this.authService.getUserRole();
  
    this.isSAdmin = userRole === 'SADMIN';
    this.isCAdmin = userRole === 'CADMIN';
    this.isCustomer = userRole === 'CUSTOMER';
    this.companyid = this.authService.getCompanyId();
    
    this.loadReservations();
  }

  loadReservations(): void {
    if (this.isSAdmin) {
      this.loadAllReservations();
    } else if (this.isCAdmin && this.companyid) {
      this.reservationService.getUserReservationsByCompanyid(this.companyid).subscribe(
        (reservations) => {
          this.reservations = reservations;
          this.applyFilter();
        }, 
        (error) => {
          console.error('Greška prilikom učitavanja rezervacija za CADMIN:', error);
        }
      );
    } else if (this.isCustomer && this.userId) {
      this.loadUserReservations(this.userId);
    }
  }

  // Method to toggle additional filters
  toggleAdditionalFilters(): void {
    this.showAdditionalFilters = !this.showAdditionalFilters;
  }

  // Method to apply all filters
  applyFilter(): void {
    const term = this.filterTerm ? this.filterTerm.toLowerCase() : '';
    const confirmation = this.confirmationStatus === '' ? null : this.confirmationStatus === 'true';
    const fromDate = this.dateFrom ? new Date(this.dateFrom) : null;
    const toDate = this.dateTo ? new Date(this.dateTo) : null;
  
    this.filteredReservations = this.reservations.filter((reservation) => {
      const matchesText = (reservation.name && reservation.name.toLowerCase().includes(term)) ||
                          (reservation.usluga && reservation.usluga.naziv && reservation.usluga.naziv.toLowerCase().includes(term)) ||
                          (reservation.phone && reservation.phone.includes(term));
  
      // Provera za potvrđeno polje sada direktno poredi sa boolean vrednostima
      const matchesConfirmation = confirmation === null || reservation.confirmed === (confirmation === true);
  
      const reservationDate = new Date(reservation.appointmentDate);
      const matchesDate = (!fromDate || reservationDate >= fromDate) && 
                          (!toDate || reservationDate <= toDate);
  
      return matchesText && matchesConfirmation && matchesDate;
    });
  }
  

  openEditDialog(reservation: any): void {
    const dialogRef = this.dialog.open(EditReservationDialogComponent, {
      width: '500px',
      data: reservation
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'updated') {
        this.loadReservations();
      }
    });
  }

  openDeleteDialog(reservationId: number): void {
    const dialogRef = this.dialog.open(DeleteConfirmationDialogComponent, {
      width: '400px',
      data: { id: reservationId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirmed') {
        this.reservationService.deleteReservation(reservationId).subscribe(() => {
          this.loadReservations();
        });
      }
    });
  }

  // Učitaj sve rezervacije za admina
  loadAllReservations(): void {
    this.reservationService.getAllReservations().subscribe((reservations) => {
      this.reservations = reservations;
      this.applyFilter();
    }, (error) => {
      console.error('Greška prilikom učitavanja rezervacija:', error);
    });
  }

  // Učitaj rezervacije za specifičnog korisnika
  loadUserReservations(userId: number): void {
    this.reservationService.getUserReservations(userId).subscribe((reservations) => {
      this.reservations = reservations;
      this.applyFilter();
    }, (error) => {
      console.error('Greška prilikom učitavanja rezervacija:', error);
    });
  }

  toggleDetails(index: number): void {
    console.log('Toggled row: ', index); // Proverite da li se ovo ispisuje
    if (this.selectedReservationIndex === index) {
      this.selectedReservationIndex = null; // Ako kliknemo ponovo, zatvaramo red
    } else {
      this.selectedReservationIndex = index; // Otvaramo red
    }
  }
   



}
