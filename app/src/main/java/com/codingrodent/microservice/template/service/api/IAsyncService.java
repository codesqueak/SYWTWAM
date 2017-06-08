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
package com.codingrodent.microservice.template.service.api;

import com.codingrodent.microservice.template.model.ModelVersion;
import rx.Observable;

import java.util.*;

/**
 * Async Service Interface - standard business logic operations
 */
public interface IAsyncService<M> {

    /**
     * Create an entity
     *
     * @param uuid    UUID of model object to save
     * @param model   Model to create as an entity
     * @param version Version (if required)
     * @return Saved model observable
     */
    Observable<ModelVersion<M>> save(UUID uuid, M model, Optional<Long> version);

    /**
     * Create an entity
     *
     * @param model   Model object to create
     * @param version Version (if required)
     * @return Saved model observable
     */
    Observable<ModelVersion<M>> create(M model, Optional<Long> version);

    /**
     * Load an entity by its key
     *
     * @param uuid Key
     * @return The entity or an empty optional observable
     */
    Observable<Optional<ModelVersion<M>>> load(String uuid);

    /**
     * Delete an entity by its key
     *
     * @param uuid Key
     */
    void delete(String uuid);

    /**
     * Get a page of entities
     *
     * @param page Page to retrieve
     * @param size Size of page
     * @return Model observable
     */
    Observable<M> findAll(int page, int size);

}
