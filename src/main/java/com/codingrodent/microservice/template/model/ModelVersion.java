package com.codingrodent.microservice.template.model;

import java.util.Optional;

/**
 *
 */
public class ModelVersion<M> {

    private final M model;
    private final Optional<Long> version;

    public ModelVersion(final M model, final Optional<Long> version) {
        this.model = model;
        this.version = version;
    }

    public M getModel() {
        return model;
    }

    public Optional<Long> getVersion() {
        return version;
    }

}
