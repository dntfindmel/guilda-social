import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Perfil, AtualizarPerfilRequest } from '../models/perfil.model';

@Injectable({
  providedIn: 'root'
})
export class PerfilService {
  private apiUrl = 'http://localhost:8080/api/perfil';
  private uploadUrl = 'http://localhost:8080/api/upload';

  constructor(private http: HttpClient) {}

  buscarPerfil(usuarioId: string): Observable<Perfil> {
    return this.http.get<Perfil>(`${this.apiUrl}/${usuarioId}`);
  }

  atualizarPerfil(usuarioId: string, dados: AtualizarPerfilRequest): Observable<Perfil> {
    return this.http.put<Perfil>(`${this.apiUrl}/${usuarioId}`, dados);
  }

  uploadFoto(usuarioId: string, file: File): Observable<{ fotoUrl: string; message: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ fotoUrl: string; message: string }>(
      `${this.uploadUrl}/foto/${usuarioId}`,
      formData
    );
  }

  removerFoto(usuarioId: string): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.uploadUrl}/foto/${usuarioId}`);
  }
}
