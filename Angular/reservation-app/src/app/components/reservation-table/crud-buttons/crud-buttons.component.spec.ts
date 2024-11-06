import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudButtonsComponent } from './crud-buttons.component';

describe('CrudButtonsComponent', () => {
  let component: CrudButtonsComponent;
  let fixture: ComponentFixture<CrudButtonsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CrudButtonsComponent]
    });
    fixture = TestBed.createComponent(CrudButtonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
