// src/app/components/settings/contact-info/contact-info.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Settings } from '../Settings';
 

@Component({
  selector: 'app-contact-info',
  templateUrl: './contact-info.component.html',
  styleUrls: ['./contact-info.component.css']
})
export class ContactInfoComponent {
  @Input() settings!: Settings;
  @Input() timezones!: { value: string; displayName: string }[];
  @Output() settingsChange = new EventEmitter<Settings>();

  onSettingsChange() {
    this.settingsChange.emit(this.settings);
  }
}
