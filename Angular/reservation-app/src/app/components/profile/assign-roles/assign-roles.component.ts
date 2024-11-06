import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'; // Za preusmeravanje korisnika
import { UserService } from 'src/app/services/user/user.service';
import { CompanyService } from 'src/app/services/company/company.service';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-assign-roles',
  templateUrl: './assign-roles.component.html',
  styleUrls: ['./assign-roles.component.css']
})
export class AssignRolesComponent implements OnInit {

  users: any[] = [];
  companies: any[] = [];
  selectedCompanyId: number | null = null;
  userRole: string = '';

  constructor(
    private userService: UserService,
    private companyService: CompanyService,
    private authService: AuthService,
    private router: Router // Za preusmeravanje korisnika
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
    }
    this.userRole = this.authService.getUserRole()||"";

    if (this.userRole === 'SADMIN') {
      this.loadCompanies(); // SAdmin bira kompaniju
    } else if (this.userRole === 'CADMIN') {
      this.loadUsersByCompany(); // CAdmin vidi korisnike unutar svoje kompanije
    } else {
      this.router.navigate(['/profile']); // Preusmeri Customer-a na profil
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

  onCompanyChange(): void {
    if (this.selectedCompanyId !== null) {
      this.loadUsersByCompany(this.selectedCompanyId);
    }
  }

  loadUsersByCompany(companyId?: number): void {
    const id = companyId || this.authService.getCompanyId();
    if (id !== null) {
      this.userService.getUsersByCompanyId(id).subscribe(
        (users) => {
          this.users = users;
  
          // Automatski popunjava trenutnu rolu kao newRole
          this.users.forEach(user => {
            user.newRole = user.role.name; // Inicijalizacija sa trenutnom ulogom
          });
        },
        (error) => {
          console.error('Greška pri učitavanju korisnika', error);
        }
      );
    }
  }

  assignRole(user: any): void {
    if (!user.newRole) {
      alert('Niste izabrali novu ulogu.');
      return;
    }
  
    const companyId = this.selectedCompanyId || this.authService.getCompanyId(); // Dobijamo ID kompanije
  
    if (!companyId) {
      alert('Niste odabrali kompaniju.');
      return;
    }
  
    const roleData = {
      userId: user.id,
      roleName: user.newRole,
      companyId: companyId
    };
  
    console.log(roleData);
  
    this.userService.assignRole(roleData).subscribe(
      (response: any) => {
        // Očekujemo JSON odgovor sa ključem 'message'
        if (response && response.message) {
          alert(response.message);
          // Nakon alert poruke osveži stranicu
          window.location.reload(); // Osvežava celu stranicu
        }
      },
      (error) => {
        console.error('Greška pri dodeljivanju uloge', error);
        if (error.error && error.error.message) {
          alert('Greška pri dodeljivanju uloge: ' + error.error.message);
        } else {
          alert('Greška pri dodeljivanju uloge.');
        }
      }
    );
  }
  
  

  
}
