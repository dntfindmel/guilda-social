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
        console.log('Sugestões recebidas:', data);
        this.sugestoes = data;
        this.currentIndex = 0;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar sugestões:', error);
        this.errorMessage = 'Erro ao carregar sugestões. Tente novamente.';
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
    if (!jogador?.fotoPerfil) return '';
    if (jogador.fotoPerfil.startsWith('http')) {
      return jogador.fotoPerfil;
    }
    return `http://localhost:8080${jogador.fotoPerfil}`;
  }

  passar(): void {
    if (this.animating || !this.currentCard) return;
    this.animating = true;

    const usuarioId = this.authService.getUsuarioId();
    if (!usuarioId) {
      this.router.navigate(['/login']);
      return;
    }

    const cardPassado = this.currentCard;
    console.log('Passando sugestão:', cardPassado.id);

    this.matchService.passarSugestao(usuarioId, cardPassado.id).subscribe({
      next: () => {
        console.log('Sugestão passada com sucesso');
        // Remover o card da lista
        this.sugestoes = this.sugestoes.filter(s => s.id !== cardPassado.id);
        this.currentIndex = 0;
        this.animating = false;

        if (this.sugestoes.length === 0) {
          console.log('Não há mais sugestões');
        }
      },
      error: (error) => {
        console.error('Erro ao passar sugestão:', error);
        this.animating = false;
        alert('Erro ao passar sugestão. Tente novamente.');
      }
    });
  }

  solicitarChat(): void {
    if (this.animating || !this.currentCard) return;
    this.animating = true;

    const usuarioId = this.authService.getUsuarioId();
    if (!usuarioId) {
      this.router.navigate(['/login']);
      return;
    }

    const card = this.currentCard;

    this.matchService.enviarSolicitacao(usuarioId, card.id).subscribe({
      next: () => {
        alert('Solicitação enviada!');
        this.sugestoes = this.sugestoes.filter(s => s.id !== card.id);
        this.currentIndex = 0;
        this.animating = false;
      },
      error: (error) => {
        console.error('Erro ao enviar solicitação:', error);
        let mensagem = 'Erro ao enviar solicitação. Tente novamente.';
        if (error.error?.message) {
          mensagem = error.error.message;
        }
        alert(mensagem);
        this.animating = false;
      }
    });
  }

  recarregar(): void {
    console.log('Recarregando sugestões...');
    this.carregarSugestoes();
  }
}
