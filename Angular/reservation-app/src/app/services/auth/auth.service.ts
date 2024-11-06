// src/app/services/auth/auth.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router'; 
import { jwtDecode } from 'jwt-decode'; 
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<any>;
  public currentUser: Observable<any>;
   
  constructor(private http: HttpClient, private router: Router) {
    const storedUser = JSON.parse(localStorage.getItem('currentUser') || 'null');
    this.currentUserSubject = new BehaviorSubject<any>(storedUser);
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public getToken(): string | null {
    return localStorage.getItem('jwtToken');
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (token) {
      const decodedToken = this.decodeToken(token);
      return !this.isTokenExpired(decodedToken); // Vraća true ako je token validan i nije istekao
    }
    return false; // Ako token nije pronađen
  }

  isTokenExpired(token: any): boolean {
    if (!token || !token.exp) {
      return true; // Ako nema polja za isteka, smatra se da je token istekao
    }
    const currentTime = Math.floor(Date.now() / 1000); // Trenutno vreme u sekundama
    return token.exp < currentTime;
  }

  /**
   * Proverava da li korisnik ima ulogu SADMIN ili CADMIN
   */
  isUserAdmin(): boolean {
    const role = this.getUserRole();
    return role === 'SADMIN' || role === 'CADMIN';
  }

  public getUserRole(): string | null {
    const token = this.getToken();
    if (token) {
      try {
        const decoded: any = jwtDecode(token);
        return decoded.Role || null; // Vraća ulogu iz token-a
      } catch (error) {
        console.error('Failed to decode token:', error);
        return null;
      }
    }
    return null;
  }

  decodeToken(token: string): any {
    try {
      return jwtDecode(token); // Dekodira token
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }

  /**
   * Dohvata ID korisnika iz token-a
   */
  getUserId(): number | null {
    const token = this.getToken();
    if (token) {
      try {
        const decoded: any = jwtDecode(token);
        return decoded.ID_usera; // Izvlači ID korisnika
      } catch (error) {
        console.error('Failed to decode token');
      }
    }
    return null;
  }
  
  getCompanyId(): number | null {
    const token = this.getToken();
    if (token) {
      try {
        const decoded: any = jwtDecode(token);
        return decoded.ID_kompanije; // Izvlači ID kompnije
      } catch (error) {
        console.error('Failed to decode token');
      }
    }
    return null;
  }

  logout(): void {
    const token = this.getToken();
    if (token) {
      this.http.post(`${environment.apiUrl}/auth/logout`, {}, {
        headers: new HttpHeaders().set('Authorization', `Bearer ${token}`)
      }).subscribe({
        next: () => {
          console.log("Logged out successfully.");
          localStorage.removeItem('jwtToken');
          this.currentUserSubject.next(null);
          this.router.navigate(['']);
        },
        error: error => {
          console.error('Logout failed:', error);
          localStorage.removeItem('jwtToken');
          this.currentUserSubject.next(null);
          this.router.navigate(['']);
        }
      });
    } else {
      localStorage.removeItem('jwtToken');
      this.currentUserSubject.next(null);
      this.router.navigate(['']);
    }
  }

  login(username: string, password: string): Observable<{ jwt: string }> { 
    localStorage.removeItem('jwtToken'); // Očisti token pri pokušaju login-a
    return this.http.post<{ jwt: string }>(`${environment.apiUrl}/auth/login`, { username, password });
  }

  register(data: { username: string; email: string; password: string }): Observable<any> {
    return this.http.post(`${environment.apiUrl}/auth/register`, data);
  }

  handleAuthentication(jwt: string): void {
    console.log('Login successful:', jwt);
    localStorage.setItem('jwtToken', jwt);
    this.currentUserSubject.next(jwtDecode(jwt));
    this.router.navigate(['/']);
  }
}
