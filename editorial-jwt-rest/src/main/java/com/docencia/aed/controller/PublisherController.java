package com.docencia.aed.controller;

import com.docencia.aed.entity.Publisher;
import com.docencia.aed.service.IPublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Publishers", description = "Endpoints de editoriales")
@CrossOrigin(origins = "http://localhost:4200") // Permite peticiones desde Angular (CORS)
@RequestMapping({ "/api/v1", "/api/v2" }) // Mapea este controlador a ambas versiones de la API
public class PublisherController {

    private final IPublisherService publisherService;

    // Inyección del servicio para delegar la lógica de negocio
    public PublisherController(IPublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * Obtener todas las editoriales.
     * 
     * @GetMapping mapea solicitudes HTTP GET a este método.
     */
    @Operation(summary = "Get all publishers")
    @GetMapping("/publishers")
    public List<Publisher> getAllPublishers() {
        return publisherService.findAll();
    }

    /**
     * Crear una nueva editorial.
     * 
     * @PostMapping mapea solicitudes HTTP POST.
     * @Valid activa las validaciones (ej: @NotNull) en la entidad Publisher.
     * @RequestBody convierte el JSON del cuerpo de la petición a un objeto Java.
     */
    @Operation(summary = "Create publisher")
    @PostMapping("/publishers")
    public ResponseEntity<Publisher> createPublisher(@Valid @RequestBody Publisher publisher) {
        Publisher created = publisherService.create(publisher);
        // Devuelve código 201 (CREATED) y el objeto creado
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualizar una editorial existente.
     * 
     * @PutMapping mapea solicitudes HTTP PUT.
     *             Se recibe el ID en la URL (@PathVariable) y los datos en el body
     *             (@RequestBody).
     */
    @Operation(summary = "Update publisher")
    @PutMapping("/publishers/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @Valid @RequestBody Publisher publisher) {
        Publisher updated = publisherService.update(id, publisher);
        // Devuelve 200 OK con el objeto actualizado
        return ResponseEntity.ok(updated);
    }

    /**
     * Eliminar una editorial.
     * 
     * @DeleteMapping mapea solicitudes HTTP DELETE.
     *                Devuelve 204 No Content porque no hay cuerpo de respuesta.
     */
    @Operation(summary = "Delete publisher")
    @DeleteMapping("/publishers/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.delete(id);
        // Devuelve 204 No Content (éxito sin contenido)
        return ResponseEntity.noContent().build();
    }
}
