import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario.service';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.css']
})
export class CadastroComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private usuarioService = inject(UsuarioService);

  cadastroForm: FormGroup;
  passoAtual = 1;
  estados = ['AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'];
  loading = false;
  errorMessage = '';

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

  avancarPasso(): void {
    const nomeControl = this.cadastroForm.get('nome');
    const emailControl = this.cadastroForm.get('email');
    const senhaControl = this.cadastroForm.get('senha');
    
    if (this.passoAtual === 1 && nomeControl?.valid && emailControl?.valid && senhaControl?.valid) {
      this.passoAtual = 2;
    } else {
      nomeControl?.markAsTouched();
      emailControl?.markAsTouched();
      senhaControl?.markAsTouched();
    }
  }

  voltarPasso(): void {
    this.passoAtual = 1;
  }

  cadastrar(): void {
    if (this.cadastroForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      
      const { confirmarSenha, ...usuarioData } = this.cadastroForm.value;
      
      this.usuarioService.criarUsuario(usuarioData, usuarioData.senha).subscribe({
        next: (usuario) => {
          this.loading = false;
          localStorage.setItem('usuarioId', usuario.id!);
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = error.error?.message || 'Erro ao cadastrar. Tente novamente.';
          console.error('Erro no cadastro:', error);
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
