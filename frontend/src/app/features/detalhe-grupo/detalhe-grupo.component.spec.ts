import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalheGrupoComponent } from './detalhe-grupo.component';

describe('DetalheGrupoComponent', () => {
  let component: DetalheGrupoComponent;
  let fixture: ComponentFixture<DetalheGrupoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalheGrupoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalheGrupoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
