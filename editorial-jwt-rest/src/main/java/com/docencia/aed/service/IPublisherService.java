package com.docencia.aed.service;

import com.docencia.aed.entity.Publisher;

import java.util.List;

/**
 * Interfaz para el servicio de Editoriales (Publisher).
 * Define los métodos que debe implementar la lógica de negocio.
 * Se usa para desacoplar el controlador de la implementación concreta.
 */
public interface IPublisherService {

    /**
     * Recupera todas las editoriales de la base de datos.
     * 
     * @return Lista de objetos Publisher.
     */
    List<Publisher> findAll();

    /**
     * Crea una nueva editorial.
     * 
     * @param publisher Objeto Publisher con los datos a guardar.
     * @return El objeto Publisher guardado (con ID generado).
     */
    Publisher create(Publisher publisher);

    /**
     * Actualiza una editorial existente.
     * 
     * @param id        Identificador de la editorial a actualizar.
     * @param publisher Objeto con los nuevos datos.
     * @return La editorial actualizada.
     */
    Publisher update(Long id, Publisher publisher);

    /**
     * Elimina una editorial por su ID.
     * 
     * @param id Identificador de la editorial a borrar.
     */
    void delete(Long id);
}
