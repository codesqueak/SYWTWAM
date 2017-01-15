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
import org.springframework.dao.*;
import org.springframework.http.HttpHeaders;

import java.util.*;

import static com.codingrodent.microservice.template.constants.SystemConstants.*;
import static com.codingrodent.microservice.template.matchers.ExtraHeaderResultMatchers.extra;
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
    private Contact contact;

    @Mock
    private IContactService<Contact> contactService;

    @Before
    public void init() throws JsonProcessingException {
        contact = new Contact("Firstname", "Lastname", 1, "000 000 000", "001 001 001", "AZ");
        json = mapper.writeValueAsString(contact);
        modelVersion = new ModelVersion<>(contact, Optional.of(12345L));
        controller = new SyncContactController(contactService);
    }

    @Test
    public void getContact() throws Exception {
        when(contactService.load(any(UUID.class))).thenReturn(Optional.of(modelVersion), Optional.of(modelVersion), Optional.of(modelVersion), Optional.empty());

        // @formatter:off
        // Invalid UUID
        performGet(controller, "/syncname/" + API_VERSION + "/" + BAD_UUID, null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found
        performGet(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), null)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - non matching eTag
        performGet(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"2468\"")
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - matching eTag
        performGet(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"12345\"")
                .andExpect(status().isNotModified())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - not found
        performGet(controller,"/syncname/" + API_VERSION + "/" + UUID.randomUUID(), null)
                .andExpect(status().isGone())
                .andReturn();
        // @formatter:on
        verify(contactService, times(4)).load(any());
    }

    @Test
    public void headContact() throws Exception {
        when(contactService.load(any(UUID.class))).thenReturn(Optional.of(modelVersion), Optional.of(modelVersion), Optional.of(modelVersion), Optional.empty());

        // @formatter:off
        // Invalid UUID
        performHead(controller, "/syncname/" + API_VERSION + "/" + BAD_UUID, null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found
        performHead(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), null)
                .andExpect(status().isNoContent())
                .andExpect(content().string("")) // Must be an empty body even if it is found
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - non matching eTag
        performHead(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"2468\"")
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - matching eTag
        performHead(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"12345\"")
                .andExpect(status().isNotModified())
                .andReturn();

        // @formatter:on
        // @formatter:off
        // load - not found
        performHead(controller,"/syncname/" + API_VERSION + "/" + UUID.randomUUID(), null)
                .andExpect(status().isGone())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on
        verify(contactService, times(4)).load(any());
    }

    @Test
    public void putContact() throws Exception {
        when(contactService.load(any(UUID.class))).thenReturn(Optional.of(modelVersion), Optional.of(modelVersion), Optional.of(modelVersion));
        when(contactService.save(any(UUID.class), any(Contact.class), any(Optional.class))).thenThrow(OptimisticLockingFailureException.class).thenReturn(modelVersion,
                                                                                                                                                          modelVersion, null);

        // @formatter:off
        // No body
       performPut(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(),null,null)
                .andExpect(status()
                .isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Invalid UUID
        performPut(controller, "/syncname/" + API_VERSION + "/" + BAD_UUID,null,null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // CAS fail
        performPut(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"12345\"", json)
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // If-Match precondition fail
        performPut(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"246\"", json)
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Save
        performPut(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"12345\"", json)
                .andExpect(status().isAccepted())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create
        performPut(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), null, json)
                .andExpect(status().isCreated())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Save - null return value - fault
        performPut(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"12345\"", json)
                .andExpect(status().isInternalServerError())
                .andReturn();
        // @formatter:on

        verify(contactService, times(4)).load(any());
        verify(contactService, times(4)).save(any(), any(), any());
    }

    @Test
    public void postContact() throws Exception {
        Long version = 789L;
        when(contactService.create(any(Contact.class), any(Optional.class))).thenThrow(DuplicateKeyException.class).thenReturn(modelVersion).thenAnswer(inv -> new ModelVersion<>
                (contact, (Optional) inv.getArguments()[1])).thenReturn(null);

        // @formatter:off
        // No body
       performPost(controller, "/syncname/" + API_VERSION,null,null)
                .andExpect(status()
                .isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Already exists
        performPost(controller, "/syncname/" + API_VERSION , "\"12345\"", json)
                .andExpect(status().isConflict())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create - no eTag
        performPost(controller, "/syncname/" + API_VERSION , null, json)
                .andExpect(status().isCreated())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create with eTag
        performPost(controller, "/syncname/" + API_VERSION , "\""+version+"\"", json)
                .andExpect(status().isCreated())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\""+version+"\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create with eTag - null return value - fault
        performPost(controller, "/syncname/" + API_VERSION , "\""+version+"\"", json)
                .andExpect(status().isInternalServerError())
                .andReturn();
        // @formatter:on

        verify(contactService, times(4)).create(any(), any());
    }

    @Test
    public void deleteContact() throws Exception {
        when(contactService.load(any())).thenReturn(Optional.of(modelVersion));

        // @formatter:off
        // Invalid UUID
        performDelete(controller, "/syncname/" + API_VERSION + "/" + BAD_UUID, "\"12345\"")
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Delete
        performDelete(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), null)
                .andExpect(status().isNoContent())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Delete
        performDelete(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"12345\"")
                .andExpect(status().isNoContent())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Delete - loaded etag doesn't match
        performDelete(controller, "/syncname/" + API_VERSION + "/" + UUID.randomUUID(), "\"246\"")
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        // @formatter:on

        verify(contactService, times(2)).load(any());
        verify(contactService, times(2)).delete(any());
    }

    @Test
    public void optionsContact() throws Exception {

        // @formatter:off
        // Save
        performOptions(controller, "/syncname/" + API_VERSION)
                .andExpect(status().isOk())
                .andExpect(extra().options("GET, HEAD, POST, PUT, PATCH, DELETE"))
                .andReturn();
        // @formatter:on
    }
}


