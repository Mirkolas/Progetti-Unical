import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContattavenditoreComponent } from './contattavenditore.component';

describe('ContattavenditoreComponent', () => {
  let component: ContattavenditoreComponent;
  let fixture: ComponentFixture<ContattavenditoreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ContattavenditoreComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContattavenditoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
