
# 🍃 CHULETA BACKEND TOTAL (Spring Boot 3)
*Para cuando te pidan: "Crea la entidad Libro" o "Añade un campo nuevo"*

Si ya tienes el Frontend y la Seguridad, solo te falta dominar el CRUD en Java.

---

# 1. 📦 La Entidad (Entity)
Representa la tabla en la base de datos.

```java
package com.docencia.aed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "libros") // Opcional, pero buena práctica
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio") // Validación
    private String titulo;

    private String autor;

    // ¡IMPORTANTE! JPA necesita constructor vacío
    public Libro() {}

    // Constructor con campos
    public Libro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    // Getters y Setters (O usa @Data de Lombok si te dejan)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
}
```

---

# 2. 🗄️ El Repositorio (Repository)
Para hablar con la base de datos. ¡Es solo una interfaz!

```java
package com.docencia.aed.repository;

import com.docencia.aed.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Aquí puedes añadir métodos mágicos si te los piden
    // Ejemplo: List<Libro> findByAutor(String autor);
}
```

---

# 3. ⚙️ El Servicio (Service)
La lógica de negocio. Es el intermediario.

```java
package com.docencia.aed.service;

import com.docencia.aed.entity.Libro;
import com.docencia.aed.repository.LibroRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository repository;

    public LibroService(LibroRepository repository) {
        this.repository = repository;
    }

    public List<Libro> findAll() {
        return repository.findAll();
    }

    public Optional<Libro> findById(Long id) {
        return repository.findById(id);
    }

    public Libro save(Libro libro) {
        return repository.save(libro);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    // Método Update (Truco para examen)
    public Libro update(Long id, Libro datosNuevos) {
        return repository.findById(id).map(existente -> {
            existente.setTitulo(datosNuevos.getTitulo());
            existente.setAutor(datosNuevos.getAutor());
            return repository.save(existente);
        }).orElse(null); // O lanza excepción si prefieres
    }
}
```

---

# 4. 🎮 El Controlador (Controller)
El que recibe las peticiones HTTP. **¡Cuidado con los códigos de estado!**

```java
package com.docencia.aed.controller;

import com.docencia.aed.entity.Libro;
import com.docencia.aed.service.LibroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/libros") // O /api/v2 si es segura
@CrossOrigin(origins = "http://localhost:4200") // ¡NO OLVIDAR!
public class LibroController {

    private final LibroService service;

    public LibroController(LibroService service) {
        this.service = service;
    }

    // 1. GET ALL (200 OK)
    @GetMapping
    public List<Libro> getAll() {
        return service.findAll();
    }

    // 2. GET BY ID (200 OK o 404 Not Found)
    @GetMapping("/{id}")
    public ResponseEntity<Libro> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(libro -> ResponseEntity.ok(libro))
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. CREATE (201 Created) ¡Muy importante el 201!
    @PostMapping
    public ResponseEntity<Libro> create(@Valid @RequestBody Libro libro) {
        Libro guardado = service.save(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // 4. UPDATE (200 OK o 404)
    @PutMapping("/{id}")
    public ResponseEntity<Libro> update(@PathVariable Long id, @Valid @RequestBody Libro libro) {
        Libro actualizado = service.update(id, libro);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // 5. DELETE (204 No Content) ¡Muy importante el 204!
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

# 5. 🚀 Configurar POSTMAN (Para probar Seguridad)

Si tienes que probar la API segura (`/api/v2/...`), sigue estos pasos en Postman:

1.  **Login**:
    *   Método: `POST`
    *   URL: `http://localhost:8080/api/auth/login`
    *   Body (Raw -> JSON):
        ```json
        {
          "username": "admin",
          "password": "admin123"
        }
        ```
    *   Dale a **Send** y copia el token que te devuelve (`eyJh...`).

2.  **Peticiones Seguras (Get/Post/Put/Delete)**:
    *   Ve a la pestaña **Auth** (o Authorization).
    *   Tipo: Selecciona **Bearer Token**.
    *   Token: Pega el chorizo de letras (`eyJh...`) ahí.
    *   ¡Dale a Send!

*Si no configuras esto, recibirás siempre un 403 Forbidden.*
