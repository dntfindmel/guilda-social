import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalheJogadorComponent } from './detalhe-jogador.component';

describe('DetalheJogadorComponent', () => {
  let component: DetalheJogadorComponent;
  let fixture: ComponentFixture<DetalheJogadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalheJogadorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalheJogadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
