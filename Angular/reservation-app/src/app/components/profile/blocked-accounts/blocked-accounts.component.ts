import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserService } from 'src/app/services/user/user.service';
import { CompanyService } from 'src/app/services/company/company.service'; 

@Component({
  selector: 'app-blocked-accounts',
  templateUrl: './blocked-accounts.component.html',
  styleUrls: ['./blocked-accounts.component.css']
})
export class BlockedAccountsComponent implements OnInit {

  // Polje za čuvanje korisnika (blokiranih ili ne)
  blockedUsers: any[] = [];

  // Polje za čuvanje lista kompanija (koristi se samo za SAdmin-a)
  companies: any[] = [];

  // ID trenutno izabrane kompanije (vrednost je null dok se kompanija ne izabere)
  selectedCompanyId: number | null = null;

  // Uloga trenutno ulogovanog korisnika
  userRole: string = '';

  // Konstruktor - inicijalizujemo potrebne servise (userService, authService, companyService)
  constructor(
    private userService: UserService,
    private authService: AuthService,
    private companyService: CompanyService
  ) {}

  // Ova metoda se pokreće kad se komponenta inicijalizuje
  ngOnInit(): void {
    // Proverava da li je korisnik ulogovan, ako nije, odjavljuje ga
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
    }

    // Postavlja ulogu korisnika iz auth servisa (ili praznu vrednost ako nema uloge)
    this.userRole = this.authService.getUserRole() || "";

    // Ako je ulogovani korisnik SAdmin, učitaj sve kompanije
    if (this.userRole === 'SADMIN') {
      this.loadCompanies(); // SAdmin bira kompaniju
    } 
    // Ako je korisnik CAdmin, učitava korisnike unutar svoje kompanije
    else if (this.userRole === 'CADMIN') {
      this.loadBlockedUsers(); // CAdmin vidi korisnike unutar svoje kompanije
    }
  }

  // Metoda za učitavanje svih kompanija (koristi se za SAdmin-a)
  loadCompanies(): void {
    this.companyService.getAllCompanies().subscribe(
      (companies) => {
        this.companies = companies; // Postavlja učitane kompanije u lokalnu promenljivu
      },
      (error) => {
        console.error('Greška pri učitavanju kompanija', error); // Ispisuje grešku u konzolu ako dođe do problema
      }
    );
  }

  // Ova metoda se pokreće kad se promeni izabrana kompanija (SAdmin biranje kompanije)
  onCompanyChange(): void {
    if (this.selectedCompanyId !== null) {
      this.loadBlockedUsers(this.selectedCompanyId); // Učitaj korisnike za izabranu kompaniju
    }
  }

  // Metoda za učitavanje korisnika (blokiranih i aktivnih) za određenu kompaniju
  loadBlockedUsers(companyId?: number) {
    // Uzimamo ID kompanije: ili izabran od strane SAdmin-a ili iz sesije za CAdmin-a
    const id = companyId || this.authService.getCompanyId(); // Uzmi kompaniju iz sesije za CAdmin-a
    if (id !== null) {
      this.userService.getUsersByCompanyId(id).subscribe(
        (users) => {
          // Postavlja sve korisnike kompanije u listu za prikaz u tabeli
          this.blockedUsers = users; 
          console.log(this.blockedUsers); // Ispisuje korisnike u konzolu radi provere
        },
        (error) => {
          console.error('Greška pri učitavanju korisnika', error); // Ispisuje grešku u konzolu ako dođe do problema
        }
      );
    }
  }

  // Metoda za blokiranje korisnika - poziva se kada korisnik pritisne dugme "Blokiraj"
  blockUser(userId: number) {
    this.userService.blockUser(userId).subscribe({
      next: (response) => {
        console.log(response.message); // Ispisujemo poruku koja dolazi sa servera
        if(this.userRole === 'SADMIN' && this.selectedCompanyId){
          this.loadBlockedUsers(this.selectedCompanyId);
        }else
          this.loadBlockedUsers(); // Osvežavanje liste nakon blokiranja
        
      },
      error: (error) => {
        console.error('Greška prilikom blokiranja korisnika:', error); // Prikaz greške u konzoli
      }
    });
  }
  
  unblockUser(userId: number) {
    this.userService.unblockUser(userId).subscribe({
      next: (response) => {
        console.log(response.message); // Ispisujemo poruku koja dolazi sa servera
        if(this.userRole === 'SADMIN' && this.selectedCompanyId){
          this.loadBlockedUsers(this.selectedCompanyId);
        }else
        this.loadBlockedUsers();
      },
      error: (error) => {
        console.error('Greška prilikom odblokiranja korisnika:', error); // Prikaz greške u konzoli
      }
    });
  }
  
}
