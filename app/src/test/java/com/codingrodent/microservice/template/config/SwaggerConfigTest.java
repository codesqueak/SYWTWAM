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

import com.fasterxml.classmate.TypeResolver;
import org.junit.*;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;

import static org.junit.Assert.*;

public class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig;

    @Before
    public void init() {
        swaggerConfig = new SwaggerConfig(new TypeResolver());
    }

    @Test
    public void panopticonApi() throws Exception {
        Docket docket = swaggerConfig.panopticonApi();
        assertEquals("template-api", docket.getGroupName());
    }

    @Test
    public void uiConfig() throws Exception {
        UiConfiguration uiConfig = swaggerConfig.uiConfig();
        assertEquals(60000L, uiConfig.getRequestTimeout().longValue());
        assertArrayEquals(new String[]{"get", "post", "put", "delete", "patch", "head", "options"}, uiConfig.getSupportedSubmitMethods());
    }

}