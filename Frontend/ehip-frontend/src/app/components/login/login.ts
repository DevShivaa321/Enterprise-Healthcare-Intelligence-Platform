import { Component } from '@angular/core';

import { Router } from '@angular/router';

import { FormsModule } from '@angular/forms';

import { ButtonModule } from 'primeng/button';

import { InputTextModule } from 'primeng/inputtext';

import { CommonModule } from '@angular/common';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',

  standalone: true,

  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule],

  templateUrl: './login.html',
})
export class LoginComponent {
  loginName: string = '';

  password: string = '';

  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  login() {
    this.authService.Login({ loginName: this.loginName, password: this.password }).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);

        this.router.navigate(['/patients']);
      },

      error: () => {
        this.errorMessage = 'Login failed. Please check your credentials and try again.';
      },
    });
  }
}
