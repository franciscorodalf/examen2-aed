package com.docencia.aed.service;

import com.docencia.aed.entity.Book;

import java.util.List;

public interface IBookService {
    List<Book> findAll(Long authorId);
    Book findById(Long id);
    Book create(Long authorId, Book book);
}
