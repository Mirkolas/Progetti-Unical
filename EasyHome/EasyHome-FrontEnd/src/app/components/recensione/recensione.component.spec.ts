import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecensioneComponent } from './recensione.component';

describe('RecensioneComponent', () => {
  let component: RecensioneComponent;
  let fixture: ComponentFixture<RecensioneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RecensioneComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecensioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
