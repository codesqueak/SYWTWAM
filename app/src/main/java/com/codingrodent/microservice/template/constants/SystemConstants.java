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
package com.codingrodent.microservice.template.constants;

import org.springframework.http.MediaType;

/**
 * Various application wide constants
 */
public class SystemConstants {

    // Logging
    public final static String SYSTEM_NAME = "SYSTEM";
    public final static String SUBSYSTEM_NAME = "SUBSYSTEM";
    // API Versions
    public final static String API_VERSION = "V1";
    // Encoding
    public final static String CHAR_ENCODING = "UTF-8";
    public final static String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE + ";charset=" + CHAR_ENCODING;
    // Metrics
    public final static String METRIC_VERSION_GET = "com.codingrodent.microservice.template.get";
    public final static String METRIC_VERSION_OPTIONS = "com.codingrodent.microservice.template.options";

    private SystemConstants() {
        // Never need to make an instance of this class
    }

}
