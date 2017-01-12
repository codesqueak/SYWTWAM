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
package com.codingrodent.microservice.template.controller.api;

import com.codingrodent.microservice.template.model.ModelVersion;
import org.springframework.http.HttpHeaders;

import java.util.Optional;
import java.util.function.Function;

import static com.codingrodent.microservice.template.constants.SystemConstants.CONTENT_TYPE;

/**
 * Useful utility code potentially used by many REST operations
 */
public interface RESTBase<K, V> {
    /**
     * Generate ETag header from version resource version (if it exists)
     *
     * @param modelVersion Source of version information
     * @param args         Additional header keys and values
     * @return Headers with ETag set (if available) and customer values set
     */
    default HttpHeaders getETag(ModelVersion<?> modelVersion, String... args) {
        HttpHeaders headers = new HttpHeaders();
        modelVersion.getVersion().ifPresent(version -> headers.setETag("\"" + version + "\""));
        for (int p = 0; p < args.length; ) {
            headers.set(args[p++], args[p++]);
        }
        return headers;
    }

    /**
     * Generate ETag header from version resource version (if it exists)
     *
     * @return Headers with ETag set (if available)
     */
    default HttpHeaders getContent() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        return headers;
    }

    Function<String, Long> extractETag = v -> Long.parseLong(v.replace("\"", ""));

    default boolean isNotModified(Optional<String> version, Optional<ModelVersion<V>> modelVersion) {
        if (!version.isPresent())
            return false;
        if (!modelVersion.isPresent())
            return false;
        if (!modelVersion.get().getVersion().isPresent())
            return false;
        //
        String ifNoneMatch = version.get().replace("\"", "");
        String cas = modelVersion.get().getVersion().get().toString();
        return ifNoneMatch.equals(cas);
    }

}
