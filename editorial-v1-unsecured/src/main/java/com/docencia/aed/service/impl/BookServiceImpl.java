package com.docencia.aed.service.impl;

import com.docencia.aed.entity.Author;
import com.docencia.aed.entity.Book;
import com.docencia.aed.exception.ResourceNotFoundException;
import com.docencia.aed.repository.AuthorRepository;
import com.docencia.aed.repository.BookRepository;
import com.docencia.aed.service.IBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements IBookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Book> findAll(Long authorId) {
        if (authorId == null) {
            return bookRepository.findAll();
        }
        // Si filtra por autor, aseguramos que exista para devolver 404 coherente
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author", "id", authorId);
        }
        return bookRepository.findByAuthorId(authorId);
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    @Override
    @Transactional
    public Book create(Long authorId, Book book) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", authorId));

        book.setAuthor(author);
        Book saved = bookRepository.save(book);

        // Mantener consistencia del lado del autor en memoria
        if (author.getBooks() != null && author.getBooks().stream().noneMatch(b -> saved.getId().equals(b.getId()))) {
            author.getBooks().add(saved);
        }
        return saved;
    }
}
