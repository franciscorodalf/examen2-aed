Aquí tienes el **Kit de Login Completo** para el examen.

El Login suele ser la parte donde más gente suspende porque requiere tocar 4 archivos distintos y conectarlos bien. Si te falla uno, no funciona nada.

Sigue este orden exacto para implementarlo sin errores:

### 1. El Servicio (`src/app/services/auth.service.ts`)

Este es el cerebro. Se encarga de hablar con la API y guardar el token en el navegador.

```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  
  // URL de la API (cámbiala según el examen)
  private url = 'http://localhost:3000/auth/login'; 

  // 1. Método para Loguearse
  login(credenciales: any) {
    return this.http.post<any>(this.url, credenciales).pipe(
      tap((response) => {
        // Asume que la API devuelve un objeto con la propiedad 'token'
        localStorage.setItem('token', response.token);
      })
    );
  }

  // 2. Saber si estoy logueado
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token'); // Devuelve true si hay token
  }

  // 3. Obtener el token (para el interceptor)
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // 4. Cerrar sesión
  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}

```

---

### 2. El Interceptor (`src/app/interceptors/auth.interceptor.ts`)

**IMPORTANTE:** Sin esto, el Login es inútil. Este archivo mete el token en cada petición automáticamente.

```typescript
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    // Clona la petición y le mete el Header
    const cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(cloned);
  }

  return next(req);
};

```

⚠️ **ALERTA ROJA:** No olvides registrar el interceptor en `app.config.ts` o no funcionará:

```typescript
// en app.config.ts
providers: [
  provideHttpClient(withInterceptors([authInterceptor])) // <--- AQUÍ
]

```

---

### 3. El Guardián (`src/app/guards/auth.guard.ts`)

Protege las rutas para que nadie entre sin loguearse.

```typescript
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isLoggedIn()) {
    return true; // Pasa, amigo
  } else {
    router.navigate(['/login']); // Fuera de aquí
    return false;
  }
};

```

**Y lo aplicas en `app.routes.ts`:**

```typescript
{ path: 'tareas', component: Tasks, canActivate: [authGuard] }, // <--- AQUÍ

```

---

### 4. La Pantalla de Login (`src/app/pages/login/login.ts`)

El formulario visual.

```typescript
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="container">
      <h2>Iniciar Sesión</h2>
      <form [formGroup]="form" (ngSubmit)="entrar()">
        
        <div>
           <label>Usuario</label>
           <input formControlName="username" type="text" class="input">
        </div>

        <div>
           <label>Contraseña</label>
           <input formControlName="password" type="password" class="input">
        </div>

        <p class="error" *ngIf="errorMsg">{{ errorMsg }}</p>

        <button type="submit">Entrar</button>
      </form>
    </div>
  `
})
export class Login {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  
  errorMsg = '';

  form = this.fb.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  entrar() {
    if (this.form.invalid) return;

    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => {
        this.router.navigate(['/tareas']); // Redirige al éxito
      },
      error: () => {
        this.errorMsg = 'Usuario o contraseña incorrectos';
      }
    });
  }
}

```

---

### 🚑 TRUCO DE EMERGENCIA (Si el Backend de Login Falla)

A veces en los exámenes el servidor de Login no va, o te da pereza montarlo todo y quieres demostrar que sabes hacer el resto.

**Hackea tu propio servicio (`auth.service.ts`):**

Si no consigues loguearte con la API real, cambia el método `login` por esto para simularlo y poder seguir trabajando en el resto del examen:

```typescript
// LOGIN FALSO PARA SALIR DEL PASO
login(credenciales: any) {
  // Guardamos un token falso
  localStorage.setItem('token', 'soy-un-token-falso-pero-sirvo');
  
  // Devolvemos un observable vacío para engañar al componente
  return of(true); 
}

```

*(Recuerda importar `of` de `rxjs`)*.

Con esto, el Guard te dejará pasar, el Interceptor enviará el token falso, y podrás mostrar que el resto de tu aplicación (Lista, Crear, Editar) funciona perfectamente.