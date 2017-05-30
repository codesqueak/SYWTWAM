package com.codingrodent.microservice.template.repository.impl;

import com.codingrodent.microservice.template.entity.FortuneEntity;
import com.codingrodent.microservice.template.utility.Utility;
import com.couchbase.client.java.*;
import com.couchbase.client.java.document.Document;
import com.couchbase.client.java.document.json.JsonObject;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.function.Function;

/**
 *
 */
@Service
public class AsyncFortuneRepository extends AsyncRepository<FortuneEntity> {

    private final Cluster cluster = CouchbaseCluster.create("localhost");
    private final Bucket bucket = cluster.openBucket("template", "bucketpassword");

    private final Function<Document, FortuneEntity> docToEntity = doc -> {
        String json = doc.content().toString();
        return getFortuneEntity(json);
    };

    private final Function<JsonObject, FortuneEntity> jsonToEntity = jsonObject -> {
        String json = jsonObject.get("template").toString();
        return getFortuneEntity(json);
    };

    private FortuneEntity getFortuneEntity(final String json) {
        try {
            return Utility.getObjectMapper().readValue(json, FortuneEntity.class);
        } catch (IOException e) {
            throw new InvalidDataAccessResourceUsageException("JSON deerialization failed", e);
        }
    }

    protected Bucket getBucket() {
        return bucket;
    }

    protected Function<Document, FortuneEntity> getDocToEntity() {
        return docToEntity;
    }

    protected Function<JsonObject, FortuneEntity> getJsonToEntity() {
        return jsonToEntity;
    }

}
