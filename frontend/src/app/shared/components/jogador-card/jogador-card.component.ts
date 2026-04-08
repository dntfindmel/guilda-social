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
  @Output() onConectar = new EventEmitter<void>();
  @Output() onVerPerfil = new EventEmitter<void>();

  getInitials(): string {
    return this.jogador.nome
      .split(' ')
      .map(n => n[0])
      .join('')
      .substring(0, 2)
      .toUpperCase();
  }

  getAfinidadeClass(): string {
    const afinidade = this.jogador.nivelAfinidade;
    if (afinidade >= 80) return 'alta';
    if (afinidade >= 50) return 'media';
    return 'baixa';
  }
}
