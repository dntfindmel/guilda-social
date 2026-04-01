// frontend/src/app/app.routes.ts
import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { LoginGuard } from './core/guards/login.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // Rotas públicas
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component')
      .then(m => m.LoginComponent),
    canActivate: [LoginGuard]
  },
  {
    path: 'cadastro',
    loadComponent: () => import('./features/auth/cadastro/cadastro.component')
      .then(m => m.CadastroComponent),
    canActivate: [LoginGuard]
  },

  // Fallback
  { path: '**', redirectTo: '/login' }
];
