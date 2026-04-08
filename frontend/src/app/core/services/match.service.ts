import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Match, Sugestao } from '../models/match.model';

@Injectable({
  providedIn: 'root'
})
export class MatchService {
  private apiUrl = 'http://localhost:8080/api/matches';

  constructor(private http: HttpClient) {}

  getSugestoes(usuarioId: string): Observable<Sugestao[]> {
    return this.http.get<Sugestao[]>(`${this.apiUrl}/sugestoes/${usuarioId}`);
  }

  enviarSolicitacao(usuarioId: string, alvoId: string): Observable<Match> {
    return this.http.post<Match>(`${this.apiUrl}/solicitar/${usuarioId}`, { alvoId });
  }

  responderSolicitacao(matchId: string, status: string): Observable<Match> {
    return this.http.put<Match>(`${this.apiUrl}/responder/${matchId}?status=${status}`, {});
  }

  getMeusMatches(usuarioId: string): Observable<Match[]> {
    return this.http.get<Match[]>(`${this.apiUrl}/meus-matches/${usuarioId}`);
  }
}
