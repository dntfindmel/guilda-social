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
          this.currentIndex++;
          this.animating = false;
        },
        error: () => {
          alert('Erro ao enviar solicitação');
          this.animating = false;
        }
      });
    }
  }

  recarregar(): void {
    this.carregarSugestoes();
  }

  getNivelAleatorio(): number {
    return Math.floor(Math.random() * 60) + 20;
  }

  getTags(jogador: Sugestao): string[] {
    const tags = ['MMORPG', 'Voice Chat', 'Late Night'];
    if (jogador.estiloJogo) {
      tags.push(jogador.estiloJogo);
    }
    return tags.slice(0, 4);
  }

  getDistancia(): number {
    return Math.floor(Math.random() * 10) + 1;
  }
}
