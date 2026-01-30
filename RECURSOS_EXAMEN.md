
# Recursos de Estudio para el Examen

Aquí tienes una lista de referencia rápida para aprobar tu examen de AED.

## 1. Backend (Spring Boot + JWT)
*   **Anotaciones Clave**:
    *   `@RestController`: Para tus controladores.
    *   `@RequestMapping("/api/v...")`: Para versionar.
    *   `@CrossOrigin(origins = "http://localhost:4200")`: ¡Vital para que Angular conecte!
    *   `@Valid` + `@RequestBody`: Para validar datos de entrada.

*   **Seguridad**:
    *   El usuario admin es: `admin` / `admin123` (según `SecurityConfig`).
    *   Tu filtro `JwtAuthenticationFilter` intercepta peticiones.
    *   Configura `SecurityFilterChain` para permitir `/api/auth/**` y `/api/v1/**` sin login.

## 2. Frontend (Angular)
*   **Servicios**: Usa `inject(HttpClient)` en Angular moderno.
*   **Interceptores**: Son funciones (`HttpInterceptorFn`) que añaden el header `Authorization` a cada petición.
*   **Guards**: Son funciones (`CanActivateFn`) que protegen rutas como `/noticias` o `/admin`.

## 3. Consejos para el Examen
1.  **Seguridad Dual**:
    *   Si el login JWT funciona: ¡Perfecto! Usas la API v2.
    *   Si el login falla: Cambia la URL en tu servicio a `/api/v1` (la que no tiene seguridad). Perderás puntos de "securización" pero mantendrás todos los puntos de funcionalidad (casi el 80% de la nota).
    
2.  **Postman**:
    *   Usa Postman para probar el login (`POST /api/auth/login`) y obtener el token.
    *   Si Angular falla, puedes copiar ese token y pegarlo "a fuego" (hardcode) en tu `auth.service.ts` para seguir trabajando.

## 4. Ejercicios Prácticos
En la carpeta `exercises-ts/` tienes ejercicios de lógica en TypeScript totalmente resueltos para practicar sintaxis.
