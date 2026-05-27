import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
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
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.carregarMatches();
  }

  irParaSugestoes(): void {
    this.router.navigate(['/sugestoes']);
  }

  carregarMatches() {
    this.loading = true;
    const usuarioId = this.authService.getUsuarioId();

    if (!usuarioId) {
      this.errorMessage = 'Usuário não autenticado';
      this.loading = false;
      return;
    }

    this.matchService.getMeusMatches(usuarioId).subscribe({
      next: (data) => {
        this.matches = data.sort((a, b) => {
          const dataA = a.ultimaMensagem?.dataEnvio ? new Date(a.ultimaMensagem.dataEnvio) : new Date(0);
          const dataB = b.ultimaMensagem?.dataEnvio ? new Date(b.ultimaMensagem.dataEnvio) : new Date(0);
          return dataB.getTime() - dataA.getTime();
        });
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar matches:', error);
        this.errorMessage = 'Erro ao carregar conversas';
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

  getFotoAmigo(match: any): string {
    const usuarioId = this.authService.getUsuarioId();
    if (!usuarioId) return '';

    let foto = '';
    if (match.usuario1?.id === usuarioId) {
      foto = match.usuario2?.fotoPerfil || '';
    } else {
      foto = match.usuario1?.fotoPerfil || '';
    }

    if (foto && !foto.startsWith('http')) {
      return `http://localhost:8080${foto}`;
    }
    return foto;
  }

  getUltimaMensagem(match: any): string {
    const mensagem = match.ultimaMensagem?.conteudo;
    if (mensagem) {
      return mensagem.length > 40 ? mensagem.substring(0, 40) + '...' : mensagem;
    }
    return 'Nenhuma mensagem ainda';
  }
  getStatusChat(match: any): { texto: string; classe: string; destaque: boolean } {
    const usuarioId = this.authService.getUsuarioId();

    if (!match.ultimaMensagem) {
      return { texto: '✨ Novo match!', classe: 'new', destaque: false };
    }

    if (match.ultimaMensagem.remetenteId !== usuarioId) {
      return { texto: '⏳ Aguardando resposta', classe: 'waiting', destaque: true };
    }

    return { texto: '💬 Conversa ativa', classe: 'active', destaque: false };
  }

getDataUltimaMensagem(match: any): string {
  if (match.ultimaMensagem?.dataEnvio) {
    const data = new Date(match.ultimaMensagem.dataEnvio);
    const hoje = new Date();

    const dataLocal = new Date(data.toLocaleString('en-US', { timeZone: 'America/Sao_Paulo' }));
    const hojeLocal = new Date(hoje.toLocaleString('en-US', { timeZone: 'America/Sao_Paulo' }));

    if (dataLocal.toDateString() === hojeLocal.toDateString()) {
      return dataLocal.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    }
    return dataLocal.toLocaleDateString('pt-BR');
  }
  return '';
}
}
