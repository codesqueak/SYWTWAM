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
import com.codingrodent.microservice.template.exception.ApplicationFaultException;
import com.codingrodent.microservice.template.repository.api.ISyncFortuneRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In memory version of repository for when Couchbase is unavailable in the environment - minimal implementation of required methods
 */
@Profile({"test", "integration", "aws"})
@Service
public class FortuneInMemoryRepository implements ISyncFortuneRepository<FortuneEntity> {

    private final ConcurrentHashMap<String, FortuneEntity> store = new ConcurrentHashMap<>();
    private final Field versionField;

    public FortuneInMemoryRepository() {
        try {
            versionField = FortuneEntity.class.getDeclaredField("version");
        } catch (NoSuchFieldException e) {
            throw new ApplicationFaultException("Unable to gain access to the FortuneEntity version field");
        }
        versionField.setAccessible(true);
    }

    @Override
    public List<FortuneEntity> findAllNamed(final Pageable pageable) {
        List<FortuneEntity> l = store.keySet().stream().map(store::get).filter(model -> !model.getAuthor().equals("")).collect(Collectors.toList());
        return getFortunes(pageable.getPageNumber(), pageable.getPageSize(), l).getContent();
    }

    @Override
    public List<FortuneEntity> findAllAnon(final Pageable pageable) {
        List<FortuneEntity> l = store.keySet().stream().map(store::get).filter(model -> model.getAuthor().equals("")).collect(Collectors.toList());
        return getFortunes(pageable.getPageNumber(), pageable.getPageSize(), l).getContent();
    }

    @Override
    public Iterable<FortuneEntity> findAll(final Sort sort) {
        throw new UnsupportedOperationException("Not implemented - findAll(sort)");
    }

    @Override
    public Page<FortuneEntity> findAll(final Pageable pageable) {
        List<FortuneEntity> l = store.keySet().stream().map(store::get).collect(Collectors.toList());
        return getFortunes(pageable.getPageNumber(), pageable.getPageSize(), l);
    }

    private PageImpl<FortuneEntity> getFortunes(final int page, final int size, final List<FortuneEntity> l) {
        int start = page * size;
        if (start >= l.size())
            return new PageImpl<>(new ArrayList<>(0));
        int last = start + size;
        if (last > l.size())
            last = l.size();
        return new PageImpl<>(l.subList(start, last));
    }

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity Entity to save
     * @return the saved entity
     */
    @Override
    public <S extends FortuneEntity> S save(final S entity) {
        FortuneEntity original = store.get(entity.getId());
        long version = (null == original) ? 0L : entity.getVersion();
        original = new FortuneEntity(entity.getId(), entity.getText(), entity.getAuthor());
        // No direct access to the version
        try {
            versionField.set(original, version);
        } catch (IllegalAccessException e) {
            throw new ApplicationFaultException("Unable to update version field");
        }
        return (S) store.put(entity.getId(), original);
    }

    @Override
    public <S extends FortuneEntity> Iterable<S> save(final Iterable<S> entities) {
        throw new UnsupportedOperationException("Not implemented - Iterable");
    }

    @Override
    public FortuneEntity findOne(final String s) {
        return store.get(s);
    }

    @Override
    public boolean exists(final String s) {
        return store.containsKey(s);
    }

    @Override
    public Iterable<FortuneEntity> findAll() {
        throw new UnsupportedOperationException("Not implemented - findAll");
    }

    @Override
    public Iterable<FortuneEntity> findAll(final Iterable<String> strings) {
        throw new UnsupportedOperationException("Not implemented - finaAll(Iterable)");
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not implemented - count");
    }

    @Override
    public void delete(final String s) {
        throw new UnsupportedOperationException("Not implemented - delete(string)");
    }

    @Override
    public void delete(final FortuneEntity entity) {
        throw new UnsupportedOperationException("Not implemented - delete(entity)");
    }

    @Override
    public void delete(final Iterable<? extends FortuneEntity> entities) {
        throw new UnsupportedOperationException("Not implemented - delete(iterable)");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not implemented - deleteAll");
    }
}
