import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../core/services/auth.service';
import { PerfilService } from '../../../core/services/perfil.service';
import { HeaderComponent } from '../../../layout/header/header.component';

@Component({
  selector: 'app-editar-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HeaderComponent],
  templateUrl: './editar-perfil.component.html',
  styleUrls: ['./editar-perfil.component.css']
})
export class EditarPerfilComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private perfilService = inject(PerfilService);

  editForm: FormGroup;
  loading = true;
  saving = false;
  errorMessage = '';
  successMessage = '';
  fotoPreview: string | undefined;
  fotoFile: File | null = null;
  distanciaMaxima: number = 20;
  estados = ['AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'];

  constructor() {
    this.editForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      telefone: ['', [Validators.pattern(/^\(?[1-9]{2}\)? ?(?:[2-8]|9[1-9])[0-9]{3}-?[0-9]{4}$/)]],
      cidade: ['', Validators.required],
      estado: ['', Validators.required],
      descricao: ['', [Validators.maxLength(500)]],
      distanciaMaximaKm: [20, [Validators.min(1), Validators.max(100)]],
      senhaAtual: [''],
      novaSenha: ['', [Validators.minLength(6)]],
      confirmarNovaSenha: ['']
    }, { validators: this.checkSenhas });
  }

  ngOnInit(): void {
    this.carregarPerfil();
  }

  checkSenhas(group: FormGroup): any {
    const novaSenha = group.get('novaSenha')?.value;
    const confirmar = group.get('confirmarNovaSenha')?.value;
    if (novaSenha && confirmar && novaSenha !== confirmar) {
      return { senhasDiferentes: true };
    }
    return null;
  }

  carregarPerfil(): void {
    const usuarioId = this.authService.getUsuarioId();
    if (!usuarioId) {
      this.router.navigate(['/login']);
      return;
    }

    this.perfilService.buscarPerfil(usuarioId).subscribe({
      next: (data) => {
        this.editForm.patchValue({
          nome: data.nome,
          telefone: data.telefone || '',
          cidade: data.cidade,
          estado: data.estado,
          descricao: data.descricao || '',
          distanciaMaximaKm: data.distanciaMaximaKm || 20
        });
        this.distanciaMaxima = data.distanciaMaximaKm || 20;
        if (data.fotoPerfil && !data.fotoPerfil.startsWith('data:')) {
          this.fotoPreview = `http://localhost:8080${data.fotoPerfil}`;
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro:', error);
        this.errorMessage = 'Erro ao carregar perfil';
        this.loading = false;
      }
    });
  }

  onDistanciaChange(event: any): void {
    this.distanciaMaxima = event.target.value;
  }

  onFotoSelecionada(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.fotoFile = input.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        this.fotoPreview = e.target?.result as string;
      };
      reader.readAsDataURL(this.fotoFile);
    }
  }

  uploadFoto(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (!this.fotoFile) {
        resolve();
        return;
      }

      const usuarioId = this.authService.getUsuarioId();
      if (!usuarioId) {
        resolve();
        return;
      }

      const formData = new FormData();
      formData.append('file', this.fotoFile);

      this.http.post<{ fotoUrl: string }>(`http://localhost:8080/api/upload/foto/${usuarioId}`, formData)
        .subscribe({
          next: () => {
            console.log('Foto enviada com sucesso');
            resolve();
          },
          error: (error) => {
            console.error('Erro ao enviar foto:', error);
            reject(error);
          }
        });
    });
  }

  removerFoto(): void {
    const usuarioId = this.authService.getUsuarioId();
    if (!usuarioId) return;

    this.http.delete<{ message: string }>(`http://localhost:8080/api/upload/foto/${usuarioId}`).subscribe({
      next: () => {
        this.fotoPreview = undefined;
        this.fotoFile = null;
        this.successMessage = 'Foto removida com sucesso';
        setTimeout(() => { this.successMessage = ''; }, 3000);
      },
      error: (error) => {
        console.error('Erro:', error);
        this.errorMessage = 'Erro ao remover foto';
      }
    });
  }

  async salvar(): Promise<void> {
    if (this.editForm.valid) {
      this.saving = true;
      this.errorMessage = '';
      this.successMessage = '';

      const usuarioId = this.authService.getUsuarioId();
      if (!usuarioId) {
        this.router.navigate(['/login']);
        return;
      }

      const dados: any = {
        nome: this.editForm.get('nome')?.value,
        telefone: this.editForm.get('telefone')?.value || null,
        cidade: this.editForm.get('cidade')?.value,
        estado: this.editForm.get('estado')?.value,
        descricao: this.editForm.get('descricao')?.value || null,
        distanciaMaximaKm: this.editForm.get('distanciaMaximaKm')?.value
      };

      const senhaAtual = this.editForm.get('senhaAtual')?.value;
      const novaSenha = this.editForm.get('novaSenha')?.value;
      const temAlteracaoSenha = senhaAtual && novaSenha && novaSenha.length >= 6;

      try {
        await this.perfilService.atualizarPerfil(usuarioId, dados).toPromise();

        if (this.fotoFile) {
          await this.uploadFoto();
        }

        if (temAlteracaoSenha) {
          await this.http.post(`http://localhost:8080/api/auth/alterar-senha/${usuarioId}`, {
            senhaAntiga: senhaAtual,
            novaSenha: novaSenha
          }).toPromise();
          this.successMessage = 'Perfil e senha atualizados com sucesso!';
          this.editForm.patchValue({
            senhaAtual: '',
            novaSenha: '',
            confirmarNovaSenha: ''
          });
        } else {
          this.successMessage = 'Perfil atualizado com sucesso!';
        }

        if (dados.nome) {
          localStorage.setItem('usuarioNome', dados.nome);
        }

        setTimeout(() => {
          this.router.navigate(['/perfil']);
        }, 2000);

      } catch (error: any) {
        this.saving = false;
        this.errorMessage = error.error?.message || 'Erro ao salvar alterações';
      } finally {
        this.saving = false;
      }
    }
  }

  cancelar(): void {
    this.router.navigate(['/perfil']);
  }
}
