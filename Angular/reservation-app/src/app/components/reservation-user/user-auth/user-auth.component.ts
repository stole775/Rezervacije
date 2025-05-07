import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-user-auth',
  templateUrl: './user-auth.component.html',
  styleUrls: ['./user-auth.component.scss']
})
export class UserAuthComponent {
  @Input() buttonShape: 'PILL' | 'SQUARE' = 'PILL';
  @Output() userAuthenticated = new EventEmitter<any>();

  authForm: FormGroup;
  isLoginMode = true;
  errorMessage: string | null = null;

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.authForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      username: [''] // koristi se samo za registraciju
    });
  }

  toggleMode(): void {
    this.isLoginMode = !this.isLoginMode;
    this.errorMessage = null;
  }

  submit(): void {
    if (this.authForm.invalid) return;

    const { email, password, username } = this.authForm.value;

    if (this.isLoginMode) {
      this.authService.login(email, password).subscribe({
        next: (user) => this.userAuthenticated.emit(user),
        error: (err) => {
          this.errorMessage = err.error?.message || 'Greška pri prijavi.';
        }
      });
    } else {
      this.authService.register({ email, password, username }).subscribe({
        next: (user) => this.userAuthenticated.emit(user),
        error: (err) => {
          this.errorMessage = err.error?.message || 'Greška pri registraciji.';
        }
      });
    }
  }
}
