package com.docencia.aed.repository;

import com.docencia.aed.entity.Book;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de libros (sin BBDD).
 */
public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    List<Book> findByAuthorId(Long authorId);
}
