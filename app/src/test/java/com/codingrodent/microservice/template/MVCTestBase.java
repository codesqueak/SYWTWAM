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

import com.codingrodent.microservice.template.api.IREST;
import com.codingrodent.microservice.template.config.advice.RestAdvice;
import com.codingrodent.microservice.template.constants.SystemConstants;
import com.codingrodent.microservice.template.model.Fortune;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static com.codingrodent.microservice.template.constants.SystemConstants.CHAR_ENCODING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Common code for controller tests
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:unit_test.properties")
public abstract class MVCTestBase {

    protected final static String BAD_UUID = "abc-123";
    protected final ObjectMapper mapper = new ObjectMapper();
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    /**
     * Set logging system and subsystem names (Used in logback)
     */
    public MVCTestBase() {
        MDC.put(SystemConstants.SYSTEM_NAME, "Unit");
        MDC.put(SystemConstants.SUBSYSTEM_NAME, "Test");
    }
    /**
     * Generate a default mock MVC environment for a controller
     *
     * @param controller The controller under test
     * @return The mocked environment
     */
    private MockMvc getMockMvc(IREST<UUID, Fortune> controller) {
        return MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestAdvice()).setMessageConverters(mappingJackson2HttpMessageConverter).build();
    }

    /**
     * Execute a test GET request
     *
     * @param controller  Controller under test
     * @param urlTemplate URL to be called
     * @param ifNoneMatch eTag value (May be null)
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performGet(IREST<UUID, Fortune> controller, String urlTemplate, String ifNoneMatch, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = get(urlTemplate, urlVars)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(CHAR_ENCODING);
        if (null != ifNoneMatch)
                builder.header(HttpHeaders.IF_NONE_MATCH, ifNoneMatch);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    /**
     * Execute a test HEAD request
     *
     * @param controller  Controller under test
     * @param urlTemplate URL to be called
     * @param ifNoneMatch eTag value (May be null)
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performHead(IREST<UUID, Fortune> controller, String urlTemplate, String ifNoneMatch, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = head(urlTemplate, urlVars)
                .characterEncoding(CHAR_ENCODING);
        if (null != ifNoneMatch)
                builder.header(HttpHeaders.IF_NONE_MATCH, ifNoneMatch);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    /**
     * Execute a test PUT request
     *
     * @param controller  Controller under test
     * @param urlTemplate URL to be called
     * @param ifMatch     eTag value (May be null)
     * @param bodyJson    Body (May be null for tests)
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performPut(IREST<UUID, Fortune> controller, String urlTemplate, String ifMatch, String bodyJson, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = put(urlTemplate, urlVars)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(CHAR_ENCODING);
        if (null != ifMatch)
            builder.header(HttpHeaders.IF_MATCH, ifMatch);
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
     * @param ifMatch     eTag value (May be null)
     * @param bodyJson    Body (May be null for tests)
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performPost(IREST<UUID, Fortune> controller, String urlTemplate, String ifMatch, String bodyJson, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = post(urlTemplate, urlVars)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(CHAR_ENCODING);
        if (null != ifMatch)
            builder.header(HttpHeaders.IF_MATCH, ifMatch);
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
     * @param ifMatch     eTag value (May be null)
     * @param urlVars     Zero or more URL variables
     * @return Execution result
     * @throws Exception Thrown on any error
     */
    protected ResultActions performDelete(IREST<UUID, Fortune> controller, String urlTemplate, String ifMatch, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = delete(urlTemplate, urlVars)
                .characterEncoding(CHAR_ENCODING);
        if (null != ifMatch)
            builder.header(HttpHeaders.IF_MATCH, ifMatch);
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
    protected ResultActions performOptions(IREST<UUID, Fortune> controller, String urlTemplate, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = options(urlTemplate, urlVars)
                .characterEncoding(CHAR_ENCODING);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }
}
