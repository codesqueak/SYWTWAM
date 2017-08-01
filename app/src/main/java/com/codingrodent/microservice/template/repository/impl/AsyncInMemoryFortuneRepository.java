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

import com.codingrodent.microservice.template.entity.FortuneEntity;
import com.codingrodent.microservice.template.repository.api.*;
import com.couchbase.client.java.Bucket;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.inject.Inject;

/**
 * In memory version of repository for when Couchbase is unavailable in the environment - minimal implementation of required methods
 */
@Profile({"test", "integration", "aws"})
@Service
public class AsyncInMemoryFortuneRepository extends AsyncRepository<FortuneEntity> implements IAsyncFortuneRepository<FortuneEntity> {

    private final ISyncFortuneRepository<FortuneEntity> repository;

    @Inject
    public AsyncInMemoryFortuneRepository(ISyncFortuneRepository<FortuneEntity> repository) {
        this.repository = repository;
    }

    @Override
    public Observable<FortuneEntity> findAllNamed(final Pageable pageable) {
        return Observable.from(repository.findAllNamed(pageable));
    }

    @Override
    public Observable<FortuneEntity> findAllAnon(final Pageable pageable) {
        return Observable.from(repository.findAllAnon(pageable));
    }

    @Override
    public Observable<FortuneEntity> findAll(final Pageable pageable) {
        return Observable.from(repository.findAll());
    }

    @Override
    protected Bucket getBucket() {
        return null;
    }
}
