import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);

  loginForm: FormGroup;
  loading = false;
  errorMessage = '';

  constructor() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.errorMessage = '';

      const credentials = {
        email: this.loginForm.get('email')?.value,
        senha: this.loginForm.get('senha')?.value
      };

      console.log('Tentando login com:', credentials);

      this.authService.login(credentials).subscribe({
        next: (response) => {
          console.log('Login bem-sucedido:', response);
          this.loading = false;
          localStorage.setItem('token', response.token);
          localStorage.setItem('usuarioId', response.usuarioId);
          localStorage.setItem('usuarioNome', response.nome);
          this.router.navigate(['/sugestoes']);
        },
        error: (error) => {
          console.error('Erro no login:', error);
          this.loading = false;
          this.errorMessage = error.error?.message || 'E-mail ou senha inválidos';
        }
      });
    } else {
      console.log('Formulário inválido');
      this.markFormGroupTouched(this.loginForm);
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
