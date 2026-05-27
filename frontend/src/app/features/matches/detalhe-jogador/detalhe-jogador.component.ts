import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router'; // Remove RouterLink
import { MatchService } from '../../../core/services/match.service';
import { AuthService } from '../../../core/services/auth.service';
import { UsuarioService } from '../../../core/services/usuario.service';
import { Usuario } from '../../../core/models/usuario.model';

@Component({
  selector: 'app-detalhe-jogador',
  standalone: true,
  imports: [CommonModule], 
  templateUrl: './detalhe-jogador.component.html',
  styleUrls: ['./detalhe-jogador.component.css']
})
export class DetalheJogadorComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private matchService = inject(MatchService);
  private authService = inject(AuthService);
  private usuarioService = inject(UsuarioService);

  jogador: Usuario | null = null;
  loading = true;
  errorMessage = '';
  enviandoSolicitacao = false;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.carregarJogador(id);
    } else {
      this.errorMessage = 'ID do jogador não encontrado';
      this.loading = false;
    }
  }

  carregarJogador(id: string): void {
    this.loading = true;
    this.usuarioService.buscarPorId(id).subscribe({
      next: (data) => {
        this.jogador = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar jogador:', error);
        this.errorMessage = 'Erro ao carregar informações do jogador';
        this.loading = false;
      }
    });
  }

  enviarSolicitacao(): void {
    const usuarioAtual = this.authService.getCurrentUser();
    if (!usuarioAtual || !usuarioAtual.usuarioId || !this.jogador) return;

    this.enviandoSolicitacao = true;
    this.matchService.enviarSolicitacao(usuarioAtual.usuarioId, this.jogador.id!).subscribe({
      next: () => {
        alert('Solicitação enviada com sucesso!');
        this.enviandoSolicitacao = false;
        this.router.navigate(['/sugestoes']);
      },
      error: (error) => {
        console.error('Erro ao enviar solicitação:', error);
        alert('Erro ao enviar solicitação. Tente novamente.');
        this.enviandoSolicitacao = false;
      }
    });
  }

  voltar(): void {
    this.router.navigate(['/sugestoes']);
  }

  getInitials(): string {
    if (!this.jogador) return '';
    return this.jogador.nome
      .split(' ')
      .map(n => n[0])
      .join('')
      .substring(0, 2)
      .toUpperCase();
  }
}
