import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';

export interface CriarUsuarioDTO {
  nome: string;
  email: string;
  dataNascimento: Date;
  cidade: string;
  estado: string;
  telefone?: string;
  descricao?: string;
  interesses?: string[];
  latitude?: number;
  longitude?: number;
  fotoPerfil?: string;  // ← Adicionar foto
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) {}

  criarUsuario(usuario: CriarUsuarioDTO, senha: string): Observable<Usuario> {
    const payload = { ...usuario, senha };
    return this.http.post<Usuario>(this.apiUrl, payload);
  }

  buscarPorId(id: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`);
  }

  listarTodos(usuarioId: string): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}?usuarioId=${usuarioId}`);
  }

  atualizarUsuario(id: string, usuario: Partial<Usuario>): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}`, usuario);
  }

  deletarUsuario(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
