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
import com.codingrodent.microservice.template.controller.impl.SyncContactController;
import com.codingrodent.microservice.template.model.*;
import com.codingrodent.microservice.template.service.api.IContactService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;

import java.util.*;

import static com.codingrodent.microservice.template.constants.SystemConstants.API_VERSION;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller unit tests
 */
@RunWith(MockitoJUnitRunner.class)
public class ContactServiceTest extends BaseMVCTests {

    private String json;
    private ModelVersion<Contact> modelVersion;
    private SyncContactController controller;

    @Mock
    private IContactService<Contact> contactService;

    @Before
    public void init() throws JsonProcessingException {
        Contact contact = new Contact("Firstname", "Lastname", 1, "000 000 000", "001 001 001", "AZ");
        json = mapper.writeValueAsString(contact);
        modelVersion = new ModelVersion<>(contact, Optional.of(12345L));
        controller = new SyncContactController(contactService);
    }

    @Test
    public void getContact() throws Exception {
        when(contactService.load(any())).thenReturn(Optional.of(modelVersion)).thenReturn(Optional.empty());

        // @formatter:off
        // Invalid UUID
        performGet(controller, "/syncname/" + API_VERSION + "/" + "abc-123")
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load
        performGet(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID())
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load
        performGet(controller,"/syncname/" + API_VERSION + "/" + UUID.randomUUID())
                .andExpect(status().isNotFound())
                .andReturn();
        // @formatter:on
        verify(contactService, times(2)).load(any());
    }

    @Test
    public void putContact() throws Exception {
        when(contactService.save(any(), any(), any())).thenReturn(Optional.of(modelVersion));

        // @formatter:off
        // No body
       performPut(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(),null,null)
                .andExpect(status()
                .isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Invalid UUID
        performPut(controller, "/syncname/" + API_VERSION + "/" + "abc-123",null,null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Save
        performPut(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"12345\"", json)
                .andExpect(status().isOk())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create
        performPut(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), null, json)
                .andExpect(status().isCreated())
                .andReturn();
        // @formatter:on

        verify(contactService, times(2)).save(any(), any(), any());
    }

}


