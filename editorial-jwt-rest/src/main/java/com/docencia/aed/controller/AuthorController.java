package com.docencia.aed.controller;

import com.docencia.aed.entity.Author;
import com.docencia.aed.entity.Book;
import com.docencia.aed.service.IAuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Authors", description = "Endpoints de autores")
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping({"/api/v1", "/api/v2"})
public class AuthorController {

    private final IAuthorService authorService;

    public AuthorController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(summary = "Get all authors")
    @GetMapping("/authors")
    public List<Author> getAllAuthors() {
        return authorService.findAll();
    }

    @Operation(summary = "Get author by ID")
    @GetMapping("/authors/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @Operation(summary = "Create author")
    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        Author created = authorService.create(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get books by author")
    @GetMapping("/authors/{id}/books")
    public List<Book> getBooksByAuthor(@PathVariable("id") Long authorId) {
        return authorService.findBooksByAuthor(authorId);
    }
}
