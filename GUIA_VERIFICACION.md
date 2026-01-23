# Guía de Verificación y Securización

Esta guía explica cómo probar el proyecto (versión 1 sin seguridad y versión 2 con seguridad) y documenta los pasos para la implementación de la seguridad.

## 1. Cómo ejecutar el proyecto

Para levantar el servidor, abre un terminal en la carpeta principal (`editorial-jwt-rest`) y ejecuta:

```bash
mvn spring-boot:run
```

El servidor iniciará en el puerto **8080**.

---

## 2. Cómo comprobar las versiones (V1 vs V2)

Este proyecto expone las mismas rutas por duplicado para efectos de evaluación:

### Versión 1 (V1) - Sin Seguridad (Máx. 8 puntos)
*   **Base URL:** `http://localhost:8080/api/v1`
*   **Seguridad:** Ninguna (`permitAll`).
*   **Prueba:** Abre tu navegador o Postman e intenta acceder directamente:
    *   GET `http://localhost:8080/api/v1/publishers`
    *   Obtendrás la lista de editoriales sin necesidad de token.
    *   También puedes probar `POST`, `PUT` y `DELETE` directamente.

### Versión 2 (V2) - Securizada (Máx. 10 puntos)
*   **Base URL:** `http://localhost:8080/api/v2`
*   **Seguridad:** Protegida con JWT (`authenticated`).
*   **Prueba:**
    1.  Intenta acceder a `http://localhost:8080/api/v2/publishers`. Deberías recibir un **403 Forbidden** (o 401 Unauthorized).
    2.  **Obtener Token:** Haz un POST a `http://localhost:8080/api/auth/login` con:
        ```json
        {
          "username": "admin",
          "password": "admin123"
        }
        ```
    3.  Copia el token devuelto (`eyJhbGci...`).
    4.  Úsalo en Postman en la pestaña **Auth** -> **Bearer Token**, o en el Header: `Authorization: Bearer <TOKEN>`.
    5.  Ahora la petición a `/api/v2/publishers` funcionará correctamente.

---

## 3. Guía paso a paso: Cómo securizar la aplicación

Si quisieras securizar una aplicación desde cero (o entender qué hice), estos son los pasos clave:

### Paso 1: Dependencias (pom.xml)
Se deben añadir las librerías de `spring-boot-starter-security` y `jjwt` (para manejar tokens JWT). Estas ya venían en el proyecto base.

### Paso 2: Configuración de Seguridad (`SecurityConfig.java`)
Este es el archivo más importante. Ubicación: `com.docencia.aed.infrastructure.security.SecurityConfig`.

Lo que hemos configurado:
1.  **Deshabilitar CSRF:** Para APIs REST se suele desactivar (`.csrf(AbstractHttpConfigurer::disable)`).
2.  **Session Stateless:** No guardamos sesión en el servidor (`SessionCreationPolicy.STATELESS`), todo depende del token.
3.  **Reglas de Autorización (`authorizeHttpRequests`):**
    *   `.requestMatchers("/api/v1/**").permitAll()`: **ABIERTO** (para la v1).
    *   `.requestMatchers("/api/auth/**").permitAll()`: **ABIERTO** (para poder hacer login).
    *   `.anyRequest().authenticated()`: **CERRADO** (todo lo demás, incluyendo `/api/v2`, requiere login).
4.  **Filtro JWT:** Añadimos `jwtFilter` antes del filtro de autenticación estándar para que intercepte las peticiones y valide el token.

### Paso 3: El Controlador (`PublisherController.java`)
El controlador debe responder a ambas rutas.
*   Anotación clave: `@RequestMapping({"/api/v1", "/api/v2"})`
*   Esto hace que el mismo código sirva para la versión abierta y la cerrada. La distinción la hace `SecurityConfig` según la URL entrante.

### Paso 4: Implementar un repositorio real (Fix realizado)
Para que el login y los datos funcionen bien, el repositorio (`InMemoryPublisherRepository`) debía implementar métodos como `findById` (para buscar usuarios/elementos).
*   **Problema detectado:** Faltaban métodos en `PublisherRepository`.
*   **Solución:** Se añadieron `findById`, `existsById` y `deleteById` en la interfaz y su implementación en memoria.

---

## 4. Resumen
*   **Carpeta `editorial-jwt-rest`:** Proyecto completo con V1 (abierta) y V2 (cerrada). **Nota esperada: 10**.
*   **Carpeta `editorial-v1-unsecured`:** Copia del proyecto donde **todo** está abierto (configuración global `permitAll`). **Nota esperada: 8**.
