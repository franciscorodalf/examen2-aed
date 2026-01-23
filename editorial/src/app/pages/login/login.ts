import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [ReactiveFormsModule],
    templateUrl: './login.html',
    styleUrl: './login.css'
})
export class Login {
    private fb = inject(FormBuilder);
    private authService = inject(AuthService);
    private router = inject(Router);

    errorMsg: string | null = null;
    loading = false;

    form = this.fb.nonNullable.group({
        username: ['', Validators.required],
        password: ['', Validators.required]
    });

    login() {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        this.loading = true;
        this.errorMsg = null;

        this.authService.login(this.form.getRawValue()).subscribe({
            next: () => {
                this.loading = false;
                this.router.navigateByUrl('/tareas');
            },
            error: (err) => {
                this.loading = false;
                this.errorMsg = 'Usuario o contraseña incorrectos, o servidor no disponible.';
                console.error('Login error', err);
            }
        });
    }
}
