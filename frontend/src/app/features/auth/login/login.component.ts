import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
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
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  loginForm: FormGroup;
  recuperarForm: FormGroup;
  loading = false;
  recuperando = false;
  errorMessage = '';
  successMessage = '';
  modoRecuperacao = false;

  constructor() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]]
    });

    this.recuperarForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.errorMessage = '';

      this.authService.login(this.loginForm.value).subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/sugestoes']);
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = error.error?.message || 'E-mail ou senha inválidos';
        }
      });
    }
  }

  ativarModoRecuperacao(): void {
    this.modoRecuperacao = true;
    this.errorMessage = '';
    this.successMessage = '';
  }

  voltarLogin(): void {
    this.modoRecuperacao = false;
    this.recuperarForm.reset();
  }

  solicitarRecuperacao(): void {
    if (this.recuperarForm.valid) {
      this.recuperando = true;
      this.errorMessage = '';
      this.successMessage = '';

      this.http.post('http://localhost:8080/api/auth/recuperar-senha', {
        email: this.recuperarForm.get('email')?.value
      }).subscribe({
        next: () => {
          this.recuperando = false;
          this.successMessage = 'Um link de recuperação foi enviado para seu e-mail!';
          setTimeout(() => {
            this.voltarLogin();
          }, 3000);
        },
        error: (error) => {
          this.recuperando = false;
          this.errorMessage = error.error?.message || 'E-mail não encontrado';
        }
      });
    }
  }
}
