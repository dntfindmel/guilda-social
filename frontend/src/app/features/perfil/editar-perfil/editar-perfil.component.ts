import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { PerfilService } from '../../../core/services/perfil.service';
import { HeaderComponent } from '../../../layout/header/header.component';

@Component({
  selector: 'app-editar-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, HeaderComponent],
  templateUrl: './editar-perfil.component.html',
  styleUrls: ['./editar-perfil.component.css']
})
export class EditarPerfilComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);
  private perfilService = inject(PerfilService);

  perfilForm: FormGroup;
  loading = true;
  saving = false;
  errorMessage = '';
  successMessage = '';
  fotoPreview: string | undefined;
  fotoFile: File | null = null;
  estados = ['AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'];

  constructor() {
    this.perfilForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      telefone: ['', [Validators.pattern(/^\(?[1-9]{2}\)? ?(?:[2-8]|9[1-9])[0-9]{3}-?[0-9]{4}$/)]],
      cidade: ['', Validators.required],
      estado: ['', Validators.required],
      descricao: ['', [Validators.maxLength(500)]]
    });
  }

  ngOnInit(): void {
    this.carregarPerfil();
  }

  carregarPerfil(): void {
    const usuarioId = this.authService.getUsuarioId();

    if (!usuarioId) {
      this.router.navigate(['/login']);
      return;
    }

    this.perfilService.buscarPerfil(usuarioId).subscribe({
      next: (data) => {
        this.perfilForm.patchValue({
          nome: data.nome,
          telefone: data.telefone || '',
          cidade: data.cidade,
          estado: data.estado,
          descricao: data.descricao || ''
        });
        if (data.fotoPerfil) {
          this.fotoPreview = `http://localhost:8080${data.fotoPerfil}`;
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar perfil:', error);
        this.errorMessage = 'Erro ao carregar perfil';
        this.loading = false;
      }
    });
  }

  onFotoSelecionada(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.fotoFile = input.files[0];
      // Preview imediato
      const reader = new FileReader();
      reader.onload = (e) => {
        this.fotoPreview = e.target?.result as string;
      };
      reader.readAsDataURL(this.fotoFile);
    }
  }

  uploadFoto(): void {
    const usuarioId = this.authService.getUsuarioId();
    if (!usuarioId || !this.fotoFile) return;

    this.perfilService.uploadFoto(usuarioId, this.fotoFile).subscribe({
      next: (response) => {
        this.successMessage = response.message;
        this.fotoPreview = `http://localhost:8080${response.fotoUrl}`;
        setTimeout(() => { this.successMessage = ''; }, 3000);
      },
      error: (error) => {
        console.error('Erro ao fazer upload:', error);
        this.errorMessage = 'Erro ao fazer upload da foto';
        setTimeout(() => { this.errorMessage = ''; }, 3000);
      }
    });
  }

  removerFoto(): void {
    const usuarioId = this.authService.getUsuarioId();
    if (!usuarioId) return;

    this.perfilService.removerFoto(usuarioId).subscribe({
      next: () => {
        this.fotoPreview = undefined;
        this.fotoFile = null;
        this.successMessage = 'Foto removida com sucesso!';
        setTimeout(() => { this.successMessage = ''; }, 3000);
      },
      error: (error) => {
        console.error('Erro ao remover foto:', error);
        this.errorMessage = 'Erro ao remover foto';
        setTimeout(() => { this.errorMessage = ''; }, 3000);
      }
    });
  }

  salvar(): void {
    if (this.perfilForm.valid) {
      this.saving = true;
      this.errorMessage = '';
      this.successMessage = '';

      const usuarioId = this.authService.getUsuarioId();
      if (!usuarioId) {
        this.router.navigate(['/login']);
        return;
      }

      const dados: any = {};
      const nome = this.perfilForm.get('nome')?.value;
      if (nome) dados.nome = nome;

      const telefone = this.perfilForm.get('telefone')?.value;
      if (telefone) dados.telefone = telefone;

      const cidade = this.perfilForm.get('cidade')?.value;
      if (cidade) dados.cidade = cidade;

      const estado = this.perfilForm.get('estado')?.value;
      if (estado) dados.estado = estado;

      const descricao = this.perfilForm.get('descricao')?.value;
      if (descricao) dados.descricao = descricao;

      this.perfilService.atualizarPerfil(usuarioId, dados).subscribe({
        next: () => {
          this.saving = false;
          this.successMessage = 'Perfil atualizado com sucesso!';

          if (dados.nome) {
            localStorage.setItem('usuarioNome', dados.nome);
          }

          // Se tiver foto para upload, faz depois
          if (this.fotoFile) {
            this.uploadFoto();
          }

          setTimeout(() => {
            this.router.navigate(['/perfil']);
          }, 1500);
        },
        error: (error) => {
          this.saving = false;
          this.errorMessage = error.error?.message || 'Erro ao salvar perfil';
        }
      });
    } else {
      this.markFormGroupTouched(this.perfilForm);
    }
  }

  cancelar(): void {
    this.router.navigate(['/perfil']);
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
}
