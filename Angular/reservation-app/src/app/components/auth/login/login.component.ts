import { Component } from '@angular/core';
import { Router } from '@angular/router'; 
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginData = {
    username: '',
    password: ''
  };

  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.login(this.loginData.username, this.loginData.password).subscribe({
      next: (response) => {
        this.authService.handleAuthentication(response.jwt); // Save the token
        this.router.navigate(['/admin/reservations']); // Redirect on successful login
      },
      error: () => {
        this.errorMessage = 'Invalid login credentials. Please try again.';
      }
    });
  }
}
