import { Component, OnInit } from '@angular/core'; 
import { AuthService } from 'src/app/services/auth/auth.service'; 
import { CompanyService } from 'src/app/services/company/company.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  userRole: string = '';
  companies: any[] = [];
  users: any[] = [];
  selectedCompanyId: number | null = null;
  selectedUserId: number | null = null;
  userData: any = null;

  errorMessage: string = ""; // Poruka o grešci
  successMessage: string = ""; // Poruka o uspehu

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private companyService: CompanyService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
    }
    this.userRole = this.authService.getUserRole() || ''; 
    if (this.userRole === 'SADMIN') {
      this.loadCompanies(); 
    } else if (this.userRole === 'CADMIN') {
      this.selectedCompanyId = this.authService.getCompanyId();
      this.selectedUserId = this.authService.getUserId();
      this.loadUsersByCompany(); 
    } else {
      this.selectedUserId = this.authService.getUserId();
      this.selectedCompanyId = this.authService.getCompanyId();
      this.loadCurrentUser(); 
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
    if (companyId) {
      this.userService.getUsersByCompanyId(companyId).subscribe(
        (users) => {
          this.users = users;
        },
        (error) => {
          console.error('Greška pri učitavanju korisnika', error);
        }
      );
    }
  }

  loadCurrentUser(): void {
    const userId = this.authService.getUserId(); 
    if (userId) {
      this.userService.getUserById(userId).subscribe(
        (user) => {
          this.userData = user;
        },
        (error) => {
          console.error('Greška pri učitavanju podataka korisnika', error);
        }
      );
    }
  }

  onCompanyChange(): void {
    if (this.selectedCompanyId !== null) {
      this.userService.getUsersByCompanyId(this.selectedCompanyId).subscribe(
        (users) => {
          this.users = users;
        },
        (error) => {
          console.error('Greška pri učitavanju korisnika kompanije', error);
        }
      );
    }
  }

  onUserChange(): void {
    if (this.selectedUserId) {
      this.userService.getUserById(this.selectedUserId).subscribe(
        (user) => {
          this.userData = user;
        },
        (error) => {
          console.error('Greška pri učitavanju podataka korisnika', error);
        }
      );
    }
  }

  saveChanges(): void {
    this.successMessage = ''; // Reset uspešne poruke
    this.errorMessage = ''; // Reset poruke o grešci

    const updatedUser = {
      id: this.authService.getUserId(),
      username: this.userData.username,
      ime: this.userData.ime,
      prezime: this.userData.prezime,
      email: this.userData.email,
      smsNaslov: "/", 
      blocked: this.userData.blocked, 
      role: this.userData.role,
      company: this.userData.company,
      password: "****"
    };

    if ((this.userRole !== 'CUSTOMER')) {
      updatedUser.id = this.selectedUserId;
    }
    
    if (updatedUser.id) {
      this.userService.updateUser(updatedUser.id, updatedUser).subscribe(
        () => {
          this.successMessage = 'Podaci uspešno ažurirani'; // Prikazivanje poruke o uspehu
        },
        (error) => {
          if (error.status === 409) {
            const errorData = error.error;
            if (errorData.field === 'username') {
              this.errorMessage = "Korisničko ime je zauzeto. Izaberite drugo korisničko ime.";
            } else if (errorData.field === 'email') {
              this.errorMessage = "Email je zauzet. Izaberite drugi email.";
            } else {
              this.errorMessage = "Došlo je do greške. Pokušajte ponovo.";
            }
          } else {
            this.errorMessage = 'Greška pri ažuriranju podataka. Pokušajte ponovo.';
          }
          console.error('Greška pri ažuriranju podataka', error);
        }
      );
    }
  }
}
