import { Component, OnInit, inject, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService, CriarUsuarioDTO } from '../../../core/services/usuario.service';

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
  private usuarioService = inject(UsuarioService);

  @ViewChild('fotoInput') fotoInput!: ElementRef<HTMLInputElement>;

  cadastroForm: FormGroup;
  loading = false;
  errorMessage = '';
  fotoPreview: string | undefined;

  estados = ['AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'];

  constructor() {
    this.cadastroForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required],
      dataNascimento: ['', [Validators.required, this.validarIdade]],
      telefone: ['', [Validators.pattern(/^\(?[1-9]{2}\)? ?(?:[2-8]|9[1-9])[0-9]{3}-?[0-9]{4}$/)]],
      cidade: ['', Validators.required],
      estado: ['', Validators.required],
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

    return idade >= 13 ? null : { menorIdade: true };
  }

  obterLocalizacao(): void {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          this.cadastroForm.patchValue({
            latitude: position.coords.latitude,
            longitude: position.coords.longitude
          });
        },
        (error) => {
          console.log('Localização não permitida pelo usuário');
        }
      );
    }
  }

  selecionarFoto(): void {
    this.fotoInput.nativeElement.click();
  }

  onFotoSelecionada(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        this.fotoPreview = e.target?.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  cadastrar(): void {
    if (this.cadastroForm.valid) {
      this.loading = true;
      this.errorMessage = '';

      // Coletar interesses (convertendo null para undefined)
      const interesses: string[] = [];
      if (this.cadastroForm.get('interesseTabuleiro')?.value) interesses.push('TABULEIRO');
      if (this.cadastroForm.get('interesseCartas')?.value) interesses.push('CARD_GAME');
      if (this.cadastroForm.get('interesseRPG')?.value) interesses.push('RPG_MESA');

      // Construir objeto do tipo CriarUsuarioDTO
      const usuarioData: CriarUsuarioDTO = {
        nome: this.cadastroForm.get('nome')?.value,
        email: this.cadastroForm.get('email')?.value,
        dataNascimento: this.cadastroForm.get('dataNascimento')?.value,
        cidade: this.cadastroForm.get('cidade')?.value,
        estado: this.cadastroForm.get('estado')?.value,
        // Campos opcionais: usar undefined em vez de null
        telefone: this.cadastroForm.get('telefone')?.value || undefined,
        descricao: this.cadastroForm.get('descricao')?.value || undefined,
        interesses: interesses.length > 0 ? interesses : undefined,
        fotoPerfil: this.fotoPreview || undefined,
        latitude: this.cadastroForm.get('latitude')?.value || undefined,
        longitude: this.cadastroForm.get('longitude')?.value || undefined
      };

      const senha = this.cadastroForm.get('senha')?.value;

      this.usuarioService.criarUsuario(usuarioData, senha).subscribe({
        next: (usuario) => {
          this.loading = false;
          localStorage.setItem('usuarioId', usuario.id!);
          localStorage.setItem('usuarioNome', usuario.nome);
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = error.error?.message || 'Erro ao cadastrar. Tente novamente.';
          console.error('Erro detalhado:', error);
        }
      });
    } else {
      this.markFormGroupTouched(this.cadastroForm);

      if (!this.cadastroForm.get('aceitaTermos')?.value) {
        this.errorMessage = 'Você precisa aceitar os termos para continuar.';
      }
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
