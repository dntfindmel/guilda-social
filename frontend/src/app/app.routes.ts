import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/cadastro', pathMatch: 'full' },
  {
    path: 'cadastro',
    loadComponent: () => import('./features/auth/cadastro/cadastro.component')
      .then(m => m.CadastroComponent)
  }
];
