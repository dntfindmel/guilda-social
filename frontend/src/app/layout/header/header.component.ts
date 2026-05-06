import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { MatchService } from '../../core/services/match.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private matchService = inject(MatchService);

  mensagensNaoLidas: number = 0;

  constructor() {
    this.carregarMensagensNaoLidas();
  }

  carregarMensagensNaoLidas() {
    const usuarioId = this.authService.getUsuarioId();
    if (usuarioId) {
      this.matchService.getConversasNaoLidas(usuarioId).subscribe({
        next: (count) => this.mensagensNaoLidas = count,
        error: () => this.mensagensNaoLidas = 0
      });
    }
  }

  getUsuarioNome(): string {
    return localStorage.getItem('usuarioNome') || 'Jogador';
  }

  irParaPerfil(): void {
    this.router.navigate(['/perfil']);
  }

  irParaChat(): void {
    this.router.navigate(['/chat']);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
