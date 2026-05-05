import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatchService } from '../../core/services/match.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  matches: any[] = [];
  loading: boolean = true;
  errorMessage: string = '';

  constructor(
    private matchService: MatchService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.carregarMatches();
  }

  carregarMatches() {
    this.loading = true;
    this.errorMessage = '';

    const usuarioId = this.authService.getUsuarioId();

    console.log('=== CHAT COMPONENT ===');
    console.log('Usuário ID:', usuarioId);

    if (!usuarioId) {
      console.error('Usuário não autenticado');
      this.errorMessage = 'Usuário não autenticado. Faça login novamente.';
      this.loading = false;
      return;
    }

    this.matchService.getMeusMatches(usuarioId).subscribe({
      next: (data) => {
        console.log('Matches recebidos:', data);
        this.matches = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar matches:', error);
        this.errorMessage = 'Erro ao carregar conversas. Tente novamente.';
        this.loading = false;
      }
    });
  }

  getNomeAmigo(match: any): string {
    const usuarioId = this.authService.getUsuarioId();
    if (!usuarioId) return 'Carregando...';

    if (match.usuario1?.id === usuarioId) {
      return match.usuario2?.nome || 'Usuário';
    }
    return match.usuario1?.nome || 'Usuário';
  }

  getUltimaMensagem(match: any): string {
    if (match.ultimaMensagem?.conteudo) {
      const texto = match.ultimaMensagem.conteudo;
      return texto.length > 40 ? texto.substring(0, 40) + '...' : texto;
    }
    return 'Clique para começar a conversar';
  }

  getDataUltimaMensagem(match: any): string {
    if (match.ultimaMensagem?.dataEnvio) {
      return new Date(match.ultimaMensagem.dataEnvio).toLocaleDateString('pt-BR');
    }
    return '';
  }
}
