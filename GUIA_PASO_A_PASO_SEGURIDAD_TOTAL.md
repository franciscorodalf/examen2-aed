
# 🔐 GUÍA PASO A PASO: Implementar Seguridad en Angular (JWT)

Esta guía contiene **TODO** el código necesario para conectar tu Angular con el backend Spring Boot que ya tienes. No hay abreviaciones. Copia y pega con confianza.

Sigue estos 5 pasos exactos.

---

## PASO 1: Crear `AuthService`
Este servicio se encarga de hablar con el backend para loguearse y de guardar el token en la memoria del navegador.

**Archivo**: `src/app/services/auth.service.ts`

```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

// Definimos la respuesta que esperamos del servidor
interface LoginResponse {
  token: string;
}

// Definimos los datos que enviamos al servidor
interface LoginRequest {
  username: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  // URL del backend (Asegúrate que coincide con tu AuthController de Java)
  private apiUrl = 'http://localhost:8080/api/auth/login';
  private tokenKey = 'auth_token';

  /**
   * Envía usuario y contraseña al servidor.
   * Si es correcto, guarda el token.
   */
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.apiUrl, credentials).pipe(
      tap(response => {
        // Guardamos el token en localStorage
        localStorage.setItem(this.tokenKey, response.token);
      })
    );
  }

  /**
   * Borra el token (Cerrar sesión)
   */
  logout(): void {
    localStorage.removeItem(this.tokenKey);
  }

  /**
   * Recupera el token guardado
   */
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  /**
   * Verifica si el usuario está logueado (tiene token)
   */
  isAuthenticated(): boolean {
    const token = this.getToken();
    return token !== null; // Devuelve true si existe, false si no
  }
}
```

---

## PASO 2: Crear el Interceptor
El interceptor es un "intermediario" que coge cada petición que sale de tu app y le pega una etiqueta con el token.

**Archivo**: `src/app/core/interceptors/auth.interceptor.ts` (Créalo si no existe)

```typescript
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    // Si tenemos token, clonamos la petición y le añadimos el Header
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedReq);
  }

  // Si no hay token, dejamos pasar la petición tal cual
  return next(req);
};
```

---

## PASO 3: Configurar la App (Registrar Interceptor)
Angular necesita saber que debe usar el interceptor que acabamos de crear.

**Archivo**: `src/app/app.config.ts`

```typescript
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    // AQUI ESTÁ LA CLAVE: Registramos el interceptor
    provideHttpClient(withInterceptors([authInterceptor])) 
  ]
};
```

---

## PASO 4: Crear la Pantalla de Login (`LoginComponent`)
Aquí es donde el usuario escribe sus datos. Manejaremos errores para mostrar si la contraseña es incorrecta.

**Archivo TS**: `src/app/login/login.component.ts`

```typescript
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common'; // Necesario para @if

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  // Variable para mostrar errores en el HTML
  errorMessage: string = '';

  // Definición del formulario
  loginForm: FormGroup = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });

  onSubmit() {
    if (this.loginForm.invalid) {
      return; 
    }

    // Limpiamos errores previos
    this.errorMessage = '';

    const credentials = this.loginForm.value;

    this.authService.login(credentials).subscribe({
      next: (response) => {
        console.log('Login exitoso!', response);
        // Redirigir a la página principal o lista de noticias
        this.router.navigate(['/']); 
      },
      error: (error) => {
        console.error('Error en login:', error);
        
        // Manejo de códigos de error HTTP
        if (error.status === 401 || error.status === 403) {
          this.errorMessage = 'Usuario o contraseña incorrectos.';
        } else if (error.status === 0) {
          this.errorMessage = 'No se puede conectar con el servidor backend.';
        } else {
          this.errorMessage = 'Ha ocurrido un error inesperado.';
        }
      }
    });
  }
}
```

**Archivo HTML**: `src/app/login/login.component.html`

```html
<div class="login-container" style="max-width: 400px; margin: 2rem auto; padding: 1rem; border: 1px solid #ccc; border-radius: 8px;">
  <h2>Iniciar Sesión</h2>
  
  <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
    
    <!-- Campo Usuario -->
    <div style="margin-bottom: 1rem;">
      <label for="username">Usuario:</label>
      <input 
        id="username" 
        type="text" 
        formControlName="username" 
        style="width: 100%; padding: 0.5rem;"
      />
    </div>

    <!-- Campo Contraseña -->
    <div style="margin-bottom: 1rem;">
      <label for="password">Contraseña:</label>
      <input 
        id="password" 
        type="password" 
        formControlName="password" 
        style="width: 100%; padding: 0.5rem;"
      />
    </div>

    <!-- Mensaje de Error (Feedback al usuario) -->
    @if (errorMessage) {
      <div style="background-color: #ffebee; color: #c62828; padding: 10px; margin-bottom: 10px; border-radius: 4px;">
        {{ errorMessage }}
      </div>
    }

    <!-- Botón -->
    <button 
      type="submit" 
      [disabled]="loginForm.invalid"
      style="width: 100%; padding: 0.75rem; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">
      Entrar
    </button>

  </form>
</div>
```

---

## PASO 5: Proteger Rutas con Guards
Para que nadie entre a `/noticias` sin estar logueado.

**Archivo**: `src/app/core/guards/auth.guard.ts`

```typescript
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    // Si tiene token, le dejamos pasar
    return true;
  } else {
    // Si no, le mandamos al login
    router.navigate(['/login']);
    return false;
  }
};
```

**Aplicarlo en Rutas**: `src/app/app.routes.ts`

```typescript
import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { authGuard } from './core/guards/auth.guard';
// Importa tus otros componentes...

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { 
    path: 'noticias', 
    component: NoticiasListComponent, // Tu componente de lista
    canActivate: [authGuard] // <--- ¡AQUI PROTEGEMOS!
  },
  // ... otras rutas
];
```

---

# ⚠️ Solución de Errores Comunes

### Error 1: "CORS error" o "Access-Control-Allow-Origin"
*   **Síntoma**: El login falla inmediatamente y la consola muestra un error rojo de CORS.
*   **Causa**: El backend Java no está permitiendo conexiones desde Angular.
*   **Solución**:
    *   Ve a tu Java: `SecurityConfig.java` o `AuthController.java`.
    *   Asegúrate de que tienes `@CrossOrigin(origins = "http://localhost:4200")` puesto.

### Error 2: "403 Forbidden" al intentar listar noticias
*   **Síntoma**: Haces login bien, pero al ir a la lista de noticias no sale nada y da error 403.
*   **Causa**: El token no está llegando.
*   **Solución**:
    *   Revisa `app.config.ts`. ¿Registraste `provideHttpClient(withInterceptors([authInterceptor]))`?
    *   Revisa `auth.interceptor.ts`. Pon un `console.log('Interceptando!', token)` dentro para ver si se ejecuta.

### Error 3: Login infinito / Bucle
*   **Síntoma**: Entras al login, pones datos, y te vuelve a mandar al login.
*   **Causa**: El Guard te está bloqueando porque `isAuthenticated()` retorna falso.
*   **Solución**: Revisa que `localStorage.setItem` se esté ejecutando en el `AuthService`.
