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
package com.codingrodent.microservice.template.service.impl;

import com.codingrodent.microservice.template.entity.FortuneEntity;
import com.codingrodent.microservice.template.model.*;
import com.codingrodent.microservice.template.repository.api.IASyncFortuneRepository;
import com.codingrodent.microservice.template.service.api.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.inject.Inject;
import java.util.*;

import static com.codingrodent.microservice.template.converter.Converter.toFortuneModel;

/**
 * Fortune service implementation - Async
 */
@Service
public class AsyncFortuneService implements IAsyncFortuneService<Fortune> {

    private final IASyncFortuneRepository<FortuneEntity> repository;
    private final ILogger logger;

    @Inject
    public AsyncFortuneService(final ILogger logger, final IASyncFortuneRepository<FortuneEntity> repository) {
        this.repository = repository;
        this.logger = logger;
    }

    /**
     * Create an entity
     *
     * @param uuid    UUID of model object to save
     * @param fortune Model to create as an entity
     * @param version Version (if required)
     * @return Saved model observable
     */
    public Observable<ModelVersion<Fortune>> save(final UUID uuid, final Fortune fortune, final Optional<Long> version) {
        throw new UnsupportedOperationException("Save an entity not implemented");
    }

    /**
     * Create an entity
     *
     * @param fortune Model object to create
     * @param version Version (if required)
     * @return Saved model observable
     */
    @Override
    public Observable<ModelVersion<Fortune>> create(final Fortune fortune, final Optional<Long> version) {
        throw new UnsupportedOperationException("Create an entity not implemented");
    }

    /**
     * Load an entity by its key
     *
     * @param uuid Key
     * @return The entity or an empty optional observable
     */
    @Override
    public Observable<ModelVersion<Fortune>> load(final String uuid) {

        return repository.findOne(uuid).
                first().
                map(entity -> new ModelVersion<>(toFortuneModel.convert(entity), Optional.of(entity.getVersion())));
    }

    /**
     * Delete an entity by its key
     *
     * @param uuid Key
     */
    @Override
    public void delete(final String uuid) {
        throw new UnsupportedOperationException("Delete an entity not implemented");
    }

    /**
     * Get a page of entities
     *
     * @param page Page to retrieve
     * @param size Size of page
     * @return Model list observable
     */
    @Override
    public Observable<Fortune> findAll(final int page, final int size) {
        return repository.findAll(new PageRequest(page, size)).map(toFortuneModel::convert);
    }

    /**
     * Get a page of fortunes with named authors
     *
     * @param page Page to retrieve
     * @param size Size of page
     * @return Model list
     */
    @Override
    public Observable<Fortune> findNamed(final int page, final int size) {
        return repository.findAllNamed(new PageRequest(page, size)).map(toFortuneModel::convert);
    }

    /**
     * Get a page of fortunes with anonymous authors
     *
     * @param page Page to retrieve
     * @param size Size of page
     * @return Model list
     */
    @Override
    public Observable<Fortune> findAnon(final int page, final int size) {
        return repository.findAllAnon(new PageRequest(page, size)).map(toFortuneModel::convert);
    }

}
