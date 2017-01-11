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
package com.codingrodent.microservice.template;

import com.codingrodent.microservice.template.config.advice.RestAdvice;
import com.codingrodent.microservice.template.constants.SystemConstants;
import com.codingrodent.microservice.template.controller.api.IREST;
import com.codingrodent.microservice.template.model.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static com.codingrodent.microservice.template.constants.SystemConstants.CHAR_ENCODING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Common code for controller tests
 */
public abstract class BaseMVCTests {

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    protected final static String BAD_UUID = "abc-123";
    protected final ObjectMapper mapper = new ObjectMapper();

    /**
     * Set logging system and subsystem names (Used in logback)
     */
    @BeforeClass
    public static void initialize() {
        System.setProperty(SystemConstants.SYSTEM_NAME, "Unit_Test");
        System.setProperty(SystemConstants.SUBSYSTEM_NAME, "Controller");
    }

    /**
     * Generate a default mock MVC environment for a controller
     *
     * @param controller The controller under test
     * @return The mocked environment
     */
    protected MockMvc getMockMvc(IREST<UUID, Contact> controller) {
        return MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestAdvice()).setMessageConverters(mappingJackson2HttpMessageConverter).build();
    }

    /**
     * Execute a test GET request
     *
     * @param controller  Controller under test
     * @param urlTemplate URL to be called
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performGet(IREST<UUID, Contact> controller, String urlTemplate, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = get(urlTemplate, urlVars)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(CHAR_ENCODING);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    /**
     * Execute a test HEAD request
     *
     * @param controller  Controller under test
     * @param urlTemplate URL to be called
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performHead(IREST<UUID, Contact> controller, String urlTemplate, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = head(urlTemplate, urlVars)
                .characterEncoding(CHAR_ENCODING);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    /**
     * Execute a test PUT request
     *
     * @param controller  Controller under test
     * @param urlTemplate URL to be called
     * @param eTag        eTag value (May be null)
     * @param bodyJson    Body (May be null for tests)
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performPut(IREST<UUID, Contact> controller, String urlTemplate, String eTag, String bodyJson, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = put(urlTemplate, urlVars)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(CHAR_ENCODING);
        if (null != eTag)
            builder.header(HttpHeaders.ETAG, eTag);
        if (null != bodyJson)
            builder.content(bodyJson);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    /**
     * Execute a test POST request
     *
     * @param controller  Controller under test
     * @param urlTemplate URL to be called
     * @param eTag        eTag value (May be null)
     * @param bodyJson    Body (May be null for tests)
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performPost(IREST<UUID, Contact> controller, String urlTemplate, String eTag, String bodyJson, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = post(urlTemplate, urlVars)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(CHAR_ENCODING);
        if (null != eTag)
            builder.header(HttpHeaders.ETAG, eTag);
        if (null != bodyJson)
            builder.content(bodyJson);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }
    /**
     * Execute a test DELETE request
     *
     * @param controller  Controller under test
     * @param urlTemplate URL to be called
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performDelete(IREST<UUID, Contact> controller, String urlTemplate, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = delete(urlTemplate, urlVars)
                .characterEncoding(CHAR_ENCODING);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    /**
     * Execute a test OPTIONS request
     *
     * @param controller  Controller under test
     * @param urlTemplate URL to be called
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performOptions(IREST<UUID, Contact> controller, String urlTemplate, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = options(urlTemplate, urlVars)
                .characterEncoding(CHAR_ENCODING);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }
}
