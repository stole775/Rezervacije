import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SveTestComponent } from './sve-test.component';

describe('SveTestComponent', () => {
  let component: SveTestComponent;
  let fixture: ComponentFixture<SveTestComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SveTestComponent]
    });
    fixture = TestBed.createComponent(SveTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
