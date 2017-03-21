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
package com.codingrodent.microservice.template.config;

import com.codahale.metrics.MetricRegistry;
import com.codingrodent.microservice.template.metrics.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.metrics.export.*;
import org.springframework.boot.actuate.metrics.reader.MetricRegistryMetricReader;
import org.springframework.context.annotation.*;

/**
 * Metrics configuration.  Basic chain of events is:
 * <p>
 * endpoint -- reader -- exporter -- writer
 */
@Configuration
public class MetricsConfig {

    /**
     * Generate a new registry for framework metrics (default)
     *
     * @return Metrics registry
     */
    @Primary
    @Bean
    public MetricRegistry getRegistryDefault() {
        return new DefaultMetricRegistry();
    }

    @Bean
    @Primary
    public MetricRegistryMetricReader metricReaderDefault(final MetricRegistry registry) {
        return new MetricRegistryMetricReader(registry);
    }

    @Bean
    @Primary
    public TemplateMetricsWriter getMetricsWriterDefault() {
        return new TemplateMetricsWriter("Default");
    }

    @Bean
    @Primary
    public Exporter exporterDefault(final MetricRegistryMetricReader reader, final TemplateMetricsWriter writer) {
        return new MetricCopyExporter(reader, writer);
    }

    // Export metrics - application specific

    /**
     * Generate a new registry to store custom metrics in
     *
     * @return Metrics registryó
     */
    @Bean("reg")
    public AppMetricsRegistry getRegistry() {
        return new AppMetricsRegistry();
    }

    /**
     * Reader for example custom metrics.  Reads from metrics put into a metrics registry
     *
     * @return Reader for the custom metrics registry
     */
    @Bean(name = "reader")
    public MetricRegistryMetricReader metricReader(final AppMetricsRegistry registry) {
        return new MetricRegistryMetricReader(registry);
    }

    @Bean(name = "writer")
    public TemplateMetricsWriter getMetricsWriter() {
        return new TemplateMetricsWriter("App");
    }

    /**
     * This class defines the mechanism for moving metrics values from reader to writer.  This may implement custom functionality such as buffereing
     *
     * @param reader Metrics source
     * @param writer Metrics target
     * @return Metrics exporter
     */
    @Bean
    public Exporter exporter(@Qualifier("reader") final MetricRegistryMetricReader reader, @Qualifier("writer") final TemplateMetricsWriter writer) {
        return new MetricCopyExporter(reader, writer);
    }
}
