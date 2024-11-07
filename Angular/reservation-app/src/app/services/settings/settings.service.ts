import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs'; 
import { tap } from 'rxjs/operators';
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  private apiUrl = `${environment.apiUrl}/settings`;
  private imageApiUrl = `${environment.apiUrl}/images`; // For handling image uploads and deletions

  constructor(private http: HttpClient) {}

  // Get settings by company ID
  getSettings(companyId: number): Observable<any> {
    console.log('Pozivam getSettings za companyId:', companyId);
    return this.http.get(`${this.apiUrl}/company/${companyId}`).pipe(
      tap(response => console.log('Odgovor za getSettings:', response))
    );
  }

  // Update settings by company ID
  saveSettings(settings: any, companyId: number): Observable<any> {
    console.log('Pozivam saveSettings za companyId:', companyId, 'sa podacima:', settings);
    return this.http.put(`${this.apiUrl}/company/${companyId}`, settings).pipe(
      tap(response => console.log('Odgovor za saveSettings:', response))
    );
  }

  // Upload a single image (logo or background)
  uploadImage(formData: FormData): Observable<any> {
    console.log('Pozivam uploadImage sa formData:', formData);
    return this.http.post(`${this.imageApiUrl}/upload`, formData).pipe(
      tap(response => console.log('Odgovor za uploadImage:', response))
    );
  }

  // Upload multiple images
  uploadMultipleImages(formData: FormData): Observable<any> {
    console.log('Pozivam uploadMultipleImages sa formData:', formData);
    return this.http.post(`${this.imageApiUrl}/uploadMultiple`, formData).pipe(
      tap(response => console.log('Odgovor za uploadMultipleImages:', response))
    );
  }

  // Delete an image
  deleteImage(companyId: number, filename: string): Observable<any> {
    console.log('Pozivam deleteImage za companyId:', companyId, 'sa filename:', filename);
    return this.http.delete(`${this.imageApiUrl}/delete/${filename}?companyId=${companyId}`).pipe(
      tap(response => console.log('Odgovor za deleteImage:', response))
    );
  }

  // Get image count
  getImageCount(companyId: number): Observable<any> {
    console.log('Pozivam getImageCount za companyId:', companyId);
    return this.http.get(`${this.apiUrl}/company/${companyId}/imageCount`).pipe(
      tap(response => console.log('Odgovor za getImageCount:', response))
    );
  }

  // Get all images
  getImages(companyId: number): Observable<any> {
    console.log('Pozivam getImages za companyId:', companyId);
    return this.http.get(`${this.imageApiUrl}/company/${companyId}/images`).pipe(
      tap(response => console.log('Odgovor za getImages:', response))
    );
  }
}
