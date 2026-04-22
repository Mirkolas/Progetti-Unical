import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsteComponent } from './aste.component';

describe('AsteComponent', () => {
  let component: AsteComponent;
  let fixture: ComponentFixture<AsteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AsteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
