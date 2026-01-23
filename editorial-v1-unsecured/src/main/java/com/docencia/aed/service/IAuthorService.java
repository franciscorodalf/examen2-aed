package com.docencia.aed.service;

import com.docencia.aed.entity.Author;
import com.docencia.aed.entity.Book;

import java.util.List;

public interface IAuthorService {
    List<Author> findAll();
    Author findById(Long id);
    Author create(Author author);
    List<Book> findBooksByAuthor(Long authorId);
}
