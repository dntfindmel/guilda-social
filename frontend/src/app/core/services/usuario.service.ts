import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) {}

  criarUsuario(usuario: Partial<Usuario>, senha: string): Observable<Usuario> {
    const payload = { ...usuario, senha };
    return this.http.post<Usuario>(this.apiUrl, payload);
  }

  buscarPorId(id: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`);
  }

  listarTodos(usuarioId: string): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}?usuarioId=${usuarioId}`);
  }
}
