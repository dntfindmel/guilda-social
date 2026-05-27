import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-grupo-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="grupo-card">
      <div class="card-header">
        <h3>{{ grupo.nome }}</h3>
        <span class="vagas" [class.cheio]="grupo.vagasPreenchidas === grupo.vagas">
          {{ grupo.vagas - grupo.vagasPreenchidas }} vagas
        </span>
      </div>
      <div class="card-body">
        <p class="descricao">{{ grupo.descricao }}</p>
        <div class="info">
          <span>🎮 {{ grupo.jogo?.nome || 'Jogo não definido' }}</span>
          <span>👤 Líder: {{ grupo.lider?.nome }}</span>
          <span *ngIf="grupo.dataEvento">📅 {{ grupo.dataEvento | date:'dd/MM/yyyy HH:mm' }}</span>
          <span *ngIf="grupo.local">📍 {{ grupo.local }}</span>
        </div>
      </div>
      <div class="card-actions">
        <button class="btn-entrar" (click)="onEntrar.emit()" *ngIf="!isMembro && !isLider">
          Entrar no grupo
        </button>
        <button class="btn-sair" (click)="onSair.emit()" *ngIf="isMembro && !isLider">
          Sair do grupo
        </button>
        <button class="btn-chat" (click)="onAbrirChat.emit()" *ngIf="isMembro || isLider">
          💬 Chat do grupo
        </button>
      </div>
    </div>
  `,
  styles: [`
    .grupo-card {
      background: white;
      border-radius: 16px;
      padding: 16px;
      margin-bottom: 16px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
    }
    .card-header h3 {
      margin: 0;
      color: #221E10;
    }
    .vagas {
      background: #E8F5E9;
      color: #2E7D32;
      padding: 4px 8px;
      border-radius: 12px;
      font-size: 0.75rem;
    }
    .vagas.cheio {
      background: #FFEBEE;
      color: #C62828;
    }
    .descricao {
      color: #64748B;
      font-size: 0.85rem;
      margin-bottom: 12px;
    }
    .info {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
      font-size: 0.8rem;
      color: #94A3B8;
      margin-bottom: 16px;
    }
    .card-actions {
      display: flex;
      gap: 12px;
    }
    button {
      padding: 8px 16px;
      border-radius: 20px;
      border: none;
      cursor: pointer;
      font-weight: 500;
    }
    .btn-entrar {
      background: #F4C025;
      color: #221E10;
    }
    .btn-sair {
      background: #F1F5F9;
      color: #64748B;
    }
    .btn-chat {
      background: #E8F0FE;
      color: #3f51b5;
    }
  `]
})
export class GrupoCardComponent {
  @Input() grupo!: any;
  @Input() isMembro: boolean = false;
  @Input() isLider: boolean = false;
  @Output() onEntrar = new EventEmitter();
  @Output() onSair = new EventEmitter();
  @Output() onAbrirChat = new EventEmitter();
}
