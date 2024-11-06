import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {
  
  userRole: string = '';
  activeTab: string = 'assignRolesTab'; // Podrazumevani tab

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.checkUserLogin();
    this.userRole = this.authService.getUserRole() || '';
    if(this.userRole=="CUSTOMER")
      {
        this.setActiveTab("editProfileTab");
      }
  }

  // Provera da li je korisnik ulogovan
  checkUserLogin(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
      this.router.navigate(['/login']); // Preusmeri na login ako nije ulogovan
    }
  
  }

  // Promena aktivnog taba
  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }
}
 
 
 