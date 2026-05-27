import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { TokenService } from './token.service';

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  type: string;
  usuarioId: string;
  nome: string;
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<LoginResponse | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    private tokenService: TokenService
  ) {
    this.loadStoredUser();
  }

  private loadStoredUser(): void {
    const token = this.tokenService.getToken();
    const usuarioId = localStorage.getItem('usuarioId');
    const usuarioNome = localStorage.getItem('usuarioNome');

    console.log('loadStoredUser - Token:', token ? 'existe' : 'não existe');
    console.log('loadStoredUser - usuarioId:', usuarioId);

    if (token && usuarioId) {
      this.currentUserSubject.next({
        token,
        type: 'Bearer',
        usuarioId,
        nome: usuarioNome || '',
        email: ''
      });
    }
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        console.log('Login bem-sucedido:', response);
        console.log('Salvando usuarioId:', response.usuarioId);
        console.log('Salvando nome:', response.nome);

        this.tokenService.saveToken(response.token);
        localStorage.setItem('usuarioId', response.usuarioId);
        localStorage.setItem('usuarioNome', response.nome);
        this.currentUserSubject.next(response);

        // Verificar se salvou
        console.log('Verificação - usuarioId salvo:', localStorage.getItem('usuarioId'));
      })
    );
  }

  logout(): void {
    console.log('Logout - limpando dados');
    this.tokenService.removeToken();
    localStorage.removeItem('usuarioId');
    localStorage.removeItem('usuarioNome');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    const token = this.tokenService.getToken();
    const usuarioId = localStorage.getItem('usuarioId');
    const isAuth = token !== null && usuarioId !== null;
    console.log('isAuthenticated:', isAuth, 'token:', !!token, 'usuarioId:', !!usuarioId);
    return isAuth;
  }

  getCurrentUser(): LoginResponse | null {
    const token = this.tokenService.getToken();
    const usuarioId = localStorage.getItem('usuarioId');
    const usuarioNome = localStorage.getItem('usuarioNome');

    if (token && usuarioId) {
      return {
        token,
        type: 'Bearer',
        usuarioId,
        nome: usuarioNome || '',
        email: ''
      };
    }
    return null;
  }

  getUsuarioId(): string | null {
    const id = localStorage.getItem('usuarioId');
    console.log('getUsuarioId retornando:', id);
    return id;
  }
}
