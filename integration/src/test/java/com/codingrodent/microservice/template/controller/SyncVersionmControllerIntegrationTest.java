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

import com.codingrodent.microservice.template.IntegrationTestBase;
import com.codingrodent.microservice.template.constants.SystemConstants;
import org.junit.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.SpringVersion;
import org.springframework.http.*;

import javax.inject.Inject;

public class SyncVersionmControllerIntegrationTest extends IntegrationTestBase {

    private final String json = "{\"Java\":\"" + System.getProperty("java.version") + "\",\"Spring\":\"" + SpringVersion.getVersion() + "\",\"Undertow\":\"" + io.undertow
            .Version.getFullVersionString() + "\"}";

    @Inject
    private TestRestTemplate restTemplate;

    @Test
    public void versionTest() throws Exception {
        ResponseEntity response = restTemplate.getForEntity("/version/" + SystemConstants.API_VERSION, String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(json, response.getBody().toString(), true);
    }
}
