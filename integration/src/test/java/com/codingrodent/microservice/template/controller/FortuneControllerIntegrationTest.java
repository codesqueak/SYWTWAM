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
import com.jayway.jsonpath.*;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

// @DirtiesContext(classMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class FortuneControllerIntegrationTest extends IntegrationTestBase {

    @Inject
    private TestRestTemplate restTemplate;

    private final static String BASE = "/sync/fortune" + "/" + SystemConstants.API_VERSION;

    @Test
    public void getBadRequestTestAll() throws Exception {
        ResponseEntity response = restTemplate.getForEntity(BASE + "/list", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        response = restTemplate.getForEntity(BASE + "/list?page=0", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        response = restTemplate.getForEntity(BASE + "/list?size=10", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        response = restTemplate.getForEntity(BASE + "/list?page=0&size=0", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getBadRequestTestAnon() throws Exception {
        ResponseEntity response = restTemplate.getForEntity(BASE + "/list/anon", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        response = restTemplate.getForEntity(BASE + "/list/anon?page=0", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        response = restTemplate.getForEntity(BASE + "/list/anon?size=10", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        response = restTemplate.getForEntity(BASE + "/list/anon?page=0&size=0", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getBadRequestTestNamed() throws Exception {
        ResponseEntity response = restTemplate.getForEntity(BASE + "/list/named", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        response = restTemplate.getForEntity(BASE + "/list/named?page=0", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        response = restTemplate.getForEntity(BASE + "/list/named?size=10", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //
        response = restTemplate.getForEntity(BASE + "/list/named?page=0&size=0", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getListAllTest() throws Exception {
        check(restTemplate.getForEntity(BASE + "/list?page=0&size=10", String.class), "5");
        check(restTemplate.getForEntity(BASE + "/list?page=0&size=3", String.class), "3");
        check(restTemplate.getForEntity(BASE + "/list?page=1&size=3", String.class), "2");
    }

    @Test
    public void getListNamedTest() throws Exception {
        check(restTemplate.getForEntity(BASE + "/list/named?page=0&size=10", String.class), "1");
        check(restTemplate.getForEntity(BASE + "/list/named?page=0&size=3", String.class), "1");
        check(restTemplate.getForEntity(BASE + "/list/named?page=1&size=3", String.class), "0");
    }

    @Test
    public void getListAnonTest() throws Exception {
        check(restTemplate.getForEntity(BASE + "/list/anon?page=0&size=10", String.class), "4");
        check(restTemplate.getForEntity(BASE + "/list/anon?page=0&size=3", String.class), "3");
        check(restTemplate.getForEntity(BASE + "/list/anon?page=1&size=3", String.class), "1");
    }

    private void check(final ResponseEntity response, final String count) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DocumentContext body = JsonPath.parse(response.getBody().toString());
        assertEquals(count, body.read("$.length()").toString());
    }
}
