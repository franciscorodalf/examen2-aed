package com.docencia.aed.service.impl;

import com.docencia.aed.entity.Publisher;
import com.docencia.aed.repository.PublisherRepository;
import com.docencia.aed.service.IPublisherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // Por defecto, las operaciones son de solo lectura para optimizar
public class PublisherServiceImpl implements IPublisherService {

    private final PublisherRepository publisherRepository;

    // Inyección de dependencias por constructor (mejor práctica que @Autowired en
    // campos)
    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * Busca todas las editoriales.
     * Al estar la clase anotada con @Transactional(readOnly = true), no hace falta
     * repetirlo aquí.
     */
    @Override
    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    /**
     * Guarda una nueva editorial.
     * 
     * @Transactional se requiere aquí para permitir escritura (readOnly = false
     *                implícito).
     */
    @Override
    @Transactional
    public Publisher create(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    /**
     * Actualiza una editorial existente.
     * 1. Busca la editorial por ID. Si no existe, lanza excepción (o devuelve null
     * según diseño, aquí usaremos RuntimeException simple).
     * 2. Actualiza los campos necesarios.
     * 3. Guarda los cambios.
     */
    @Override
    @Transactional
    public Publisher update(Long id, Publisher publisher) {
        // Buscamos la entidad existente. .orElseThrow() lanza error si no se encuentra.
        Publisher existing = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + id));

        // Actualizamos los campos. Asumimos que el objeto 'publisher' trae los datos
        // nuevos.
        // IMPORTANTE: No actualizamos el ID.
        existing.setName(publisher.getName());
        // Agrega aquí más campos si la entidad Publisher tiene más atributos (ej:
        // dirección, email, etc.)

        // .save() funciona como 'update' cuando la entidad ya tiene un ID existente en
        // la BD.
        return publisherRepository.save(existing);
    }

    /**
     * Elimina una editorial.
     * Verifica si existe antes de borrar.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new RuntimeException("Publisher not found with id: " + id);
        }
        publisherRepository.deleteById(id);
    }
}
