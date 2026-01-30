
# 🚨 DICCIONARIO DE ERRORES DE TERMINAL (Backend y Frontend)

Si te sale texto rojo en la terminal, busca aquí el mensaje clave.

---

# ☕ TERMINAL BACKEND (Spring Boot / Maven)

## Error 1: "Port 8080 was already in use"
**Mensaje en consola**:
`Web server failed to start. Port 8080 was already in use.`

**Causa**:
Tienes otra ejecución de Spring Boot arrancada y olvidada. Solo puede haber uno a la vez.

**Solución**:
1.  Abre una terminal nueva.
2.  Ejecuta: `lsof -i :8080` (Te dará un numerito llamado PID).
3.  Ejecuta: `kill -9 <PID>` (Sustituye <PID> por el número, ej: `kill -9 12345`).
4.  Vuelve a arrancar.

## Error 2: "BeanCreationException" / "UnsatisifiedDependencyException"
**Mensaje en consola**:
`Error creating bean with name 'publisherController': Unsatisfied dependency expressed through constructor parameter 0`

**Causa**:
Spring no encuentra la clase que intentas inyectar. Normalmente se te ha olvidado poner la anotación `@Service` o `@Repository` en esa clase.

**Solución**:
1.  Mira cuál es la clase que falla (ej: `PublisherService`).
2.  Ve a `PublisherService.java`.
3.  ¡Ponle `@Service` encima de la clase!

## Error 3: "communications link failure"
**Mensaje en consola**:
`com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure`

**Causa**:
La base de datos (Docker) está apagada.

**Solución**:
1.  Abre Docker Desktop.
2.  Asegúrate de que el contenedor de tu base de datos esté en verde (Running).

## Error 4: "403 Forbidden" (En logs de Spring)
**Mensaje en consola**:
No suele salir error rojo, pero ves que llega una petición y Spring responde 403.

**Causa**:
El usuario no tiene permiso o el token JWT es inválido/falta.

**Solución**:
1.  Si es un endpoint público, revisa `SecurityConfig.java` y asegúrate de que tiene `.permitAll()`.
2.  Si requiere login, asegúrate de enviar el Header `Authorization: Bearer ...`.

---

# 🅰️ TERMINAL FRONTEND (Angular / NPM)

## Error 5: "Address already in use :::4200"
**Mensaje en consola**:
`Port 4200 is already in use.`

**Causa**:
Tienes otro `ng serve` corriendo en otra terminal.

**Solución**:
*   Opción A: Busca la terminal y ciérrala con `Ctrl + C`.
*   Opción B: Dile que "Yes" cuando te pregunte si quieres usar otro puerto (ej: 56789). **OJO**: Si haces esto, tendrás que cambiar el CORS en Java para aceptar ese puerto nuevo. **Recomiendo Opción A.**

## Error 6: "NG8001: 'app-listado' is not a known element"
**Mensaje en consola**:
`NG8001: 'app-noticias-list' is not a known element`

**Causa**:
Estás intentando usar la etiqueta `<app-noticias-list>` dentro de otro componente, pero no lo has importado.

**Solución**:
1.  Ve al archivo `.ts` del componente DONDE estás usando la etiqueta (el padre).
2.  En el array `imports: []`, añade `NoticiasListComponent`.

## Error 7: "NullInjectorError: No provider for HttpClient!"
**Mensaje en consola**:
`NullInjectorError: R3InjectorError(Standalone[_AppComponent])[AuthService -> HttpClient -> HttpClient]:`

**Causa**:
Has intentado usar `http` pero no has configurado la app para ello.

**Solución**:
1.  Ve a `app.config.ts`.
2.  Añade `provideHttpClient()` en la lista de providers.

## Error 8: "Property '...' has no initializer"
**Mensaje en consola**:
`Property 'titulo' has no initializer and is not definitely assigned in the constructor.`

**Causa**:
TypeScript estricto. Has definido `titulo: string;` pero no le has dado valor.

**Solución rápida**:
Ponle un signo de exclamación o un valor inicial.
*   `titulo!: string;` (Le prometes que tendrá valor).
*   `titulo: string = '';` (Le das valor vacío).

## Error 9: "Cannot find module..."
**Mensaje en consola**:
`Error: Cannot find module '@angular/core'` o similar.

**Causa**:
La carpeta `node_modules` está corrupta o incompleta.

**Solución**:
1.  `rm -rf node_modules package-lock.json`
2.  `npm install`
(Esto tarda un poco pero lo arregla todo).

## Error 10: Error de compilación en HTML
**Mensaje en consola**:
`src/app/app.component.html:5:10 - error NG8002: Can't bind to 'ngModel' since it isn't a known property of 'input'.`

**Causa**:
Estás usando `[(ngModel)]` en un input pero te falta importar el módulo de formularios.

**Solución**:
1.  Ve al `.ts` de ese componente.
2.  En `imports: []`, añade `FormsModule`.
