import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JogadorCardComponent } from './jogador-card.component';

describe('JogadorCardComponent', () => {
  let component: JogadorCardComponent;
  let fixture: ComponentFixture<JogadorCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JogadorCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JogadorCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
