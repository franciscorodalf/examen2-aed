Aquí tienes dos documentos listos para copiar y pegar.

1. **`README.md`**: El documento oficial que debes entregar con tu proyecto.
2. **`GUIA_PASO_A_PASO.md`**: Tu "chuleta" o guía mental para reconstruir este proyecto desde cero en el examen.

---

### 1. Archivo `README.md` (Para tu repositorio)

Este archivo explica qué hace tu proyecto, qué tecnologías usa y cómo está estructurado. Cópialo en la raíz de tu proyecto.

```markdown
# Examen Angular - Gestor de Entidades (CRUD)

Este proyecto es una aplicación web desarrollada en **Angular** (Standalone Components) que implementa un sistema completo de gestión de contenidos (CRUD) con autenticación.

## 📋 Características Principales

1.  **Autenticación de Usuarios:**
    * Login con validación de formularios.
    * **Guards:** Protección de rutas (`/tareas`, `/tareas/nueva`) para usuarios no logueados.
    * **Interceptors:** Inyección automática del Token JWT en las cabeceras HTTP.
    
2.  **Gestión de Datos (CRUD):**
    * **Listar (GET):** Visualización de datos traídos desde una API REST.
    * **Crear (POST):** Formulario reactivo con validaciones.
    * **Editar (PUT/PATCH):** Reutilización del formulario de creación con carga de datos previos.
    * **Eliminar (DELETE):** Borrado de elementos con actualización automática de la vista.

3.  **Arquitectura y Buenas Prácticas:**
    * Uso de **Standalone Components** (sin `app.module.ts`).
    * Separación de responsabilidades: Modelos, Servicios (Mappers y API) y Vistas.
    * Manejo de estados de carga (`loading`) y errores (`errorMsg`).

## 🛠️ Stack Tecnológico

* **Framework:** Angular (v16+)
* **Lenguaje:** TypeScript
* **Estilos:** CSS3 (Diseño Flexbox y Grid sin librerías externas pesadas).
* **Http:** Angular HttpClient con RxJS (Observables).
* **Formularios:** Reactive Forms.

## 📂 Estructura del Proyecto

```text
src/app
├── guards/           # Protección de rutas (AuthGuard)
├── interceptors/     # Interceptor para Token JWT
├── models/           # Interfaces (Publisher, Auth)
├── pages/            # Vistas principales (Login, Tasks, TaskNew)
├── services/         # Lógica de negocio y llamadas HTTP
└── shared/           # Componentes reutilizables (Navbar)

```

## 🚀 Instalación y Ejecución

1. **Instalar dependencias:**
```bash
npm install

```


2. **Arrancar el servidor de desarrollo:**
```bash
npm start

```


La aplicación estará disponible en `http://localhost:4200/`.

---

*Examen Desarrollado por [Tu Nombre]*

```

---

### 2. GUÍA: ¿Cómo hacer este examen paso a paso?

Si en el examen te dan una hoja en blanco y te dicen "Haz un CRUD de Coches", sigue este **algoritmo exacto**. No improvises.

#### **FASE 1: Configuración Inicial (5 min)**
Lo primero es que la app no falle al arrancar.

1.  **Configurar Rutas (`app.routes.ts`):**
    * Define `home`, `login`, `lista` y `formulario` (para crear/editar).
    * Usa `path: '**', redirectTo: 'lista'` para evitar errores 404.
2.  **Habilitar Cliente HTTP (`app.config.ts`):**
    * Añade `provideHttpClient()` dentro de los `providers`. **Si olvidas esto, nada funcionará.**

#### **FASE 2: El Modelo de Datos (La base)**
No escribas HTML ni Servicios hasta saber QUÉ datos manejas.

1.  Crea la interfaz (ej. `coche.model.ts`).
    * *Tip:* Copia tal cual los campos que te pida el enunciado (ej: `matricula`, `modelo`, `año`).
    * Crea un tipo para "NuevoCoche" usando `Omit<Coche, 'id'>` para evitar problemas con el ID al crear.

#### **FASE 3: El Servicio (La conexión)**
Crea el servicio (`coche.service.ts`). Solo necesitas 4 funciones básicas:

1.  `getAll()` -> Retorna `Observable<Coche[]>`.
2.  `getById(id)` -> Retorna `Observable<Coche>`.
3.  `create(data)` -> Recibe el objeto sin ID, retorna `Observable`.
4.  `delete(id)` -> Recibe ID, retorna `Observable`.
    * *Clave:* Asegúrate de que la `baseUrl` apunta exactamente a donde dice el profesor (ej: `/api/v1/coches`).

#### **FASE 4: La Lista (GET y DELETE)**
Ve al componente de la lista (ej. `pages/coches/coches.ts`).

1.  **TS:**
    * Crea un array vacío: `coches: Coche[] = []`.
    * En `ngOnInit`, llama a tu servicio `.getAll().subscribe(data => this.coches = data)`.
2.  **HTML:**
    * Usa `@for (c of coches; track c.id)`.
    * **IMPORTANTE:** Muestra los datos interpolando `{{ c.modelo }}`.
    * Botón Eliminar: `(click)="borrar(c.id)"`.
    * *Lógica Borrar:* Llama al servicio `.delete(id)` y DENTRO del `.subscribe()`, vuelve a llamar a `this.loadData()` para refrescar la pantalla.

#### **FASE 5: El Formulario (POST y PUT)**
Esta es la parte más difícil. Ve a `pages/coche-new/coche-new.ts`.

1.  **TS:**
    * Inyecta `FormBuilder`.
    * Crea el `formGroup`. **Asegúrate de que los nombres de las claves (keys) sean IDÉNTICOS a tu modelo.**
        * Modelo: `modelo`, `año`. -> Form: `modelo: [''], año: ['']`.
2.  **HTML:**
    * Etiqueta `<form [formGroup]="form" (ngSubmit)="guardar()">`.
    * Inputs: `<input formControlName="modelo">`. **Revisa 3 veces que el `formControlName` coincida con el TS.**
3.  **Lógica Guardar:**
    * Si hay ID (modo edición) -> llama a `update`.
    * Si no hay ID (modo crear) -> llama a `create`.
    * En el `subscribe`, usa `router.navigate` para volver a la lista.

#### **FASE 6: Autenticación (Si la piden)**
Déjalo para el final, ya que suele bloquear el desarrollo si falla.

1.  Crea el `auth.service.ts` con `login()` y guardar token en `localStorage`.
2.  Crea el `auth.interceptor.ts` para leer ese token y meterlo en el header `Authorization`.
3.  Registra el interceptor en `app.config.ts`.

---

### 💡 Truco de Emergencia para el Examen
Si te atascas con el **Mapper** (el archivo que traduce de API a tu modelo), **bórralo**.
Haz que tu servicio devuelva `any` o la interfaz directa. Es mejor perder 0.5 puntos por "falta de arquitectura limpia" que perder 5 puntos porque la aplicación no muestra datos por un error de tipeo en el mapeo.

```