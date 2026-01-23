package com.docencia.aed.repository.memory;

import com.docencia.aed.entity.Publisher;
import com.docencia.aed.repository.PublisherRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class InMemoryPublisherRepository implements PublisherRepository {

    private final InMemoryDataStore store;

    public InMemoryPublisherRepository(InMemoryDataStore store) {
        this.store = store;
    }

    @Override
    public List<Publisher> findAll() {
        List<Publisher> list = new ArrayList<>(store.publishers.values());
        list.sort(Comparator.comparing(Publisher::getId));
        return list;
    }

    @Override
    public Publisher save(Publisher publisher) {
        if (publisher.getId() == null) {
            publisher.setId(store.publisherSeq.incrementAndGet());
        } else {
            store.publisherSeq.updateAndGet(prev -> Math.max(prev, publisher.getId()));
        }

        store.publishers.put(publisher.getId(), publisher);
        return publisher;
    }

    @Override
    public java.util.Optional<Publisher> findById(Long id) {
        return java.util.Optional.ofNullable(store.publishers.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return store.publishers.containsKey(id);
    }

    @Override
    public void deleteById(Long id) {
        store.publishers.remove(id);
    }
}
