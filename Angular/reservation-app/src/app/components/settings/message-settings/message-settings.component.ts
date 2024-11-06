// message-settings.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core'; 
import { Settings } from '../Settings';

@Component({
  selector: 'app-message-settings', // Use 'app-' prefix for clarity
  templateUrl: './message-settings.component.html',
  styleUrls: ['./message-settings.component.css']
})
export class MessageSettingsComponent {
  @Input() settings!: Settings;
  @Output() settingsChange = new EventEmitter<Settings>();

  onSettingsChange() {
    this.settingsChange.emit(this.settings);
  }
}
