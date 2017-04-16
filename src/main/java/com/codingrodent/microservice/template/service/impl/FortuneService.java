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
import com.codingrodent.microservice.template.model.*;
import com.codingrodent.microservice.template.repository.api.*;
import com.codingrodent.microservice.template.service.api.*;
import com.codingrodent.microservice.template.utility.Utility;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.*;
import java.util.*;

import static com.codingrodent.microservice.template.converter.Converter.*;

/**
 * Business logic for Fortune information
 */
@Service
@Profile("prod")
public class FortuneService implements IFortuneService<Fortune> {

    // Both sync and async implementations
    // Normally use only one but this is for demo purposes
    private final ISyncFortuneRepository repository;
    private final IAsync<FortuneEntity, UUID> asyncRepository;
    private final ILogger logger;

    @Inject
    public FortuneService(final ILogger logger, final ISyncFortuneRepository repository, final IAsync<FortuneEntity, UUID> asyncRepository) {
        this.repository = repository;
        this.asyncRepository = asyncRepository;
        this.logger = logger;
    }

    @PostConstruct
    public void fill() {
        // Load default records
        InputStream s = getClass().getClassLoader().getResourceAsStream("fortune.json");
        FortuneElement[] fortunes = null;
        try {
            fortunes = Utility.getObjectMapper().readValue(s, FortuneElement[].class);
            for (FortuneElement element : fortunes) {
                if (!repository.exists(element.getKey().toString())) {
                    logger.info(element.getKey().toString() + " " + element.getText());
                    repository.save(new FortuneEntity(element.getKey().toString(), element.getText(), element.getAuthor().orElse("")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Preloaded fortunes:" + fortunes.length);
    }

    @Override
    public ModelVersion<Fortune> create(final Fortune fortune, final Optional<Long> version) {
        FortuneEntity entity = repository.save(toNameEntity.convert(UUID.randomUUID(), fortune, version));
        return new ModelVersion<>(toNameModel.convert(entity), Optional.ofNullable(entity.getVersion()));
    }

    @Override
    public Optional<ModelVersion<Fortune>> load(final String uuid) {
        FortuneEntity entity = repository.findOne(uuid);
        if (null == entity)
            return Optional.empty();
        else
            return Optional.of(new ModelVersion<>(toNameModel.convert(entity), Optional.ofNullable(entity.getVersion())));
    }

    @Override
    public void delete(String uuid) {
        repository.delete(uuid);
    }

    // ASync Implementations

    @Override
    public Observable<Fortune> saveAsync(final UUID uuid, final Fortune fortune) {
        return asyncRepository.saveAsync(toNameEntity.convert(uuid, fortune, Optional.empty())).map(toNameModel::convert);
    }

    @Override
    public Observable<Fortune> loadAsync(final UUID uuid) {
        return asyncRepository.findOneAsync(uuid).map(toNameModel::convert);
    }

    @Override
    public ModelVersion<Fortune> save(final UUID uuid, final Fortune fortune, Optional<Long> version) {

        FortuneEntity entity = repository.save(toNameEntity.convert(uuid, fortune, version));
        return new ModelVersion<>(toNameModel.convert(entity), Optional.ofNullable(entity.getVersion()));
    }
}
