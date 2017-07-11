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
import com.codingrodent.microservice.template.repository.api.ISyncFortuneRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * In memory version of repository for when Couchbase is unavailable in the environment - minimal implementation of required methods
 */
@Profile({"test", "integration", "aws"})
@Service
public class FortuneInMemoryRepository extends InMemoryRepository<FortuneEntity> implements ISyncFortuneRepository<FortuneEntity> {

    public FortuneInMemoryRepository() {
        super();
    }

    @Override
    FortuneEntity copy(final FortuneEntity original) {
        return new FortuneEntity(original.getId(), original.getText(), original.getAuthor());
    }

    @Override
    public List<FortuneEntity> findAllNamed(final Pageable pageable) {
        List<FortuneEntity> list = getStore().keySet().
                stream().
                map(getStore()::get).
                filter(model -> !"".equals(model.getAuthor())).
                collect(Collectors.toList());
        return getPage(pageable.getPageNumber(), pageable.getPageSize(), list).getContent();
    }

    @Override
    public List<FortuneEntity> findAllAnon(final Pageable pageable) {
        List<FortuneEntity> list = getStore().keySet().stream().map(getStore()::get).filter(model -> model.getAuthor().equals("")).collect(Collectors.toList());
        return getPage(pageable.getPageNumber(), pageable.getPageSize(), list).getContent();
    }

}
