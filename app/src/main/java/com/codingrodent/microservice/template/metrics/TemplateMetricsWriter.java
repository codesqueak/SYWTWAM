/*
 * MIT License
 *
 * Copyright (c) 2017
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
package com.codingrodent.microservice.template.metrics;

import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.*;

/**
 * Example metrics writer - writes to stdout
 */
public class TemplateMetricsWriter implements MetricWriter {

    private final String token;

    public TemplateMetricsWriter(final String name) {
        this.token = name;
    }

    /**
     * @see org.springframework.boot.actuate.metrics.writer.CounterWriter#increment
     */
    @Override
    public void increment(final Delta<?> delta) {
        System.out.println(token + ":" + delta.getName() + "(increment) " + delta.getValue());
    }

    /**
     * @see org.springframework.boot.actuate.metrics.writer.CounterWriter#reset
     */
    @Override
    public void reset(final String metricName) {
        System.out.println(token + ":" + "Reset metric: " + metricName);
    }

    /**
     * @see org.springframework.boot.actuate.metrics.writer.GaugeWriter#set
     */
    @Override
    public void set(final Metric<?> value) {
        System.out.println(token + ":" + value.getName() + "(set) " + value.getValue());
    }
}
