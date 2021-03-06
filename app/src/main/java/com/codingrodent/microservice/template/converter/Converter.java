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
package com.codingrodent.microservice.template.converter;

import com.codingrodent.microservice.template.entity.*;
import com.codingrodent.microservice.template.exception.ApplicationFaultException;
import com.codingrodent.microservice.template.model.Fortune;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Converter functions to allow entity and model objects to be interchanged
 */
public class Converter {

    public final static IConvertToEntity<Fortune, FortuneEntity, String, Optional<Long>> toFortuneEntity = (id, m, version) -> {
        FortuneEntity entity = new FortuneEntity(id, m.getText(), m.getAuthor().orElse(""));
        setVersion(entity, version);
        return entity;
    };

    public final static IConvertToModel<FortuneEntity, Fortune> toFortuneModel = entity -> new Fortune(entity.getText(), Optional.of(entity.getAuthor()), Optional.of(UUID.fromString(entity.getId())));

    private Converter() {
        // Do not instantiate.
    }

    private static void setVersion(final EntityBase entity, Optional<Long> version) {
        version.ifPresent(v -> {
            try {
                Field versionField = EntityBase.class.getDeclaredField("version");
                versionField.setAccessible(true);
                versionField.set(entity, v);
                versionField.setAccessible(false);
            } catch (NoSuchFieldException e) {
                throw new ApplicationFaultException("Unable to gain access to the FortuneEntity version field");
            } catch (IllegalAccessException e) {
                throw new ApplicationFaultException("Unable to update version field");
            }
        });
    }

}
