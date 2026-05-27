// frontend/src/app/shared/components/jogador-card/jogador-card.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Sugestao } from '../../../core/models/match.model';

@Component({
  selector: 'app-jogador-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './jogador-card.component.html',
  styleUrls: ['./jogador-card.component.css']
})
export class JogadorCardComponent {
  @Input() jogador!: Sugestao;
  @Input() nivelAfinidade: number = 0;
  @Output() onConectar = new EventEmitter<void>();
  @Output() onVerPerfil = new EventEmitter<void>();

  getInitials(): string {
    if (!this.jogador?.nome) return 'U';
    return this.jogador.nome
      .split(' ')
      .map(n => n[0])
      .join('')
      .substring(0, 2)
      .toUpperCase();
  }

  getFotoUrl(): string {
    if (!this.jogador?.fotoPerfil) return '';
    if (this.jogador.fotoPerfil.startsWith('http')) {
      return this.jogador.fotoPerfil;
    }
    return `http://localhost:8080${this.jogador.fotoPerfil}`;
  }

  getAfinidadeClass(): string {
    const afinidade = this.nivelAfinidade;
    if (afinidade >= 80) return 'alta';
    if (afinidade >= 50) return 'media';
    return 'baixa';
  }
}
