import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingReservationsComponent } from './pending-reservations.component';

describe('PendingReservationsComponent', () => {
  let component: PendingReservationsComponent;
  let fixture: ComponentFixture<PendingReservationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PendingReservationsComponent]
    });
    fixture = TestBed.createComponent(PendingReservationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
