// frontend/src/app/core/services/match.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Match, Sugestao } from '../models/match.model';

@Injectable({
  providedIn: 'root'
})
export class MatchService {
  private apiUrl = 'http://localhost:8080/api/matches';
  private chatUrl = 'http://localhost:8080/api/chat';  

  constructor(private http: HttpClient) {}

  // Sugestões
  getSugestoes(usuarioId: string): Observable<Sugestao[]> {
    return this.http.get<Sugestao[]>(`${this.apiUrl}/sugestoes/${usuarioId}`);
  }

  // Enviar solicitação de match
  enviarSolicitacao(usuarioId: string, alvoId: string): Observable<Match> {
    const payload = { alvoId: alvoId };
    return this.http.post<Match>(`${this.apiUrl}/solicitar/${usuarioId}`, payload);
  }

  // CORRIGIDO: usar chatUrl em vez de apiUrl
  getMeusMatches(usuarioId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.chatUrl}/matches/${usuarioId}`);
  }

  // CORRIGIDO: usar chatUrl em vez de apiUrl
  getMensagensChat(matchId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.chatUrl}/mensagens/${matchId}`);
  }

  // CORRIGIDO: usar chatUrl em vez de apiUrl
  enviarMensagem(matchId: string, remetenteId: string, conteudo: string): Observable<any> {
    return this.http.post(`${this.chatUrl}/mensagens/${matchId}?remetenteId=${remetenteId}&conteudo=${conteudo}`, {});
  }
}
