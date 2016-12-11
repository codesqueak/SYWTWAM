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

import com.codingrodent.microservice.template.entity.NameEntity;
import com.codingrodent.microservice.template.model.Name;
import com.codingrodent.microservice.template.repository.spec.*;
import com.codingrodent.microservice.template.service.spec.INameService;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.inject.Inject;
import java.util.UUID;

import static com.codingrodent.microservice.template.converter.Converter.toNameEntity;
import static com.codingrodent.microservice.template.converter.Converter.toNameModel;

/**
 * Business logic for Name information
 */
@Service
public class NameService implements INameService {

    private final INameRepository repository;
    private final IAsync<NameEntity, UUID> asyncRepository;

    @Inject
    public NameService(INameRepository repository, IAsync<NameEntity, UUID> asyncRepository) {
        this.repository = repository;
        this.asyncRepository = asyncRepository;
    }

    @Override
    public Observable<?> saveAsync(final Name model) {
        return asyncRepository.saveAsync(toNameEntity.convert(model));
    }

    @Override
    public Observable<Name> loadAsync(final UUID uuid) {
        return asyncRepository.findOneAsync(uuid).map(toNameModel::convert);
    }

    @Override
    public void save(final Name model) {
        NameEntity entity = toNameEntity.convert(model);
        repository.save(entity);
    }

    @Override
    public Name load(final UUID uuid) {
        NameEntity entity = repository.findOne(uuid);
        if (null == entity)
            return null;
        else
            return toNameModel.convert(entity);
    }
}
