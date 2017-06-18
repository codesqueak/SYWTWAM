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
import com.codingrodent.microservice.template.repository.api.IASyncFortuneRepository;
import com.couchbase.client.java.*;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Manually built async repository - Sprint Data can't auto build this at the moment
 * <p>
 * Repository to be used when Couchbase is  present and selected
 */

@Profile({"prod"})
@Service
public class AsyncFortuneRepository extends AsyncRepository<FortuneEntity> implements IASyncFortuneRepository<FortuneEntity> {

    private final Cluster cluster = CouchbaseCluster.create("localhost");
    private final Bucket bucket = cluster.openBucket("template", "bucketpassword");

    protected Bucket getBucket() {
        return bucket;
    }

    @Override
    public Observable<FortuneEntity> findAllNamed(final Pageable pageable) {
        return findByView(VIEW_NAMED).skip(pageable.getPageNumber() * pageable.getPageSize()).take(pageable.getPageSize());
    }

    @Override
    public Observable<FortuneEntity> findAllAnon(final Pageable pageable) {
        return findByView(VIEW_ANON).skip(pageable.getPageNumber() * pageable.getPageSize()).take(pageable.getPageSize());
    }

}
