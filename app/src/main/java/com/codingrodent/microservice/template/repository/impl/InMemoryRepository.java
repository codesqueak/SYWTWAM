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
import com.codingrodent.microservice.template.exception.ApplicationFaultException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.*;

/**
 * In memory version of repository for when Couchbase is unavailable in the environment - common code
 */
abstract class InMemoryRepository<T extends EntityBase> implements PagingAndSortingRepository<T, String> {

    private final ConcurrentHashMap<String, T> store = new ConcurrentHashMap<>();
    private final Field versionField;

    InMemoryRepository() {
        try {
            versionField = EntityBase.class.getDeclaredField("version");
        } catch (NoSuchFieldException e) {
            throw new ApplicationFaultException("Unable to gain access to the version field");
        }
        versionField.setAccessible(true);
    }

    @Override
    public Page<T> findAll(final Pageable pageable) {
        List<T> list = store.keySet().stream().map(this::findOne).filter(Objects::nonNull).collect(Collectors.toList());
        return getPage(pageable.getPageNumber(), pageable.getPageSize(), list);
    }

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity Entity to save
     * @return the saved entity
     */
    @Override
    public <S extends T> S save(final S entity) {
        T original = store.get(entity.getId());
        long version = (null == original) ? 0L : entity.getVersion();
        if ((null != original) && (original.getVersion() != version))
            throw new OptimisticLockingFailureException("Original:" + original.getVersion() + " New:" + entity.getVersion());
        //
        original = copy(entity);
        // No direct access to the version
        try {
            versionField.set(original, version);
        } catch (IllegalAccessException e) {
            throw new ApplicationFaultException("Unable to update version field");
        }
        store.put(entity.getId(), original);
        return (S) original;
    }

    @Override
    public <S extends T> Iterable<S> save(final Iterable<S> entities) {
        if (null == entities)
            throw new IllegalArgumentException();
        LinkedList<S> saved = new LinkedList<>();
        entities.forEach(e -> saved.add(save(e)));
        return saved;
    }

    @Override
    public T findOne(final String s) {
        T entity = store.get(s);
        if (entity == null)
            return null;
        return copy(entity);
    }

    @Override
    public boolean exists(final String s) {
        return store.containsKey(s);
    }

    @Override
    public Iterable<T> findAll() {
        return store.keySet().stream().map(this::findOne).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public void delete(final String s) {
        if (null == s)
            throw new IllegalArgumentException();
        store.remove(s);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

    @Override
    public void delete(final T entity) {
        if (null == entity)
            throw new IllegalArgumentException();
        delete(entity.getId());
    }

    @Override
    public Iterable<T> findAll(final Iterable<String> ids) {
        if (null == ids)
            throw new IllegalArgumentException();
        return StreamSupport.stream(ids.spliterator(), false).map(this::findOne).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void delete(final Iterable<? extends T> entities) {
        if (null == entities)
            throw new IllegalArgumentException();
        entities.forEach(this::delete);
    }

    // Helper methods

    /**
     * Extractg one page from a list and return
     *
     * @param page Page to return
     * @param size Page size
     * @param l    list to extract page from
     * @return Page - may be empty if no data available at requested location
     */
    PageImpl<T> getPage(final int page, final int size, final List<T> l) {
        int start = page * size;
        if (start >= l.size())
            return new PageImpl<>(new ArrayList<>(0));
        int last = start + size;
        if (last > l.size())
            last = l.size();
        return new PageImpl<>(l.subList(start, last));
    }

    /**
     * Get the underlying data store. Available if extended operations are required
     *
     * @return Key / value store as a hash map
     */
    ConcurrentHashMap<String, T> getStore() {
        return store;
    }

    /**
     * Make a copy of the entity being stored
     *
     * @param original Oribal entity
     * @return Copy
     */
    abstract T copy(T original);

    // Not Yet Implemented

    @Override
    public Iterable<T> findAll(final Sort sort) {
        throw new UnsupportedOperationException("Not implemented - findAll");
    }

}
