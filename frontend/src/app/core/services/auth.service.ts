// frontend/src/app/core/services/auth.service.ts
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
    const userData = localStorage.getItem('currentUser');
    if (token && userData) {
      this.currentUserSubject.next(JSON.parse(userData));
    }
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        this.tokenService.saveToken(response.token);
        localStorage.setItem('currentUser', JSON.stringify(response));
        this.currentUserSubject.next(response);
      })
    );
  }

  logout(): void {
    this.tokenService.removeToken();
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.tokenService.hasToken() && this.currentUserSubject.value !== null;
  }

  getCurrentUser(): LoginResponse | null {
    return this.currentUserSubject.value;
  }
}
