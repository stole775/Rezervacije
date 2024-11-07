// src/app/components/settings/image-settings/image-settings.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Settings } from '../Settings';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-image-settings',
  templateUrl: './image-settings.component.html',
  styleUrls: ['./image-settings.component.css']
})
export class ImageSettingsComponent {
  @Input() settings!: Settings;
  @Input() images: string[] = [];
  @Input() imageCount: number = 0;
  @Input() maxImagesAllowed: number = 10;
  @Input() previewImages: string[] = [];

  @Output() uploadLogo = new EventEmitter<void>();
  @Output() uploadBackground = new EventEmitter<void>();
  @Output() uploadMultipleImages = new EventEmitter<void>();
  @Output() deleteImage = new EventEmitter<string>();
  @Output() onLogoSelected = new EventEmitter<Event>();
  @Output() onBackgroundSelected = new EventEmitter<Event>();
  @Output() onMultipleImagesSelected = new EventEmitter<Event>();

  @Output() imageAction = new EventEmitter<{ type: string; message: string }>();

  constructor(private sanitizer: DomSanitizer) {}

  safeUrl(url: string): SafeUrl {
    console.log(url)
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  // Upload Logo Success
  emitUploadLogoSuccess(): void {
    this.imageAction.emit({ type: 'success', message: 'Logo uploaded successfully.' });
  }

  // Upload Logo Error
  emitUploadLogoError(): void {
    this.imageAction.emit({ type: 'error', message: 'Error uploading logo.' });
  }

  // Upload Background Success
  emitUploadBackgroundSuccess(): void {
    this.imageAction.emit({ type: 'success', message: 'Background image uploaded successfully.' });
  }

  // Upload Background Error
  emitUploadBackgroundError(): void {
    this.imageAction.emit({ type: 'error', message: 'Error uploading background image.' });
  }

  // Upload Multiple Images Success
  emitUploadMultipleImagesSuccess(): void {
    this.imageAction.emit({ type: 'success', message: 'Images uploaded successfully.' });
  }

  // Upload Multiple Images Error
  emitUploadMultipleImagesError(): void {
    this.imageAction.emit({ type: 'error', message: 'Error uploading images.' });
  }

  // Delete Image Success
  emitDeleteImageSuccess(imageUrl: string): void {
    this.imageAction.emit({ type: 'success', message: `Image ${imageUrl} deleted successfully.` });
  }

  // Delete Image Error
  emitDeleteImageError(imageUrl: string): void {
    this.imageAction.emit({ type: 'error', message: `Error deleting image ${imageUrl}.` });
  }
}
