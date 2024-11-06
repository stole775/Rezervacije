// src/app/components/settings/page-settings/page-settings.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Settings } from '../Settings';
 

@Component({
  selector: 'app-page-settings',
  templateUrl: './page-settings.component.html',
  styleUrls: ['./page-settings.component.css']
})
export class PageSettingsComponent {
  @Input() settings!: Settings;
  @Output() settingsChange = new EventEmitter<Settings>();

  onSettingsChange() {
    this.settingsChange.emit(this.settings);
  }
}
