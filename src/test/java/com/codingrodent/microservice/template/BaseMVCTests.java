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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 *
 */
public abstract class BaseMVCTests {

    protected final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    protected final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void initialize() {
        System.setProperty(SystemConstants.SYSTEM_NAME, "MVC Test");
        System.setProperty(SystemConstants.SUBSYSTEM_NAME, "Spring");
    }

    protected MockMvc getMockMvc(IREST<UUID, Contact> controller) {
        return MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestAdvice()).setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();
    }

    protected ResultActions performGet(IREST<UUID, Contact> controller, String urlTemplate, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = get(urlTemplate, urlVars)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8");
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    protected ResultActions performHead(IREST<UUID, Contact> controller, String urlTemplate, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = head(urlTemplate, urlVars)
                .characterEncoding("UTF-8");
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    protected ResultActions performPut(IREST<UUID, Contact> controller, String urlTemplate, String eTag, String bodyJson, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = put(urlTemplate, urlVars)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8");
        if (null != eTag)
            builder.header(HttpHeaders.ETAG, eTag);
        if (null != bodyJson)
            builder.content(bodyJson);
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    protected ResultActions performDelete(IREST<UUID, Contact> controller, String urlTemplate, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = delete(urlTemplate, urlVars)
                .characterEncoding("UTF-8");
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }

    protected ResultActions performOptions(IREST<UUID, Contact> controller, String urlTemplate, Object... urlVars) throws Exception {
        // @formatter:off
        MockHttpServletRequestBuilder builder = options(urlTemplate, urlVars)
                .characterEncoding("UTF-8");
        return getMockMvc(controller).perform(builder);
        // @formatter:on
    }
}
