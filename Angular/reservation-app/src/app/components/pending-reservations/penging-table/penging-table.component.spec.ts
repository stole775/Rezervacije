import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PengingTableComponent } from './penging-table.component';

describe('PengingTableComponent', () => {
  let component: PengingTableComponent;
  let fixture: ComponentFixture<PengingTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PengingTableComponent]
    });
    fixture = TestBed.createComponent(PengingTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
