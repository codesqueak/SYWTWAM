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

import com.codingrodent.microservice.template.MVCTestBase;
import com.codingrodent.microservice.template.config.advice.RestAdvice;
import com.codingrodent.microservice.template.metrics.api.ITemplateMetrics;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.*;
import org.mockito.Mock;
import org.springframework.core.SpringVersion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.codingrodent.microservice.template.constants.SystemConstants.*;
import static com.codingrodent.microservice.template.matchers.ExtraHeaderResultMatchers.extra;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Version Controller unit tests
 */
public class VersionControllerTest extends MVCTestBase {

    private final static String BASE = "/version/" + API_VERSION;
    private final static String JAVA = System.getProperty("java.version");
    private final static String SPRING = SpringVersion.getVersion();
    private final static String UNDERTOW = io.undertow.Version.getFullVersionString();
    private final static String json = "{\"Java\": \"" + JAVA + "\"," + "  \"Spring\":\"" + SPRING + "\"," + "\"Undertow\":\"" + UNDERTOW + "\"}";
    //
    private MockMvc mvc;
    private VersionController controller;
    @Mock
    private ITemplateMetrics templateMetrics;

    @Before
    public void init() throws JsonProcessingException {
        controller = new VersionController(templateMetrics);
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestAdvice()).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void getVersionSync() throws Exception {
        MockHttpServletRequestBuilder builder = get(BASE + "/sync").characterEncoding(CHAR_ENCODING);
        // @formatter:off
        mvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ));
        verify(templateMetrics, times(1)).inc(METRIC_VERSION_GET);
        // @formatter:on
    }

    @Test
    public void getVersionAsync() throws Exception {
        MockHttpServletRequestBuilder builder = get(BASE + "/async").characterEncoding(CHAR_ENCODING);
        MvcResult result = mvc.perform(builder).andReturn();
        // @formatter:off
        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ));
        verify(templateMetrics, times(1)).inc(METRIC_VERSION_GET);
        // @formatter:on
    }

    @Test
    public void getOptions() throws Exception {
        MockHttpServletRequestBuilder builder = options(BASE).characterEncoding(CHAR_ENCODING);
        // @formatter:off
        mvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(extra().options("GET,OPTIONS"));
         verify(templateMetrics, times(1)).inc(METRIC_VERSION_OPTIONS);
        // @formatter:on
    }
}


