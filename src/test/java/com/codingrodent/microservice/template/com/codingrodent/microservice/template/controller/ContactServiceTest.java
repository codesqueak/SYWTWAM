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
package com.codingrodent.microservice.template.com.codingrodent.microservice.template.controller;

import com.codingrodent.microservice.template.BaseMVCTests;
import com.codingrodent.microservice.template.model.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;

import javax.inject.Inject;
import java.util.UUID;

import static com.codingrodent.microservice.template.constants.SystemConstants.API_VERSION;
import static com.codingrodent.microservice.template.matchers.ETagNumericMatcher.isETagNumeric;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ContactServiceTest extends BaseMVCTests {

    @Inject
    private MockMvc mvc;

    @Test
    public void getContact() throws Exception {
        // Not available test
        mvc.perform(MockMvcRequestBuilders.get("/syncname/" + API_VERSION + "/" + UUID.randomUUID()).accept(MediaType.APPLICATION_JSON_VALUE))//
                .andExpect(status().isNotFound());
    }

    @Test
    public void createContact() throws Exception {
        // Create test
        Contact contact = new Contact("Firstname", "Lastname", 1, "000 000 000", "001 001 001", "AZ");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(contact);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/syncname/" + API_VERSION + "/" + UUID.randomUUID()).accept(MediaType
                .APPLICATION_JSON_VALUE).content(json).contentType(MediaType.APPLICATION_JSON_VALUE);
        mvc.perform(builder).andExpect(status().isNoContent()).andExpect(header().string(HttpHeaders.ETAG, isETagNumeric()));
    }

}
