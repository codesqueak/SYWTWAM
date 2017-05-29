package com.codingrodent.microservice.template.repository.impl;

import com.codingrodent.microservice.template.entity.FortuneEntity;
import com.codingrodent.microservice.template.utility.Utility;
import com.couchbase.client.java.*;
import com.couchbase.client.java.document.Document;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

/**
 *
 */
public class FortuneAsyncRepository extends AsyncRepository<FortuneEntity, UUID> {

    private final Cluster cluster = CouchbaseCluster.create("localhost");
    private final Bucket bucket = cluster.openBucket("template", "bucketpassword");

    private final Function<Document, FortuneEntity> docToEntity = doc -> {
        String json = doc.content().toString();
        try {
            return Utility.getObjectMapper().readValue(json, FortuneEntity.class);
        } catch (IOException e) {
            throw new InvalidDataAccessResourceUsageException("JSON deerialization failed", e);
        }
    };

    protected Bucket getBucket() {
        return bucket;
    }

    protected Function<Document, FortuneEntity> getDocToEntity() {
        return docToEntity;
    }

}
