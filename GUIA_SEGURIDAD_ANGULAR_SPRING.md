
# 🛡️ Guía Maestra: Securizar API con Spring Boot y Angular (v19+)

Esta guía te explica paso a paso cómo implementar la seguridad completa. Usaremos como ejemplo la entidad **`Noticia`**, pero aplica a cualquier cosa (Libro, Autor, etc.).

---

# 1. 🧠 Concepto Clave
La seguridad funciona como un pasaporte:
1.  **Login**: Angular envía usuario/pass a Spring (`POST /api/auth/login`).
2.  **Token**: Spring valida y devuelve un **JWT** (una cadena larga de letras y números).
3.  **Almacenamiento**: Angular guarda ese token (normalmente en `localStorage`).
4.  **Uso**: Angular envía ese token en el **Header** (`Authorization: Bearer <TOKEN>`) de **TODAS** las peticiones siguientes.
5.  **Validación**: Spring revisa el token antes de dejarte entrar a los endpoints protegidos.

---

# 2. ☕ Parte Backend (Spring Boot)
*Lista de chequeo rápida. Si falta algo de esto, el frontend no funcionará.*

1.  **Dependencias**: `spring-boot-starter-security`, `jjwt-api`, `jjwt-impl`, `jjwt-jackson`.
2.  **`SecurityConfig.java`**:
    *   Debe tener `.csrf(csrf -> csrf.disable())`.
    *   Debe permitir acceso libre a `.requestMatchers("/api/auth/**").permitAll()`.
    *   Debe exigir autenticación para el resto: `.anyRequest().authenticated()`.
    *   **CORS**: Debe permitir peticiones desde `http://localhost:4200` y permitir el header `Authorization`.
3.  **`JwtAuthenticationFilter.java`**: Es el portero que lee el header `Authorization`, valida el token y mete al usuario en el contexto de seguridad.
4.  **`AuthController.java`**: Un endpoint que reciba LoginRequest y devuelva un string (el token).

---

# 3. 🅰️ Parte Frontend (Angular) - PASO A PASO

Sigue estos pasos en orden para implementar la seguridad en tu examen.

## Paso 1: Crear el Servicio de Autenticación
Este servicio maneja el login y guarda el token.

`ng generate service services/auth`

```typescript
// src/app/services/auth.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/auth'; // Ajusta esto a tu URL real
  private tokenKey = 'auth_token';

  // 1. Método de Login
  login(credentials: { username: string, password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        // Asumiendo que el backend devuelve un objeto { token: "..." }
        // Si devuelve texto plano, ajusta esto.
        this.saveToken(response.token); 
      })
    );
  }

  // 2. Guardar Token
  private saveToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  // 3. Obtener Token (Getter)
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // 4. Saber si está logueado
  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token; // Devuelve true si hay token, false si no
  }

  // 5. Logout
  logout(): void {
    localStorage.removeItem(this.tokenKey);
  }
}
```

## Paso 2: El Interceptor (¡LA PIEZA MÁGICA!) 🪄
Este archivo intercepta todas las peticiones HTTP y les pega el token automáticamente. En Angular moderno (v15+) usamos **Interceptor Funcional**.

Crea el archivo manualmente: `src/app/core/interceptors/auth.interceptor.ts`

```typescript
// src/app/core/interceptors/auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Si hay token, clonamos la petición y le añadimos el header
  if (token) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedReq);
  }

  // Si no hay token, la dejamos pasar tal cual (ej: login)
  return next(req);
};
```

## Paso 3: Registrar el Interceptor
Para que funcione, debes registrarlo en tu configuración principal (`app.config.ts` en componentes standalone).

```typescript
// src/app/app.config.ts
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
// IMPORTA ESTOS DOS:
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    // AÑADE ESTA LÍNEA MÁGICA:
    provideHttpClient(withInterceptors([authInterceptor])) 
  ]
};
```

## Paso 4: Proteger las Rutas (Guard) 🛡️
Evita que entren a `/noticias` si no están logueados.

`ng generate guard core/guards/auth` (Elige "Functional" y "CanActivate")

```typescript
// src/app/core/guards/auth.guard.ts
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true; // Pasa, amigo
  } else {
    router.navigate(['/login']); // Fuera de aquí
    return false;
  }
};
```

**Uso en `app.routes.ts`**:
```typescript
{ path: 'noticias', component: NoticiasListComponent, canActivate: [authGuard] }
```

---

# 4. 📝 Ejemplo Práctico: Servicio de Noticias
¿Cómo queda tu servicio de datos? **¡Igual que antes!** No tienes que añadir el token manualmente en cada llamada `get` o `post`, porque el interceptor ya lo hace.

```typescript
// src/app/services/noticia.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Noticia } from '../models/noticia.model';

@Injectable({ providedIn: 'root' })
export class NoticiaService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/v2/noticias'; // Ojo: v2 es la segura

  getAll(): Observable<Noticia[]> {
    return this.http.get<Noticia[]>(this.apiUrl);
    // ¡El interceptor añade Authorization header aquí solo!
  }

  create(noticia: Noticia): Observable<Noticia> {
    return this.http.post<Noticia>(this.apiUrl, noticia);
  }
  
  // ... update, delete igual
}
```

---

# 5. 💡 Consejos de Emergencia para el Examen

1.  **Si falla el Login**: Hardcodea el token.
    *   Haz el login en Postman.
    *   Copia el token.
    *   En `AuthService`, haz que `getToken()` devuelva siempre ese string fijo: `return "eyJhGciOi..."`.
    *   ¡Así podrás seguir programando el resto de la app aunque el login falle!

2.  **Si falla CORS**:
    *   Asegúrate de tener `@CrossOrigin(origins = "http://localhost:4200")` en tu Controller de Java.
    *   O mejor, configura el `CorsConfigurationSource` global en `SecurityConfig`.

3.  **Plan B (Versión insegura)**:
    *   Si no consigues que autentique, cambia la URL en tu servicio Angular de `/api/v2/noticias` a `/api/v1/noticias` (si la tienes implementada). Perderás puntos de seguridad, pero ganarás todos los puntos de funcionalidad CRUD.
