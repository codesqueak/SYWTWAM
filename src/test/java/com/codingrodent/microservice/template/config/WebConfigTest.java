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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebConfigTest {

    @Test
    public void configureContentNegotiation() throws Exception {
        WebConfig webConfig = new WebConfig();
        ContentNegotiationConfigurer contentNegotiationConfigurer = Mockito.mock(ContentNegotiationConfigurer.class);

        when(contentNegotiationConfigurer.useJaf(anyBoolean())).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.defaultContentType(any(MediaType.class))).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.defaultContentTypeStrategy(any(ContentNegotiationStrategy.class))).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.favorParameter(anyBoolean())).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.favorPathExtension(anyBoolean())).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.ignoreAcceptHeader(anyBoolean())).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.ignoreUnknownPathExtensions(anyBoolean())).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.mediaType(any(String.class), any(MediaType.class))).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.parameterName(any(String.class))).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.replaceMediaTypes(any(Map.class))).thenReturn(contentNegotiationConfigurer);

        webConfig.configureContentNegotiation(contentNegotiationConfigurer);
        assertNotNull(webConfig);
    }
}