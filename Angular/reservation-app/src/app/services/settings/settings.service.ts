import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs'; 
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
    return this.http.get(`${this.apiUrl}/company/${companyId}`);
  }

  // Update settings by company ID
  saveSettings(settings: any, companyId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/company/${companyId}`, settings);
  }

  // Upload a single image (logo or background)
  uploadImage(formData: FormData): Observable<any> {
    return this.http.post(`${this.imageApiUrl}/upload`, formData);
  }

  // Upload multiple images
 // In settings.service.ts
 uploadMultipleImages(formData: FormData): Observable<any> {
  return this.http.post(`${this.imageApiUrl}/uploadMultiple`, formData);
}



  // Delete an image
  deleteImage(companyId: number, filename: string): Observable<any> {
    return this.http.delete(`${this.imageApiUrl}/delete/${filename}?companyId=${companyId}`);
  }

  // Get image count
  getImageCount(companyId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/company/${companyId}/imageCount`);
  }

  // Get all images
  getImages(companyId: number): Observable<any> {
    return this.http.get(`${this.imageApiUrl}/company/${companyId}/images`);
  }
}
