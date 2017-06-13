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
package com.codingrodent.microservice.template.controller;

import com.codingrodent.microservice.template.model.ModelVersion;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Optional;
import java.util.function.Function;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * Shared base functionality for Sync Services
 */
abstract class RestBase<V> {

    private final static String ETAG_WILDCARD = "\"*\"";

    /**
     * Evaluate If-Match
     *
     * @param etag         ETag supplied in the If-Match header
     * @param modelVersion Resource entity being requested
     * @return Matching status
     */
    boolean ifMatch(final Optional<String> etag, final Optional<ModelVersion<V>> modelVersion) {
        // basic check - must have a field
        if (!etag.isPresent())
            return false;
        // if (field=='*') and (record does not exist) return false;
        if (etag.get().equals(ETAG_WILDCARD) && !modelVersion.isPresent())
            return false;
        // if (field doesn't match record) return false;
        if (!modelVersion.isPresent() || !modelVersion.get().getVersion().isPresent())
            return false;
        // See if supplied etag matchches generated etag
        return generateEtag(modelVersion).equals(etag.get());
    }

    /**
     * Evaluate If-None-Match
     *
     * @param etag         ETag supplied in the If-None-Match header
     * @param modelVersion Resource entity being requested
     * @return Matching status
     */
    boolean ifNoneMatch(final Optional<String> etag, final Optional<ModelVersion<V>> modelVersion) {
        // basic checks - must have a field and a record to match
        if (!etag.isPresent() || (!modelVersion.isPresent()))
            return true; // can never match
        // if (field=='*') and (record exists) return false;
        if (etag.get().equals(ETAG_WILDCARD))
            return false;
        // if (field matches record) return false;
        return !generateEtag(modelVersion).equals(etag.get());
    }

    /**
     * Generate ETag header from version resource (if it exists) and then add any additionally defined headers
     *
     * @param modelVersion      Source of version information
     * @param additionalHeaders Additional header keys and values
     * @return Headers with ETag set (if available) and customer values set
     */
    HttpHeaders getETagAndHeaders(final ModelVersion<?> modelVersion, String... additionalHeaders) {
        HttpHeaders headers = new HttpHeaders();
        // Add etag
        modelVersion.getVersion().ifPresent(cas -> headers.setETag(makeETag.apply(cas)));
        // Add any others defined (args - key/value/key/value...key/value)
        for (int p = 0; p < additionalHeaders.length; ) {
            headers.set(additionalHeaders[p++], additionalHeaders[p++]);
        }
        return headers;
    }

    /**
     * Generate an etag from CAS in the version objects and the active URL
     *
     * @param modelVersion Version object
     * @return etag or empty string no version value present
     */
    private String generateEtag(final Optional<ModelVersion<V>> modelVersion) {
        return modelVersion.get().getVersion().map(makeETag).orElse("");
    }

    /**
     * Get the URL for the present request
     *
     * @return Request URL
     */
    private String getURL() {
        return RequestContextHolder.currentRequestAttributes().getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, SCOPE_REQUEST).toString();
    }

    /**
     * Extract a strong etag value from its quotation marks
     */
    final Function<String, Long> extractCAS = headerValue -> {
        String etag = headerValue.replace("\"", "");
        if (etag.length() == 12)
            return Etag.decodeEtag(etag, getURL());
        return null;
    };

    /**
     * Generate a quoted strong etag from a supplied CAS value and the URL attached to the thread
     */
    private final Function<Long, String> makeETag = cas -> "\"" + Etag.encodEtag(cas, getURL()) + "\"";

}
