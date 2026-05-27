import { Component, OnInit, inject, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UsuarioService } from '../../../core/services/usuario.service';
import { GeolocationService } from '../../../core/services/geolocation.service';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.css']
})
export class CadastroComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private http = inject(HttpClient);
  private usuarioService = inject(UsuarioService);
  private geolocationService = inject(GeolocationService);

  @ViewChild('fotoInput') fotoInput!: ElementRef<HTMLInputElement>;

  cadastroForm: FormGroup;
  loading = false;
  errorMessage = '';
  fotoPreview: string | undefined;
  fotoFile: File | null = null;
  carregandoLocalizacao = false;
  cidadeAtual: string = '';
  estadoAtual: string = '';

  estados = ['AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'];

  constructor() {
    this.cadastroForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required],
      dataNascimento: ['', [Validators.required, this.validarIdade]],
      telefone: ['', [Validators.pattern(/^[0-9]{10,11}$/)]],
      descricao: ['', [Validators.maxLength(500)]],
      interesseTabuleiro: [false],
      interesseCartas: [false],
      interesseRPG: [false],
      aceitaTermos: [false, Validators.requiredTrue],
      latitude: [null],
      longitude: [null]
    }, { validators: this.checkSenhas });
  }

  ngOnInit(): void {
    this.obterLocalizacao();
  }

  checkSenhas(group: FormGroup): ValidationErrors | null {
    const senha = group.get('senha')?.value;
    const confirmar = group.get('confirmarSenha')?.value;
    return senha === confirmar ? null : { senhasDiferentes: true };
  }

  validarIdade(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;

    const dataNascimento = new Date(control.value);
    const hoje = new Date();
    let idade = hoje.getFullYear() - dataNascimento.getFullYear();
    const mes = hoje.getMonth() - dataNascimento.getMonth();

    if (mes < 0 || (mes === 0 && hoje.getDate() < dataNascimento.getDate())) {
      idade--;
    }

    return idade >= 18 ? null : { menorIdade: true };
  }

  obterLocalizacao(): void {
    this.carregandoLocalizacao = true;

    this.geolocationService.getCurrentPosition().subscribe({
      next: (location) => {
        this.cadastroForm.patchValue({
          latitude: location.lat,
          longitude: location.lng
        });
        this.cidadeAtual = location.cidade;
        this.estadoAtual = location.estado;
        this.carregandoLocalizacao = false;
      },
      error: (error) => {
        console.error('Erro ao obter localização:', error);
        this.carregandoLocalizacao = false;
      }
    });
  }

  selecionarFoto(): void {
    this.fotoInput.nativeElement.click();
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

  uploadFoto(usuarioId: string): Promise<void> {
    return new Promise((resolve, reject) => {
      if (!this.fotoFile) {
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

  async cadastrar(): Promise<void> {
    if (this.cadastroForm.valid) {
      this.loading = true;
      this.errorMessage = '';

      const interesses: string[] = [];
      if (this.cadastroForm.get('interesseTabuleiro')?.value) interesses.push('TABULEIRO');
      if (this.cadastroForm.get('interesseCartas')?.value) interesses.push('CARD_GAME');
      if (this.cadastroForm.get('interesseRPG')?.value) interesses.push('RPG_MESA');

      const usuarioData: any = {
        nome: this.cadastroForm.get('nome')?.value,
        email: this.cadastroForm.get('email')?.value,
        dataNascimento: this.cadastroForm.get('dataNascimento')?.value,
        cidade: this.cidadeAtual,
        estado: this.estadoAtual,
        telefone: this.cadastroForm.get('telefone')?.value || undefined,
        descricao: this.cadastroForm.get('descricao')?.value || undefined,
        interesses: interesses.length > 0 ? interesses : undefined,
        latitude: this.cadastroForm.get('latitude')?.value,
        longitude: this.cadastroForm.get('longitude')?.value
      };

      const senha = this.cadastroForm.get('senha')?.value;

      this.usuarioService.criarUsuario(usuarioData, senha).subscribe({
        next: async (usuario) => {
          if (this.fotoFile && usuario.id) {
            await this.uploadFoto(usuario.id);
          }

          this.loading = false;
          localStorage.setItem('usuarioId', usuario.id!);
          localStorage.setItem('usuarioNome', usuario.nome);
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = error.error?.message || 'Erro ao cadastrar. Tente novamente.';
        }
      });
    } else {
      this.markFormGroupTouched(this.cadastroForm);
    }
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
