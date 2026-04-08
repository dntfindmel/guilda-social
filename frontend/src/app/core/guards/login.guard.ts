import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(): boolean {
    console.log('LoginGuard - Verificando...');

    if (this.authService.isAuthenticated()) {
      console.log('✅ Usuário já está logado, redirecionando para sugestões');
      this.router.navigate(['/sugestoes']);
      return false;
    }

    console.log('❌ Usuário não está logado, acesso permitido à página de login');
    return true;
  }
}
