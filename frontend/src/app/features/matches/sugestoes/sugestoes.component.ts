import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatchService } from '../../../core/services/match.service';
import { AuthService } from '../../../core/services/auth.service';
import { HeaderComponent } from '../../../layout/header/header.component';
import { Sugestao } from '../../../core/models/match.model';

@Component({
  selector: 'app-sugestoes',
  standalone: true,
  imports: [CommonModule, HeaderComponent],
  templateUrl: './sugestoes.component.html',
  styleUrls: ['./sugestoes.component.css']
})
export class SugestoesComponent implements OnInit {
  private matchService = inject(MatchService);
  private authService = inject(AuthService);
  private router = inject(Router);

  sugestoes: Sugestao[] = [];
  currentIndex = 0;
  loading = true;
  errorMessage = '';
  animating = false;

  niveis: Map<string, number> = new Map();
  distancias: Map<string, number> = new Map();
  idades: Map<string, number> = new Map();
  tagsList: Map<string, string[]> = new Map();

  ngOnInit(): void {
    this.carregarSugestoes();
  }

  carregarSugestoes(): void {
    this.loading = true;
    const usuarioId = this.authService.getUsuarioId();

    if (!usuarioId) {
      this.router.navigate(['/login']);
      return;
    }

    this.matchService.getSugestoes(usuarioId).subscribe({
      next: (data) => {
        this.sugestoes = data;
        this.currentIndex = 0;
        this.sugestoes.forEach(jogador => {
          this.niveis.set(jogador.id, Math.floor(Math.random() * 60) + 20);
          this.distancias.set(jogador.id, Math.floor(Math.random() * 10) + 1);
          this.idades.set(jogador.id, Math.floor(Math.random() * 10) + 20);
          this.tagsList.set(jogador.id, ['MMORPG', 'Voice Chat', 'Late Night']);
        });
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro:', error);
        this.errorMessage = 'Erro ao carregar sugestões';
        this.loading = false;
      }
    });
  }

  get currentCard(): Sugestao | null {
    if (this.sugestoes.length === 0 || this.currentIndex >= this.sugestoes.length) {
      return null;
    }
    return this.sugestoes[this.currentIndex];
  }

  getFotoUrl(jogador: Sugestao | null): string {
    if (!jogador || !jogador.fotoPerfil) return '';
    if (jogador.fotoPerfil.startsWith('http')) {
      return jogador.fotoPerfil;
    }
    return `http://localhost:8080${jogador.fotoPerfil}`;
  }

  getNivelAleatorio(jogadorId?: string): number {
    if (jogadorId && this.niveis.has(jogadorId)) {
      return this.niveis.get(jogadorId)!;
    }
    return Math.floor(Math.random() * 60) + 20;
  }

  getDistanciaAleatoria(jogadorId?: string): number {
    if (jogadorId && this.distancias.has(jogadorId)) {
      return this.distancias.get(jogadorId)!;
    }
    return Math.floor(Math.random() * 10) + 1;
  }

  getIdadeAleatoria(jogadorId?: string): number {
    if (jogadorId && this.idades.has(jogadorId)) {
      return this.idades.get(jogadorId)!;
    }
    return Math.floor(Math.random() * 10) + 20;
  }

  getTags(jogador: Sugestao | null): string[] {
    if (!jogador || !jogador.id) return ['Gamer'];
    if (this.tagsList.has(jogador.id)) {
      return this.tagsList.get(jogador.id)!;
    }
    return ['MMORPG', 'Voice Chat', 'Late Night'];
  }

  passar(): void {
    if (this.animating) return;
    this.animating = true;

    setTimeout(() => {
      this.currentIndex++;
      this.animating = false;
    }, 300);
  }

solicitarChat(): void {
  if (this.animating || !this.currentCard) return;
  this.animating = true;

  const usuarioId = this.authService.getUsuarioId();
  if (usuarioId && this.currentCard) {
    this.matchService.enviarSolicitacao(usuarioId, this.currentCard.id).subscribe({
      next: () => {
        alert('Solicitação enviada!');
        this.router.navigate(['/chat']);
        this.animating = false;
      },
      error: () => {
        alert('Erro ao enviar solicitação');
        this.animating = false;
      }
    });
  }
}

jaSolicitou(jogadorId: string): boolean {
    const solicitacoes = localStorage.getItem('solicitacoesEnviadas');
    if (solicitacoes) {
        const enviadas = JSON.parse(solicitacoes);
        return enviadas.includes(jogadorId);
    }
    return false;
}


  recarregar(): void {
    this.carregarSugestoes();
  }
}
