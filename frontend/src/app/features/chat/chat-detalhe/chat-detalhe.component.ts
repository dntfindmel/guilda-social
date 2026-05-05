import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatchService } from '../../../core/services/match.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-chat-detalhe',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './chat-detalhe.component.html',
  styleUrls: ['./chat-detalhe.component.css']
})
export class ChatDetalheComponent implements OnInit {
  matchId: string = '';
  mensagens: any[] = [];
  novaMensagem: string = '';
  remetenteId: string = '';

  constructor(
    private route: ActivatedRoute,
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

    this.carregarMensagens();
  }

  carregarMensagens() {
    if (!this.matchId) return;
    this.matchService.getMensagensChat(this.matchId).subscribe((data: any[]) => {
      this.mensagens = data;
    });
  }

  enviar() {
    if (!this.novaMensagem.trim() || !this.matchId || !this.remetenteId) return;
    this.matchService.enviarMensagem(this.matchId, this.remetenteId, this.novaMensagem).subscribe(() => {
      this.novaMensagem = '';
      this.carregarMensagens();
    });
  }
}
