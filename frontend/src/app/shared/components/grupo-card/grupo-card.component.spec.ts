import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrupoCardComponent } from './grupo-card.component';

describe('GrupoCardComponent', () => {
  let component: GrupoCardComponent;
  let fixture: ComponentFixture<GrupoCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GrupoCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GrupoCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
