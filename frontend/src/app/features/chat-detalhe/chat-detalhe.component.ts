import { Component, OnInit, OnDestroy, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatchService } from '../../core/services/match.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-chat-detalhe',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './chat-detalhe.component.html',
  styleUrls: ['./chat-detalhe.component.css']
})
export class ChatDetalheComponent implements OnInit, OnDestroy {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  matchId: string = '';
  match: any = null;
  mensagens: any[] = [];
  novaMensagem: string = '';
  remetenteId: string = '';
  destinatarioNome: string = 'Carregando...';
  destinatarioFoto: string = '';
  loading: boolean = true;
  enviando: boolean = false;
  private intervalId: any;
  private ultimoAvatarIndex: number = -1;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private matchService: MatchService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.matchId = id;
    }

    const usuarioId = this.authService.getUsuarioId();
    if (usuarioId) {
      this.remetenteId = usuarioId;
    }

    this.carregarMatch();
    this.carregarMensagens();

    this.intervalId = setInterval(() => {
      this.carregarMensagensSemScroll();
    }, 3000);
  }

  ngOnDestroy() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  irParaSugestoes(): void {
    this.router.navigate(['/sugestoes']);
  }

  carregarMatch() {
    const usuarioId = this.authService.getUsuarioId();
    if (usuarioId && this.matchId) {
      this.matchService.getMeusMatches(usuarioId).subscribe({
        next: (matches) => {
          const encontrado = matches.find(m => m.id === this.matchId);
          if (encontrado) {
            this.match = encontrado;
            if (encontrado.usuario1?.id === this.remetenteId) {
              this.destinatarioNome = encontrado.usuario2?.nome || 'Usuário';
              this.destinatarioFoto = encontrado.usuario2?.fotoPerfil || '';
            } else {
              this.destinatarioNome = encontrado.usuario1?.nome || 'Usuário';
              this.destinatarioFoto = encontrado.usuario1?.fotoPerfil || '';
            }
          }
        },
        error: (error) => {
          console.error('Erro ao carregar match:', error);
          this.destinatarioNome = 'Usuário';
        }
      });
    }
  }

  carregarMensagens() {
    if (!this.matchId) return;

    this.matchService.getMensagensChat(this.matchId).subscribe({
      next: (data) => {
        this.mensagens = data;
        this.scrollToBottom();
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar mensagens:', error);
        this.loading = false;
      }
    });
  }

  carregarMensagensSemScroll() {
    if (!this.matchId) return;

    this.matchService.getMensagensChat(this.matchId).subscribe({
      next: (data) => {
        const oldLength = this.mensagens.length;
        this.mensagens = data;
        if (this.mensagens.length !== oldLength) {
          this.scrollToBottom();
        }
      },
      error: (error) => console.error('Erro:', error)
    });
  }

  enviarMensagem() {
    if (!this.novaMensagem.trim() || !this.matchId || !this.remetenteId || this.enviando) return;

    this.enviando = true;
    const mensagemTexto = this.novaMensagem.trim();
    this.novaMensagem = '';

    this.matchService.enviarMensagem(this.matchId, this.remetenteId, mensagemTexto).subscribe({
      next: () => {
        this.enviando = false;
        this.carregarMensagens();
      },
      error: (error) => {
        console.error('Erro ao enviar:', error);
        this.enviando = false;
        this.novaMensagem = mensagemTexto;
        alert('Erro ao enviar mensagem. Tente novamente.');
      }
    });
  }

  scrollToBottom() {
    setTimeout(() => {
      if (this.messagesContainer) {
        this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
      }
    }, 100);
  }

formatarHorario(data: string): string {
  if (!data) return '';

  const date = new Date(data);

  if (isNaN(date.getTime())) return '';

  return date.toLocaleTimeString('pt-BR', {
    hour: '2-digit',
    minute: '2-digit',
    timeZone: 'America/Sao_Paulo'
  });
}

formatarData(data: string): string {
  if (!data) return '';

  const date = new Date(data);
  if (isNaN(date.getTime())) return '';

  return date.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    timeZone: 'America/Sao_Paulo'
  });
}

  isMensagemMinha(remetenteId: string): boolean {
    return remetenteId === this.remetenteId;
  }

  shouldShowAvatar(msg: any, index: number): boolean {
    if (index === 0 && !this.isMensagemMinha(msg.remetenteId)) return true;

    const mensagemAnterior = this.mensagens[index - 1];
    if (mensagemAnterior && this.isMensagemMinha(mensagemAnterior.remetenteId)) {
      return true;
    }

    return false;
  }

  getFotoUrl(fotoPerfil: string): string {
    if (!fotoPerfil) return '';
    if (fotoPerfil.startsWith('http')) {
      return fotoPerfil;
    }
    if (fotoPerfil.startsWith('/uploads/')) {
      return `http://localhost:8080${fotoPerfil}`;
    }
    return `http://localhost:8080/uploads/${fotoPerfil}`;
  }
}
