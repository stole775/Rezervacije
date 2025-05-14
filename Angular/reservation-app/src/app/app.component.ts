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
  isReservationUserPage: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const currentUrl = event.urlAfterRedirects || event.url;
  
        this.isLoginPage = currentUrl === '/';
  
        const segments = currentUrl.split('/').filter(Boolean);
        // ako URL ima samo jedan segment, npr. /Telefoni018
        this.isReservationUserPage = segments.length === 1 && !!segments[0];
//        this.isReservationUserPage = !this.authService.isLoggedIn() && segments.length === 1;

      }
    });
  
    // this.checkAuthentication(); // možeš ovo ostaviti ako rešiš prethodni problem
  }
  

  checkAuthentication(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout(); // Log out if the token is expired or not available
    }
  }
}
