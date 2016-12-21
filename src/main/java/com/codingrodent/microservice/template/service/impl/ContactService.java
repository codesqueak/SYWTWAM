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

import com.codingrodent.microservice.template.entity.ContactEntity;
import com.codingrodent.microservice.template.model.Contact;
import com.codingrodent.microservice.template.repository.api.*;
import com.codingrodent.microservice.template.service.api.IContactService;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.inject.Inject;
import java.util.UUID;

import static com.codingrodent.microservice.template.converter.Converter.toNameEntity;
import static com.codingrodent.microservice.template.converter.Converter.toNameModel;

/**
 * Business logic for Contact information
 */
@Service
public class ContactService implements IContactService {

    // Both sync and async implementations
    // Normally use only one but this is for demo purposes
    private final ISyncNameRepository repository;
    private final IAsync<ContactEntity, UUID> asyncRepository;

    @Inject
    public ContactService(ISyncNameRepository repository, IAsync<ContactEntity, UUID> asyncRepository) {
        this.repository = repository;
        this.asyncRepository = asyncRepository;
    }

    @Override
    public Observable<?> saveAsync(final UUID uuid, final Contact model) {
        return asyncRepository.saveAsync(toNameEntity.convert(uuid, model));
    }

    @Override
    public Observable<Contact> loadAsync(final UUID uuid) {
        return asyncRepository.findOneAsync(uuid).map(toNameModel::convert);
    }

    @Override
    public void save(final UUID uuid, final Contact model) {
        ContactEntity entity = toNameEntity.convert(uuid, model);
        repository.save(entity);
    }

    @Override
    public Contact load(final UUID uuid) {
        ContactEntity entity = repository.findOne(uuid);
        if (null == entity)
            return null;
        else
            return toNameModel.convert(entity);
    }
}
