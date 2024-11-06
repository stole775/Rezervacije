import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageSettingsComponent } from './message-settings.component';

describe('MessageSettingsComponent', () => {
  let component: MessageSettingsComponent;
  let fixture: ComponentFixture<MessageSettingsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MessageSettingsComponent]
    });
    fixture = TestBed.createComponent(MessageSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
