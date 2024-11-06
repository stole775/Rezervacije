// src/app/components/settings/settings2/settings2.component.ts
import { Component, OnInit } from '@angular/core';
import { SettingsService } from 'src/app/services/settings/settings.service';
import { AuthService } from 'src/app/services/auth/auth.service'; 
import { CompanyService } from 'src/app/services/company/company.service'; 
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';  
import { Settings } from '../Settings';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  settings: Settings = {
    numberOfMessages: 1,
    hoursBeforeFirstMsg: 24,
    hoursBeforeSecondMsg: 0,
    daysToKeep: 7,
    messageTemplate: 'Dear {name}, your appointment is scheduled for {appointment_time}.',
    imageUrlLogo: "null",
    imageUrlBackground: "null",
    cenovnik: false,
    buttonShape: 'PILL',
    theme: 'LIGHT',
    email: '',
    phone: '',
    address: '',
    city: '',
    zip: '',
    timezone: '',
    prikaziCene: false
  };

  timezones = [
    { value: 'UTC_MINUS_12', displayName: 'UTC-12:00 (Baker Island)' },
    { value: 'UTC_MINUS_11', displayName: 'UTC-11:00 (American Samoa)' },
    { value: 'UTC_MINUS_10', displayName: 'UTC-10:00 (Hawaii)' },
    { value: 'UTC_MINUS_9', displayName: 'UTC-09:00 (Alaska)' },
    { value: 'UTC_MINUS_8', displayName: 'UTC-08:00 (Pacific Time US & Canada)' },
    { value: 'UTC_MINUS_7', displayName: 'UTC-07:00 (Mountain Time US & Canada)' },
    { value: 'UTC_MINUS_6', displayName: 'UTC-06:00 (Central Time US & Canada)' },
    { value: 'UTC_MINUS_5', displayName: 'UTC-05:00 (Eastern Time US & Canada)' },
    { value: 'UTC_PLUS_0', displayName: 'UTC+00:00 (London)' },
    { value: 'UTC_PLUS_1', displayName: 'UTC+01:00 (Paris)' },
    { value: 'UTC_PLUS_2', displayName: 'UTC+02:00 (Athens)' },
    { value: 'UTC_PLUS_3', displayName: 'UTC+03:00 (Moscow)' },
    { value: 'UTC_PLUS_4', displayName: 'UTC+04:00 (Dubai)' },
    { value: 'UTC_PLUS_5', displayName: 'UTC+05:00 (Karachi)' },
    { value: 'UTC_PLUS_6', displayName: 'UTC+06:00 (Dhaka)' },
    { value: 'UTC_PLUS_7', displayName: 'UTC+07:00 (Bangkok)' },
    { value: 'UTC_PLUS_8', displayName: 'UTC+08:00 (Hong Kong)' },
    { value: 'UTC_PLUS_9', displayName: 'UTC+09:00 (Tokyo)' },
    { value: 'UTC_PLUS_10', displayName: 'UTC+10:00 (Sydney)' },
    { value: 'UTC_PLUS_11', displayName: 'UTC+11:00 (Solomon Islands)' },
    { value: 'UTC_PLUS_12', displayName: 'UTC+12:00 (Fiji)' }
  ];

  logoFile: File | null = null;
  backgroundFile: File | null = null;
  newImageFiles: File[] = [];
  previewImages: string[] = [];

  message: string | null = null;
  userId: number | null = null;
  userRole: string = '';
  companies: any[] = [];
  selectedCompanyId: number | null = null;
  images: string[] = [];
  imageCount: number = 0;
  maxImagesAllowed: number = 10;
  toastMessage: string | null = null;

  constructor(
    private settingsService: SettingsService, 
    private authService: AuthService, 
    private companyService: CompanyService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
      this.router.navigate(['/login']);
    } else {
      this.userId = this.authService.getUserId(); 
      this.userRole = this.authService.getUserRole() || 'Null';

      if (this.userRole === 'CUSTOMER') {
        this.message = 'You do not have access to this page.';
        this.router.navigate(['/reservations']);
      } else if (this.userRole === 'SADMIN') {
        this.loadCompanies();
      } else if (this.userRole === 'CADMIN') {
        this.selectedCompanyId = this.authService.getCompanyId();
        if (this.selectedCompanyId) {
          this.loadSettings(this.selectedCompanyId);
        }
      }
    }
  }

  showToast(message: string): void {
    this.toastMessage = message;
    setTimeout(() => {
      this.toastMessage = null;
    }, 2000); // Toast will disappear after 2 seconds
  }

  hideToast(): void {
    this.toastMessage = null;
  }

  loadCompanies(): void {
    this.companyService.getAllCompanies().subscribe({
      next: (data) => {
        this.companies = data;
      },
      error: () => {
        this.message = 'Error loading companies.';
      }
    });
  }

  onCompanyChange(): void {
    if (this.selectedCompanyId) {
      this.loadSettings(this.selectedCompanyId);
    }
  }

  loadSettings(companyId: number): void { 
    this.settingsService.getSettings(companyId).subscribe({
      next: (data) => {
        if (data) {
          this.settings = data;
          this.loadImageCount(companyId); 
          this.loadImages(companyId);
          this.message = null;
        }
      },
      error: (error) => {
        if (error.status === 404) {
          this.message = 'No settings found for this company.';
        } else if (error.status === 500) {
          this.message = 'Server error.';
        } else {
          this.message = 'An error occurred.';
        }
      }
    });
  }

  loadImageCount(companyId: number): void {
    this.settingsService.getImageCount(companyId).subscribe({
      next: (count) => {
        this.imageCount = count;
      },
      error: () => {
        this.message = 'Error loading image count.';
      }
    });
  }

  loadImages(companyId: number): void {
    this.settingsService.getImages(companyId).subscribe({
      next: (data) => {
        this.settings.imageUrlLogo = data.logo || null;
        this.settings.imageUrlBackground = data.background || null;

        this.images = data.additionalImages || [];
      },
      error: () => {
        this.message = 'Error loading images.';
      }
    });
  }

  onLogoSelected(event: any): void {
    this.logoFile = event.target.files[0];
  }

  onBackgroundSelected(event: any): void {
    this.backgroundFile = event.target.files[0];
  }

  onMultipleImagesSelected(event: any): void {
    this.newImageFiles = Array.from(event.target.files);

    // Create image previews
    this.previewImages = [];
    for (let file of this.newImageFiles) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.previewImages.push(e.target.result);
      };
      reader.readAsDataURL(file);
    }
  }

  uploadLogo(): void {
    if (this.logoFile && this.selectedCompanyId) {
      const formData = new FormData();
      formData.append('file', this.logoFile);
      formData.append('companyId', this.selectedCompanyId.toString());
      formData.append('columnName', 'logo');
      this.settingsService.uploadImage(formData).subscribe({
        next: () => {
          this.showToast('Logo uploaded successfully.');
          this.logoFile = null;
          if (this.selectedCompanyId)
            this.loadImages(this.selectedCompanyId);
        },
        error: () => {
          this.showToast('Error uploading logo.');
        }
      });
    }
  }

  uploadBackground(): void {
    if (this.backgroundFile && this.selectedCompanyId) {
      const formData = new FormData();
      formData.append('file', this.backgroundFile);
      formData.append('companyId', this.selectedCompanyId.toString());
      formData.append('columnName', 'background');
      this.settingsService.uploadImage(formData).subscribe({
        next: () => {
          this.message = 'Background image uploaded successfully.';
          this.showToast('Background image uploaded successfully.');
          this.backgroundFile = null;
          if(this.selectedCompanyId)
            this.loadImages(this.selectedCompanyId);
        },
        error: () => {
          this.showToast('Error uploading background image.');
          this.message = 'Error uploading background image.';
        }
      });
    }
  }

  uploadMultipleImages(): void {
    if (this.newImageFiles.length > 0 && this.selectedCompanyId) {
      const totalImagesAfterUpload = this.imageCount + this.newImageFiles.length;
      if (totalImagesAfterUpload > this.maxImagesAllowed) {
        this.message = `You cannot add more than ${this.maxImagesAllowed} images.`;
        return;
      }
  
      const formData = new FormData();
      // Use 'files' as the key to match the backend
      this.newImageFiles.forEach(file => {
        formData.append('files', file);
      });
      formData.append('companyId', this.selectedCompanyId.toString());
  
      this.settingsService.uploadMultipleImages(formData).subscribe({
        next: () => {
          this.message = 'Images uploaded successfully.';
          this.showToast('Images uploaded successfully.');
          this.newImageFiles = [];
          this.previewImages = [];
          if (this.selectedCompanyId) {
            this.loadImages(this.selectedCompanyId);
            this.loadImageCount(this.selectedCompanyId);
          }
        },
        error: (error) => {
          console.error('Upload error:', error);
          this.showToast('Error uploading images.');
          this.message = error.error.error || 'Error uploading images.';
        }
      });
    } else {
      this.message = 'Please select images to upload.';
    }
  }


  deleteImage(imageUrl: string): void {
    if (this.selectedCompanyId && imageUrl) {
      const filename = imageUrl.split('/').pop() || '';

      if (filename) {
        this.settingsService.deleteImage(this.selectedCompanyId, filename).subscribe({
          next: () => {
            this.message = 'Image deleted successfully.';
            if (imageUrl === this.settings.imageUrlLogo) {
              this.settings.imageUrlLogo = "";
            } else if (imageUrl === this.settings.imageUrlBackground) {
              this.settings.imageUrlBackground = "";
            } else {
              this.images = this.images.filter(img => img !== imageUrl);
              if(this.selectedCompanyId)
                this.loadImageCount(this.selectedCompanyId);
            }
          },
          error: () => {
            this.message = 'Error deleting image.';
          }
        });
      }
    }
  }

  onSettingsChange(updatedSettings: Settings): void {
    this.settings = { ...this.settings, ...updatedSettings };
  }

  onSubmit(settingsForm: NgForm): void {
    if (settingsForm.valid) {
      if (this.settings.numberOfMessages === 1) {
        this.settings.hoursBeforeSecondMsg = 0;
      }

      let companyId: number | null = null;
      if (this.userRole === 'SADMIN') {
        companyId = this.selectedCompanyId;
      } else if (this.userRole === 'CADMIN') {
        companyId = this.authService.getCompanyId();
      }

      if (companyId !== null) {
        this.settingsService.saveSettings(this.settings, companyId).subscribe({
          next: () => {
            this.showToast('Settings saved successfully!');
          },
          error: () => {
            this.showToast('Error saving settings.');
          }
        });
      }
    }
  }

 



}
