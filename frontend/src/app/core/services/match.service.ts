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

  // Lista de matches para chat
  getMeusMatches(usuarioId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.chatUrl}/matches/${usuarioId}`);
  }

  // Buscar mensagens de um match
  getMensagensChat(matchId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.chatUrl}/mensagens/${matchId}`);
  }

  // Enviar mensagem
  enviarMensagem(matchId: string, remetenteId: string, conteudo: string): Observable<any> {
    // CORRIGIDO: enviar no corpo da requisição
    const payload = { remetenteId, conteudo };
    return this.http.post(`${this.chatUrl}/mensagens/${matchId}`, payload);
  }

    // match.service.ts
  getConversasNaoLidas(usuarioId: string): Observable<number> {
    return this.http.get<number>(`${this.chatUrl}/nao-lidas/${usuarioId}`);
  }
}
