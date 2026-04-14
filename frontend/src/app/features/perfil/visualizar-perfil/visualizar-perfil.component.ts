import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { PerfilService } from '../../../core/services/perfil.service';
import { HeaderComponent } from '../../../layout/header/header.component';
import { Perfil } from '../../../core/models/perfil.model';

@Component({
  selector: 'app-visualizar-perfil',
  standalone: true,
  imports: [CommonModule, HeaderComponent],
  templateUrl: './visualizar-perfil.component.html',
  styleUrls: ['./visualizar-perfil.component.css']
})
export class VisualizarPerfilComponent implements OnInit {
  private authService = inject(AuthService);
  private perfilService = inject(PerfilService);
  private router = inject(Router);

  perfil: Perfil | null = null;
  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    this.carregarPerfil();
  }

  carregarPerfil(): void {
    const usuarioId = this.authService.getUsuarioId();

    if (!usuarioId) {
      this.router.navigate(['/login']);
      return;
    }

    this.perfilService.buscarPerfil(usuarioId).subscribe({
      next: (data) => {
        this.perfil = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar perfil:', error);
        this.errorMessage = 'Erro ao carregar perfil';
        this.loading = false;
      }
    });
  }

  getInitials(): string {
    if (!this.perfil) return '';
    return this.perfil.nome
      .split(' ')
      .map(n => n[0])
      .join('')
      .substring(0, 2)
      .toUpperCase();
  }

  getFotoUrl(): string {
    if (!this.perfil?.fotoPerfil) return '';
    if (this.perfil.fotoPerfil.startsWith('http')) {
      return this.perfil.fotoPerfil;
    }
    return `http://localhost:8080${this.perfil.fotoPerfil}`;
  }

  getDataCadastro(): string {
    if (!this.perfil?.dataCadastro) return '';
    return new Date(this.perfil.dataCadastro).toLocaleDateString('pt-BR');
  }

  editarPerfil(): void {
    this.router.navigate(['/perfil/editar']);
  }

  voltar(): void {
    this.router.navigate(['/sugestoes']);
  }
}
