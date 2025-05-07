 
import { Component, OnInit } from '@angular/core';
import { Settings } from 'src/app/components/settings/Settings';
import { SettingsService } from 'src/app/services/settings/settings.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-reservation-user',
  templateUrl: './reservation-user.component.html',
  styleUrls: ['./reservation-user.component.css']
})
export class ReservationUserComponent implements OnInit {
  settings!: Settings;
  companyId: number = 1;
  step: 'worker' | 'service' | 'date' | 'auth' | 'done' = 'worker';

  selectedWorker: any;
  selectedService: any;
  selectedDateTime: any;
  isAuthenticated: boolean = false;
  buttonShape: 'PILL' | 'SQUARE' = 'PILL';


constructor(
  private route: ActivatedRoute,
  private settingsService: SettingsService
) {}

ngOnInit(): void {
  this.route.paramMap.subscribe(params => {
    const companyName = params.get('companyName');
    if (companyName) {
      console.log(companyName + " companyName");
      
      this.loadCompanySettings(companyName);
    }
  });
}

loadCompanySettings(companyName: string) {
  this.settingsService.getSettingsByCompanyName(companyName).subscribe({
    next: (data) => {
      this.settings = data;
      this.buttonShape = data.buttonShape === 'SQUARE' ? 'SQUARE' : 'PILL';

      if (data.companyId) {
        this.companyId = data.companyId; // DODATO OVDE
      } else {
        console.error('Nije pronađen companyId u settings');
      }
    },
    error: (err) => console.error('Greška pri učitavanju podešavanja:', err)
  });
}


  onWorkerSelected(worker: any) {
    this.selectedWorker = worker;
    this.step = 'service';
  }

  onServiceSelected(service: any) {
    this.selectedService = service;
    this.step = 'date';
  }

  onDateSelected(dateTime: any) {
    this.selectedDateTime = dateTime;
    this.step = 'auth';
  }

  onUserAuthenticated(user: any) {
    this.isAuthenticated = true;
    this.step = 'done';
    // Poziv za backend rezervaciju bi išao ovde
  }
}
