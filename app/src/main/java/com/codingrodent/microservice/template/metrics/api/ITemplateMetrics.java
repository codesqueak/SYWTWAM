package com.codingrodent.microservice.template.metrics.api;

public interface ITemplateMetrics {

    /**
     * Increment a named counter metric.  Create if necessary
     *
     * @param name Name of metric
     */
    void inc(final String name);

}
