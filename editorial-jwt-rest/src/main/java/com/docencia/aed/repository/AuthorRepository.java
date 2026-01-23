package com.docencia.aed.repository;

import com.docencia.aed.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    List<Author> findAll();

    Optional<Author> findById(Long id);

    boolean existsById(Long id);

    Author save(Author author);
}
