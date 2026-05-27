import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatDetalheComponent } from './chat-detalhe.component';

describe('ChatDetalheComponent', () => {
  let component: ChatDetalheComponent;
  let fixture: ComponentFixture<ChatDetalheComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatDetalheComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatDetalheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
