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

import org.springframework.data.couchbase.core.query.View;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.*;

import java.io.Serializable;
import java.util.List;

/**
 * Additional sync repository access methods based on Couchbase views
 */
@NoRepositoryBean
public interface ISyncFortuneRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

    /**
     * Returns a {@link List} of entities meeting the paging restriction provided in the {@code Pageable} object.
     * <p>
     * This returns entities from the 'named' view
     *
     * @param pageable Pagination information
     * @return a list of entities
     */
    @View(viewName = "named")
    List<T> findAllNamed(Pageable pageable);

    /**
     * Returns a {@link List} of entities meeting the paging restriction provided in the {@code Pageable} object.
     * <p>
     * This returns entities from the 'anon' view
     *
     * @param pageable Pagination information
     * @return a list of entities
     */
    @View(viewName = "anon")
    List<T> findAllAnon(Pageable pageable);
}
