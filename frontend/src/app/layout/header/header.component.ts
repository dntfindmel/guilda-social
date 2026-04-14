import { Component, inject, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

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

  @Output() recarregar = new EventEmitter<void>();

  getUsuarioNome(): string {
    return localStorage.getItem('usuarioNome') || 'Jogador';
  }

  irParaPerfil(): void {
    const usuarioId = this.authService.getUsuarioId();
    if (usuarioId) {
      this.router.navigate(['/perfil']);
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  onRecarregar(): void {
    this.recarregar.emit();
  }
}
