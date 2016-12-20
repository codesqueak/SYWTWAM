package com.codingrodent.microservice.template.repository.api;

import rx.Observable;

import java.io.Serializable;

/**
 *
 */
public interface IAsync<T, ID extends Serializable> {

    /**
     * Saves a given entity
     *
     * @param entity Entity to be saved
     * @return the saved entity
     */
    Observable<?> saveAsync(T entity);

    /**
     * Retrieves an entity
     *
     * @param id must not be null
     * @return the entity with the given id
     */
    Observable<T> findOneAsync(ID id);

}
