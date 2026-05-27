import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'auth_token';

  saveToken(token: string): void {
    console.log('Salvando token:', token.substring(0, 20) + '...');
    if (token) {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  getToken(): string | null {
    const token = localStorage.getItem(this.TOKEN_KEY);
    console.log('Token recuperado:', token ? 'sim' : 'não');
    return token;
  }

  removeToken(): void {
    console.log('Removendo token');
    localStorage.removeItem(this.TOKEN_KEY);
  }

  hasToken(): boolean {
    const has = this.getToken() !== null;
    console.log('hasToken:', has);
    return has;
  }
}
