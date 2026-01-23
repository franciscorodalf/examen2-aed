package com.docencia.aed.service.impl;

import com.docencia.aed.entity.Author;
import com.docencia.aed.entity.Book;
import com.docencia.aed.exception.ResourceNotFoundException;
import com.docencia.aed.repository.AuthorRepository;
import com.docencia.aed.repository.BookRepository;
import com.docencia.aed.service.IAuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements IAuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
    }

    @Override
    @Transactional
    public Author create(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public List<Book> findBooksByAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author", "id", authorId);
        }
        return bookRepository.findByAuthorId(authorId);
    }
}
