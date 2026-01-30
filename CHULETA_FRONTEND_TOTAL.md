
# 🍬 CHULETA FRONTEND TOTAL (Angular v19)
*Lo que te falta para aprobar aparte de la seguridad*

Esta guía cubre los 3 pilares que suelen fallar en el examen: **Formularios**, **Rutas** y **HTML**.

---

# 1. 📝 Formularios Reactivos (Crear y Editar)
Para hacer la página de `Create` y `Update`.

## Configuración (.ts)
```typescript
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NoticiaService } from '../../services/noticia.service';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule], // ¡IMPORTANTE!
  templateUrl: './noticia-form.component.html'
})
export class NoticiaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private noticiaService = inject(NoticiaService);
  private router = inject(Router);
  private route = inject(ActivatedRoute); // Para leer el ID de la URL

  // 1. Definir el formulario
  form = this.fb.group({
    titulo: ['', [Validators.required, Validators.minLength(5)]],
    contenido: ['', Validators.required],
    fecha: [new Date().toISOString().split('T')[0]] // Fecha de hoy por defecto
  });

  isEditMode = false;
  noticiaId: number | null = null;

  ngOnInit() {
    // 2. Detectar si es EDITAR o CREAR
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.noticiaId = parseInt(id);
      
      // Cargar datos existentes
      this.noticiaService.getById(this.noticiaId).subscribe(data => {
        this.form.patchValue(data); // Rellena el form automáticamente
      });
    }
  }

  save() {
    if (this.form.invalid) return;

    const data = this.form.value;

    if (this.isEditMode) {
      // ACTUALIZAR
      this.noticiaService.update(this.noticiaId!, data).subscribe(() => {
        this.router.navigate(['/noticias']);
      });
    } else {
      // CREAR
      this.noticiaService.create(data).subscribe(() => {
        this.router.navigate(['/noticias']);
      });
    }
  }
}
```

## Template (.html)
```html
<form [formGroup]="form" (ngSubmit)="save()">
  
  <div>
    <label>Título:</label>
    <input formControlName="titulo" type="text" />
    
    <!-- Mensaje de error -->
    @if (form.get('titulo')?.invalid && form.get('titulo')?.touched) {
      <span style="color: red;">El título es obligatorio y mín 5 letras.</span>
    }
  </div>

  <div>
    <label>Contenido:</label>
    <textarea formControlName="contenido"></textarea>
  </div>

  <!-- Botón deshabilitado si el form es inválido -->
  <button type="submit" [disabled]="form.invalid">
    {{ isEditMode ? 'Actualizar' : 'Crear' }}
  </button>
  
</form>
```

---

# 2. 🚦 Rutas y Navegación (Router)
Cómo moverte entre páginas y pasar IDs.

## `app.routes.ts`
```typescript
export const routes: Routes = [
  { path: '', redirectTo: 'noticias', pathMatch: 'full' },
  { path: 'noticias', component: NoticiasListComponent },
  { path: 'noticias/new', component: NoticiaFormComponent }, // Crear
  { path: 'noticias/:id', component: NoticiaFormComponent }, // Editar (con parámetro)
  { path: 'login', component: LoginComponent } // Login
];
```

## Navegar desde el HTML (`routerLink`)
```html
<!-- Ir a Crear -->
<a routerLink="/noticias/new">Nueva Noticia</a>

<!-- Ir a Editar (en un bucle) -->
<button [routerLink]="['/noticias', noticia.id]">Editar</button>
```

## Navegar desde el TypeScript (`Router`)
```typescript
this.router.navigate(['/noticias']);
```

---

# 3. 🎨 HTML Moderno (@for, @if)
Olvídate de `*ngIf` y `*ngFor`. Usa la nueva sintaxis de Angular 17/18/19.

## Listar elementos (@for)
```html
<table>
  <thead>
    <tr>
      <th>ID</th>
      <th>Título</th>
      <th>Acciones</th>
    </tr>
  </thead>
  <tbody>
    @for (item of items; track item.id) {
      <tr>
        <td>{{ item.id }}</td>
        <td>{{ item.titulo }}</td>
        <td>
          <button (click)="delete(item.id)">Eliminar</button>
          <button [routerLink]="['/noticias', item.id]">Editar</button>
        </td>
      </tr>
    } @empty {
      <tr><td colspan="3">No hay noticias todavía.</td></tr>
    }
  </tbody>
</table>
```

## Condicionales (@if)
```html
@if (isLoading) {
  <p>Cargando datos...</p>
} @else {
  <div class="contenido">...</div>
}
```

---

# 4. 🚨 Manejo de Errores (Feedback al usuario)
No dejes al usuario colgado si falla el servidor.

```typescript
errorMessage: string = '';

loadData() {
  this.service.getAll().subscribe({
    next: (data) => {
      this.items = data;
      this.errorMessage = '';
    },
    error: (err) => {
      console.error(err);
      this.errorMessage = 'Error al cargar datos. Intenta más tarde.';
      
      // Truco examen: Si es 403/401, redirigir a login
      if (err.status === 401) {
        this.router.navigate(['/login']);
      }
    }
  });
}
```

```html
@if (errorMessage) {
  <div class="alert alert-danger">
    {{ errorMessage }}
  </div>
}
```
