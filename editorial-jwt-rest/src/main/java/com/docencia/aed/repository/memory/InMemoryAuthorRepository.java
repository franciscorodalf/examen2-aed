package com.docencia.aed.repository.memory;

import com.docencia.aed.entity.Author;
import com.docencia.aed.repository.AuthorRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryAuthorRepository implements AuthorRepository {

    private final InMemoryDataStore store;

    public InMemoryAuthorRepository(InMemoryDataStore store) {
        this.store = store;
    }

    @Override
    public List<Author> findAll() {
        List<Author> list = new ArrayList<>(store.authors.values());
        list.sort(Comparator.comparing(Author::getId));
        return list;
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(store.authors.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return store.authors.containsKey(id);
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            author.setId(store.authorSeq.incrementAndGet());
        } else {
            store.authorSeq.updateAndGet(prev -> Math.max(prev, author.getId()));
        }

        // Asegurar lista no nula
        if (author.getBooks() == null) {
            author.setBooks(new java.util.ArrayList<>());
        }

        store.authors.put(author.getId(), author);
        return author;
    }
}
