package com.docencia.aed.repository.memory;

import com.docencia.aed.entity.Book;
import com.docencia.aed.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final InMemoryDataStore store;

    public InMemoryBookRepository(InMemoryDataStore store) {
        this.store = store;
    }

    @Override
    public List<Book> findAll() {
        List<Book> list = new ArrayList<>(store.books.values());
        list.sort(Comparator.comparing(Book::getId));
        return list;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(store.books.get(id));
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(store.bookSeq.incrementAndGet());
        } else {
            store.bookSeq.updateAndGet(prev -> Math.max(prev, book.getId()));
        }

        store.books.put(book.getId(), book);
        return book;
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) {
        return store.books.values().stream()
                .filter(b -> b.getAuthor() != null && authorId.equals(b.getAuthor().getId()))
                .sorted(Comparator.comparing(Book::getId))
                .collect(Collectors.toList());
    }
}
