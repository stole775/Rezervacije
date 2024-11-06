import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth/auth.service';
import { NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'reservation-app';
  isLoginPage: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        // Check if the current route is login
        this.isLoginPage = event.url === '/';
      }
    });
    this.checkAuthentication();
  }

  checkAuthentication(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout(); // Log out if the token is expired or not available
    }
  }
}
