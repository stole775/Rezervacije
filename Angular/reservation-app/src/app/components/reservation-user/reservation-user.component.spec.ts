import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationUserComponent } from './reservation-user.component';

describe('ReservationUserComponent', () => {
  let component: ReservationUserComponent;
  let fixture: ComponentFixture<ReservationUserComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReservationUserComponent]
    });
    fixture = TestBed.createComponent(ReservationUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
