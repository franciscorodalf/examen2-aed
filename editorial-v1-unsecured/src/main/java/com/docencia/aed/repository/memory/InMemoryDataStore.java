package com.docencia.aed.repository.memory;

import com.docencia.aed.entity.Author;
import com.docencia.aed.entity.Book;
import com.docencia.aed.entity.Publisher;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * "BBDD" en memoria.
 *
 * Sembramos los mismos datos que data.sql para que los tests (id=1, id=2, etc.)
 * funcionen igual sin depender de H2.
 */
@Component
public class InMemoryDataStore {

    final Map<Long, Author> authors = new ConcurrentHashMap<>();
    final Map<Long, Book> books = new ConcurrentHashMap<>();
    final Map<Long, Publisher> publishers = new ConcurrentHashMap<>();

    final AtomicLong authorSeq = new AtomicLong(0);
    final AtomicLong bookSeq = new AtomicLong(0);
    final AtomicLong publisherSeq = new AtomicLong(0);

    @PostConstruct
    void seed() {
        // Autores (ids 1..)
        Author a1 = new Author(1L, "Gabriel García Márquez", "Colombia");
        Author a2 = new Author(2L, "Isabel Allende", "Chile");
        authors.put(1L, a1);
        authors.put(2L, a2);
        authorSeq.set(2);

        // Libros (ids 1..)
        Book b1 = new Book(1L, "Cien años de soledad", 1967, a1);
        Book b2 = new Book(2L, "El coronel no tiene quien le escriba", 1961, a1);
        Book b3 = new Book(3L, "La casa de los espíritus", 1982, a2);
        books.put(1L, b1);
        books.put(2L, b2);
        books.put(3L, b3);
        bookSeq.set(3);

        // Enlazar en autor.books para que /authors devuelva también sus libros (si se serializa)
        a1.getBooks().add(b1);
        a1.getBooks().add(b2);
        a2.getBooks().add(b3);

        // Editoriales (ids 1..)
        Publisher p1 = new Publisher(1L, "Editorial Sur", "Madrid");
        Publisher p2 = new Publisher(2L, "Norte Libros", "Barcelona");
        publishers.put(1L, p1);
        publishers.put(2L, p2);
        publisherSeq.set(2);
    }
}
