/*
 * MIT License
 *
 * Copyright (c) 2016
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.codingrodent.microservice.template.repository.api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.*;
import rx.Observable;

/**
 *
 */
@NoRepositoryBean
public interface IAsyncCrudRepository<T> extends Repository<T, String> {

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity Entity to save
     * @return the saved entity
     */
    Observable<T> save(T entity);

    /**
     * Saves all given entities.
     *
     * @param entities Entities to save
     * @return the saved entities
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    Observable<T> save(Iterable<T> entities);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    Observable<T> findOne(String id);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return true if an entity with the given id exists, {@literal false} otherwise
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    Observable<Boolean> exists(String id);

    /**
     * Returns all instances of the type.
     *
     * @param pageable Pagination information
     * @return all entities
     */
    Observable<T> findAll(Pageable pageable);

    /**
     * Returns all instances of the type with the given IDs.
     *
     * @param pageable Pagination information
     * @param ids      ID's of entities to read
     * @return All matching entities
     */
    Observable<T> findAll(Pageable pageable, Iterable<String> ids);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    Observable<Long> count();

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void delete(String id);

    /**
     * Deletes a given entity.
     *
     * @param entity Entity to delete
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void delete(T entity);

    /**
     * Deletes the given entities.
     *
     * @param entities Entities to delete
     * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
     */
    void delete(Iterable<? extends T> entities);

    /**
     * Deletes all entities managed by the repository.
     */
    void deleteAll();
}
