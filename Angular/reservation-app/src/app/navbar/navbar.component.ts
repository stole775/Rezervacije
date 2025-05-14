// navbar.component.ts
import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
})
export class NavbarComponent {
  isAddReservationPage: boolean = false;
  isReservationsPage: boolean = false;
  isMenuOpen: boolean = false;
  isCustomer: boolean = false;

  constructor(private authService: AuthService, private router: Router) {
    // Check if user is CUSTOMER
    this.isCustomer = this.authService.getUserRole() === 'CUSTOMER';

    // Subscribe to route changes to detect the current page
    this.router.events.subscribe((event: any) => {
      if (event instanceof NavigationEnd) {
        // Use urlAfterRedirects to get the final URL after redirects
        this.isAddReservationPage = event.urlAfterRedirects === '/admin/add-reservation';
        this.isReservationsPage = event.urlAfterRedirects === '/admin/reservations';
      }
    });
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/admin']);

    this.closeMenu();
  }
}
