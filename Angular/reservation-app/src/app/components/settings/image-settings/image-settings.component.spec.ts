import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageSettingsComponent } from './image-settings.component';

describe('ImageSettingsComponent', () => {
  let component: ImageSettingsComponent;
  let fixture: ComponentFixture<ImageSettingsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImageSettingsComponent]
    });
    fixture = TestBed.createComponent(ImageSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
