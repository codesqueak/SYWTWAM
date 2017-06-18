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
import com.codingrodent.microservice.template.repository.api.ISyncFortuneRepository;
import com.codingrodent.microservice.template.service.api.*;
import com.codingrodent.microservice.template.utility.Utility;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.codingrodent.microservice.template.converter.Converter.*;

/**
 * Fortune service implementation
 */
@Service
public class FortuneService implements IFortuneService<Fortune> {

    private final ISyncFortuneRepository<FortuneEntity> repository;
    private final ILogger logger;

    @Inject
    public FortuneService(final ILogger logger, final ISyncFortuneRepository<FortuneEntity> repository) {
        this.repository = repository;
        this.logger = logger;
    }

    /**
     * Initial loader - gives us some data to demonstrate with
     *
     * @throws IOException Can't find the demo data for some reason - not good !
     */
    @PostConstruct
    public void fill() throws IOException {
        // Load default records
        InputStream s = getClass().getClassLoader().getResourceAsStream("fortune.json");
        FortuneElement[] fortunes = Utility.getObjectMapper().readValue(s, FortuneElement[].class);

        Arrays.stream(fortunes).forEach(element -> {
            if (!repository.exists(element.getKey().toString())) {
                logger.info(element.getKey().toString() + " " + element.getText());
                repository.save(new FortuneEntity(element.getKey().toString(), element.getText(), element.getAuthor().orElse("")));
            }
        });
        logger.info("Preloaded fortunes:" + fortunes.length);
    }

    /**
     * Convert list of entities to model equivalents
     *
     * @param entityList List to convert
     * @return Converted list
     */
    private List<Fortune> getFortunes(final List<FortuneEntity> entityList) {
        return entityList.stream().map(toFortuneModel::convert).collect(Collectors.toList());
    }

    /**
     * Create an entity
     *
     * @param uuid    UUID of model object to save
     * @param model   Model to create as an entity
     * @param version Version (if required)
     * @return Saved model
     */
    @Override
    public ModelVersion<Fortune> save(final String uuid, final Fortune model, Optional<Long> version) {

        FortuneEntity z = toFortuneEntity.convert(uuid, model, version);
        FortuneEntity entity = repository.save(z);
        return new ModelVersion<>(toFortuneModel.convert(entity), Optional.of(entity.getVersion()));
    }

    /**
     * Create an entity
     *
     * @param model   Model object to create
     * @param version Version (if required)
     * @return Saved model
     */
    @Override
    public ModelVersion<Fortune> create(final Fortune model, final Optional<Long> version) {
        FortuneEntity entity = repository.save(toFortuneEntity.convert(UUID.randomUUID().toString(), model, version));
        return new ModelVersion<>(toFortuneModel.convert(entity), Optional.of(entity.getVersion()));
    }

    /**
     * Load an entity by its key
     *
     * @param uuid Key
     * @return The entity or an empty optional
     */
    @Override
    public Optional<ModelVersion<Fortune>> load(final String uuid) {
        FortuneEntity entity = repository.findOne(uuid);
        if (null == entity)
            return Optional.empty();
        else
            return Optional.of(new ModelVersion<>(toFortuneModel.convert(entity), Optional.of(entity.getVersion())));
    }

    /**
     * Delete an entity by its key
     *
     * @param uuid Key
     */
    @Override
    public void delete(String uuid) {
        repository.delete(uuid);
    }

    /**
     * Get a page of fortunes
     *
     * @param page Page to retrieve
     * @param size Size of page
     * @return Fortunes
     */
    @Override
    public List<Fortune> listAll(final int page, final int size) {
        return getFortunes(repository.findAll(new PageRequest(page, size)).getContent());
    }

    // ASync Implementations

    /**
     * Get a page of fortunes with named authors
     *
     * @param page Page to retrieve
     * @param size Size of page
     * @return Fortunes
     */
    @Override
    public List<Fortune> listNamed(final int page, final int size) {
        return getFortunes(repository.findAllNamed(new PageRequest(page, size)));
    }

    /**
     * Get a page of fortunes with anonymous authors
     *
     * @param page Page to retrieve
     * @param size Size of page
     * @return Fortunes
     */
    @Override
    public List<Fortune> listAnon(final int page, final int size) {
        return getFortunes(repository.findAllAnon(new PageRequest(page, size)));
    }

}
