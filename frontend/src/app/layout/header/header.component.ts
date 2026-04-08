import { Component, inject } from '@angular/core';
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

  getUsuarioNome(): string {
    return localStorage.getItem('usuarioNome') || 'Jogador';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
