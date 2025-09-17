import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          console.log('Login response received:', response);
          if (response && response.data && response.data.token) {
            console.log('Token found, saving:', response.data.token); 
            this.authService.saveToken(response.data.token);
            this.router.navigate(['/diary']);
          } else {
            this.errorMessage = 'Falha ao processar a resposta de login. Token não encontrado.';
            console.error('Login response is missing data or token field:', response);
          }
        },
        error: (err) => {
          this.errorMessage = 'Email ou senha inválidos.';
          console.error('Login error:', err);
        }
      });
    }
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }
}