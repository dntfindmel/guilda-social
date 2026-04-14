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

  // Rotas protegidas (exigem login) - Sprint 3
  {
    path: 'sugestoes',
    loadComponent: () => import('./features/matches/sugestoes/sugestoes.component')
      .then(m => m.SugestoesComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'jogador/:id',
    loadComponent: () => import('./features/matches/detalhe-jogador/detalhe-jogador.component')
      .then(m => m.DetalheJogadorComponent),
    canActivate: [AuthGuard]
  },
  {
     path: 'perfil',
     loadComponent: () => import('./features/perfil/visualizar-perfil/visualizar-perfil.component')
       .then(m => m.VisualizarPerfilComponent),
     canActivate: [AuthGuard]
   },
   {
     path: 'perfil/editar',
     loadComponent: () => import('./features/perfil/editar-perfil/editar-perfil.component')
       .then(m => m.EditarPerfilComponent),
     canActivate: [AuthGuard]
   },
  // {
  //   path: 'grupos',
  //   loadComponent: () => import('./features/grupos/listar-grupos/listar-grupos.component')
  //     .then(m => m.ListarGruposComponent),
  //   canActivate: [AuthGuard]
  // },
  // {
  //   path: 'grupos/criar',
  //   loadComponent: () => import('./features/grupos/criar-grupo/criar-grupo.component')
  //     .then(m => m.CriarGrupoComponent),
  //   canActivate: [AuthGuard]
  // },
  // {
  //   path: 'grupo/:id',
  //   loadComponent: () => import('./features/grupos/detalhe-grupo/detalhe-grupo.component')
  //     .then(m => m.DetalheGrupoComponent),
  //   canActivate: [AuthGuard]
  // },
  // {
  //   path: 'configuracoes',
  //   loadComponent: () => import('./features/configuracoes/configuracoes.component')
  //     .then(m => m.ConfiguracoesComponent),
  //   canActivate: [AuthGuard]
  // },

  // Fallback
  { path: '**', redirectTo: '/login' }
];
