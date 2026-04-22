import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TerminiecondizioniComponent } from './terminiecondizioni.component';

describe('TerminiecondizioniComponent', () => {
  let component: TerminiecondizioniComponent;
  let fixture: ComponentFixture<TerminiecondizioniComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TerminiecondizioniComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TerminiecondizioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
