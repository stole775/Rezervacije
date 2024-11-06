import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditReservationDialogComponent } from './edit-reservation-dialog.component';

describe('EditReservationDialogComponent', () => {
  let component: EditReservationDialogComponent;
  let fixture: ComponentFixture<EditReservationDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditReservationDialogComponent]
    });
    fixture = TestBed.createComponent(EditReservationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
