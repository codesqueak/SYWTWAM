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

import com.codingrodent.microservice.template.model.*;
import com.codingrodent.microservice.template.service.api.IFortuneService;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.util.*;

@Service
@Profile("test")
public class FortuneServiceMock implements IFortuneService<Fortune> {

    private final Map<UUID, ModelVersion<Fortune>> data = new HashMap<>();

    @Override
    public Observable<Fortune> saveAsync(final UUID uuid, final Fortune fortune) {
        return null;
    }

    @Override
    public Observable<Fortune> loadAsync(final UUID uuid) {
        return null;
    }

    @Override
    public ModelVersion<Fortune> save(final UUID uuid, final Fortune model, final Optional<Long> version) {
        ModelVersion<Fortune> modelVersion;
        if (version.isPresent()) {
            if ((data.containsKey(uuid))) {
                if (version.get().equals(data.get(uuid).getVersion().get())) {
                    modelVersion = new ModelVersion<>(model, Optional.of(version.get() + 1));
                } else {
                    throw new OptimisticLockingFailureException("Mock - Version mismatch");
                }
            } else {
                // version supplied, no pre-existing record
                modelVersion = new ModelVersion<>(model, Optional.of(1L));
            }
        } else {
            // No version - create
            if (data.containsKey(uuid)) {
                throw new OptimisticLockingFailureException("Mock - No version supplied but record already exists");
            } else {
                modelVersion = new ModelVersion<>(model, Optional.of(1L));
            }
        }
        data.put(uuid, modelVersion);
        return modelVersion;
    }

    @Override
    public ModelVersion<Fortune> create(final Fortune fortune, final Optional<Long> version) {
        return null;
    }

    @Override
    public Optional<ModelVersion<Fortune>> load(final String uuid) {
        if (data.containsKey(uuid)) {
            return Optional.of(data.get(uuid));
        } else
            return Optional.empty();
    }

    @Override
    public void delete(final String uuid) {

    }
}
