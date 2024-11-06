// settings.model.ts
export class Settings {
    numberOfMessages: number = 1;
    hoursBeforeFirstMsg: number = 0;
    hoursBeforeSecondMsg?: number;
    daysToKeep: number = 0;
    messageTemplate: string = '';
    prikaziCene: boolean = false;
    cenovnik: boolean = false;
    email: string = '';
    phone: string = '';
    address: string = '';
    city: string = '';
    zip: string = '';
    timezone: string = '';
    buttonShape: string = 'PILL';
    theme: string = 'LIGHT';
    imageUrlLogo?: string;
    imageUrlBackground?: string;
    // ... other properties
  }
   
 
 
  
  