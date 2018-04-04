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

import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * Utility methods for handling etag encode / decode operations
 */
class Etag {

    /**
     * Generate an etag value from an entity CAS value and its URL
     *
     * @param cas Entity CAS value
     * @param url Location entity held at
     * @return A fairly unique etag
     */
    static String encodeEtag(final long cas, final String url) {
        int hash = url.hashCode();
        long v = cas ^ (hash << 32 + hash);
        ByteBuffer buffer = ByteBuffer.allocate(8).putLong(v);
        return Base64.getUrlEncoder().encodeToString(buffer.array());
    }

    /**
     * Regenerate a CAS value from an etag and its associated URL
     *
     * @param etag Etag holding encoded CAS value
     * @param url  Location entity held at
     * @return The original CAS value
     */
    static long decodeEtag(final String etag, final String url) {
        int hash = url.hashCode();
        byte[] parts = Base64.getUrlDecoder().decode(etag);
        long v = ByteBuffer.wrap(parts).getLong();
        return v ^ (hash << 32 + hash);
    }

}
