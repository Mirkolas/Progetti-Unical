import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsteDettaglioComponent } from './aste-dettaglio.component';

describe('AsteDettaglioComponent', () => {
  let component: AsteDettaglioComponent;
  let fixture: ComponentFixture<AsteDettaglioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AsteDettaglioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsteDettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
