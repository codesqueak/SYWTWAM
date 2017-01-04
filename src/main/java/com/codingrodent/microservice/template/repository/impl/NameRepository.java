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

import com.codingrodent.microservice.template.entity.ContactEntity;
import com.codingrodent.microservice.template.repository.api.IAsync;
import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

/**
 * Simple example repository
 */
@Service
@Profile("prod")
public class NameRepository implements IAsync<ContactEntity, UUID> {

    private final Cluster cluster = CouchbaseCluster.create("localhost");
    private final Bucket bucket = cluster.openBucket("template", "password");

    @Override
    public Observable<ContactEntity> saveAsync(final ContactEntity entity) {
        ObjectMapper mapper = new ObjectMapper();
        JsonObject name;
        try {
            name = JsonObject.fromJson(mapper.writeValueAsString(entity));
            Document document = JsonDocument.create(entity.getId().toString(), name);
            return Observable.just(document).<Document>flatMap(d -> bucket.async().upsert(d)).map(docToEntity::apply);
        } catch (IOException e) {
            throw new InvalidDataAccessResourceUsageException("Entity serialization failed", e);
        }
    }

    @Override
    public Observable<ContactEntity> findOneAsync(final UUID uuid) {
        return Observable.empty();
    }

    private final Function<Document, ContactEntity> docToEntity = doc -> {
        String json = doc.content().toString();
        ObjectMapper mapper = new ObjectMapper();
        ContactEntity zz = null;
        try {
            return mapper.readValue(json, ContactEntity.class);
        } catch (IOException e) {
            throw new InvalidDataAccessResourceUsageException("JSON deerialization failed", e);
        }
    };
}


