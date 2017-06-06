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
import com.codingrodent.microservice.template.model.Fortune;
import com.codingrodent.microservice.template.repository.api.IASyncFortuneRepository;
import com.codingrodent.microservice.template.service.api.*;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.inject.Inject;
import java.util.UUID;

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

    @Override
    public Observable<Fortune> saveAsync(final UUID uuid, final Fortune fortune) {
        throw new UnsupportedOperationException("Get an entity not implemented");
    }

    @Override
    public Observable<Fortune> loadAsync(final UUID uuid) {
        throw new UnsupportedOperationException("Get an entity not implemented");
    }

    @Override
    public Observable<Fortune> findAllAsync() {
        return repository.findAll().map(toFortuneModel::convert);
    }

    @Override
    public Observable<Fortune> findAnonAsync() {
        return repository.findAllAnon(null).map(toFortuneModel::convert);
    }

    @Override
    public Observable<Fortune> findNamedAsync() {
        return repository.findAllNamed(null).map(toFortuneModel::convert);
    }
}
