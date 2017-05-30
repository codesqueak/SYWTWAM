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
package com.codingrodent.microservice.template.repository.impl;

import com.codingrodent.microservice.template.entity.EntityBase;
import com.codingrodent.microservice.template.repository.api.IAsyncCrudRepository;
import com.codingrodent.microservice.template.utility.Utility;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.JsonObject;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import rx.Observable;

import java.io.IOException;
import java.util.function.Function;

/**
 * Simple example async repository
 */

public abstract class AsyncRepository<T extends EntityBase> implements IAsyncCrudRepository<T> {

    protected abstract Bucket getBucket();

    protected abstract Function<Document, T> getDocToEntity();

    @Override
    public Observable<T> save(final T entity) {
        JsonObject jsonEntity;
        try {
            jsonEntity = JsonObject.fromJson(Utility.getObjectMapper().writeValueAsString(entity));
            JsonDocument jsonDocument = JsonDocument.create(entity.getId(), jsonEntity);
            return getBucket().async().upsert(jsonDocument).map(getDocToEntity()::apply);
        } catch (IOException e) {
            throw new InvalidDataAccessResourceUsageException("Entity serialization failed", e);
        }
    }

    @Override
    public Observable<T> save(final Iterable<T> entities) {
        return Observable.from(entities).flatMap(this::save);
    }

    @Override
    public Observable<T> findOne(final String id) {
        return getBucket().async().get(id).map(getDocToEntity()::apply);
    }

    @Override
    public Observable<Boolean> exists(final String id) {
        return getBucket().async().exists(id);
    }

    @Override
    public Observable<T> findAll() {
        return null;
    }

    @Override
    public Observable<T> findAll(final Iterable<String> keys) {
        return Observable.from(keys).flatMap(this::findOne);
    }

    @Override
    public Observable<Long> count() {
        return null;
    }

    @Override
    public void delete(final String key) {

    }

    @Override
    public void delete(final T entity) {

    }

    @Override
    public void delete(final Iterable<? extends T> entities) {

    }

    @Override
    public void deleteAll() {

    }
}


