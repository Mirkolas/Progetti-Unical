import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnunciComponent } from './annunci.component';

describe('AnnunciComponent', () => {
  let component: AnnunciComponent;
  let fixture: ComponentFixture<AnnunciComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AnnunciComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnnunciComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
