import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificaDettaglioComponent } from './modifica-dettaglio.component';

describe('ModificaDettaglioComponent', () => {
  let component: ModificaDettaglioComponent;
  let fixture: ComponentFixture<ModificaDettaglioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModificaDettaglioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModificaDettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
