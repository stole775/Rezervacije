import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { CompanyService } from 'src/app/services/company/company.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  newPassword: string = '';
  confirmPassword: string = '';
  userRole: string = '';
  selectedCompanyId: number | null = null;
  selectedUserId: number | null = null;
  companies: any[] = [];
  users: any[] = [];
  errorMessage: string = ''; // Čuvanje poruke o grešci
  successMessage: string = ''; // Čuvanje poruke o uspehu

  constructor(
    private userService: UserService,
    private companyService: CompanyService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
    }
    this.userRole = this.authService.getUserRole() || ""; // Dobijanje uloge korisnika
    if (this.userRole === 'SADMIN') {
      this.loadCompanies();
    } else if (this.userRole === 'CADMIN') {
      this.router.navigate(['/reservations']);
    }
  }

  loadCompanies(): void {
    this.companyService.getAllCompanies().subscribe(
      (companies) => {
        this.companies = companies;
      },
      (error) => {
        console.error('Greška pri učitavanju kompanija', error);
      }
    );
  }

  loadUsersByCompany(): void {
    const companyId = this.authService.getCompanyId();
    if (companyId !== null) {
      this.userService.getUsersByCompanyId(companyId).subscribe(
        (users) => {
          this.users = users;
        },
        (error) => {
          console.error('Greška pri učitavanju korisnika kompanije', error);
        }
      );
    } else {
      this.errorMessage = 'Niste odabrali kompaniju.';
    }
  }

  onCompanyChange(): void {
    if (this.selectedCompanyId !== null) {
      this.userService.getUsersByCompanyId(this.selectedCompanyId).subscribe(
        (users) => {
          this.users = users;
        },
        (error) => {
          console.error('Greška pri učitavanju korisnika', error);
        }
      );
    }
  }

  // Validacija lozinke
  validatePassword(): string {
    if (this.newPassword.length < 8) {
      return 'Lozinka mora imati najmanje 8 karaktera.';
    }
    if (!/[A-Z]/.test(this.newPassword)) {
      return 'Lozinka mora sadržati najmanje jedno veliko slovo.';
    }
    if (!/[0-9]/.test(this.newPassword)) {
      return 'Lozinka mora sadržati najmanje jedan broj.';
    }
    return '';
  }

  // Promena lozinke za korisnika
  changePassword(): void {
    this.errorMessage = ''; // Resetovanje greške
    this.successMessage = ''; // Resetovanje uspešne poruke

    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Lozinke se ne poklapaju!';
      return;
    }

    if (this.userRole === 'SADMIN' && this.selectedCompanyId === null) {
      this.errorMessage = 'Niste odabrali kompaniju.';
      return;
    }
    
    if (this.userRole === 'CADMIN') {
      this.selectedCompanyId = this.authService.getCompanyId();
    }

    if ((this.userRole === 'SADMIN' || this.userRole === 'CADMIN') && this.selectedUserId === null) {
      this.errorMessage = 'Niste odabrali korisnika.';
      return;
    }

    // Validacija lozinke
    const passwordValidationMessage = this.validatePassword();
    if (passwordValidationMessage) {
      this.errorMessage = passwordValidationMessage;
      return;
    }

    const userId = this.selectedUserId || this.authService.getUserId();
    if (userId !== null) {
      const passwordData = { userId, newPassword: this.newPassword };

      this.userService.changePassword(passwordData).subscribe(
        (response: any) => {
          if (response && response.message) {
            this.successMessage = response.message; // Uspešna poruka
          }
        },
        (error) => {
          this.errorMessage = error.error?.message || 'Greška pri promeni lozinke. Pokušajte ponovo.';
          console.error('Greška pri promeni lozinke', error);
        }
      );
      
    } else {
      this.errorMessage = 'Korisnički ID nije validan.';
    }
  }
}
