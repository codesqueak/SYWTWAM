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
import com.codingrodent.microservice.template.model.*;
import com.codingrodent.microservice.template.service.api.IFortuneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.*;
import org.mockito.Mock;
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
public class FortuneServiceTest extends MVCTestBase {

    private final static String json = "{\"text\":\"A fortune\",\"author\":\"An author\"}";
    private final static String json2 = "{\"text\":\"A fortune with uuid\",\"author\":\"An author with uuid\"}";
    private final static String BASE = "/sync/fortune/" + API_VERSION + "/";
    //
    private ModelVersion<Fortune> modelVersion;
    private SyncFortuneController controller;
    private Fortune fortune, fortuneWithUUID;
    @Mock
    private IFortuneService<Fortune> fortuneService;

    @Before
    public void init() throws JsonProcessingException {
        fortune = new Fortune("A fortune", Optional.of("An author"));
        fortuneWithUUID = new Fortune("A fortune with uuid", Optional.of("An author with uuid"), Optional.of(UUID.randomUUID()));
        modelVersion = new ModelVersion<>(fortune, Optional.of(12345L));
        controller = new SyncFortuneController(fortuneService);
    }

    @Test
    public void getFortune() throws Exception {

        when(fortuneService.load(any(String.class))).thenReturn(Optional.of(modelVersion), Optional.of(modelVersion), Optional.of(modelVersion), Optional.empty());

        // @formatter:off
        // Invalid UUID
        performGet(controller, BASE + BAD_UUID, null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found
        performGet(controller, BASE + UUID.randomUUID(), null)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - non matching eTag
        performGet(controller, BASE + UUID.randomUUID(), "\"2468\"")
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - matching eTag
        performGet(controller, BASE + UUID.randomUUID(), "\"12345\"")
                .andExpect(status().isNotModified())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - not found
        performGet(controller,BASE + UUID.randomUUID(), null)
                .andExpect(status().isGone())
                .andReturn();
        // @formatter:on
        verify(fortuneService, times(4)).load(any());
    }

    @Test
    public void headFortune() throws Exception {
        when(fortuneService.load(any(String.class))).thenReturn(Optional.of(modelVersion), Optional.of(modelVersion), Optional.of(modelVersion), Optional.empty());

        // @formatter:off
        // Invalid UUID
        performHead(controller, BASE + BAD_UUID, null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found
        performHead(controller, BASE + UUID.randomUUID(), null)
                .andExpect(status().isNoContent())
                .andExpect(content().string("")) // Must be an empty body even if it is found
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - non matching eTag
        performHead(controller, BASE + UUID.randomUUID(), "\"2468\"")
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - matching eTag
        performHead(controller, BASE + UUID.randomUUID(), "\"12345\"")
                .andExpect(status().isNotModified())
                .andReturn();

        // @formatter:on
        // @formatter:off
        // load - not found
        performHead(controller,BASE + UUID.randomUUID(), null)
                .andExpect(status().isGone())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on
        verify(fortuneService, times(4)).load(any());
    }

    @Test
    public void putFortune() throws Exception {
        when(fortuneService.load(any(String.class))).thenReturn(Optional.of(modelVersion), Optional.of(modelVersion), Optional.of(modelVersion));
        when(fortuneService.save(any(UUID.class), any(Fortune.class), any(Optional.class))).thenThrow(OptimisticLockingFailureException.class).thenReturn(modelVersion,
                                                                                                                                                          modelVersion, null);

        // @formatter:off
        // No body
       performPut(controller, BASE + UUID.randomUUID(),null,null)
                .andExpect(status()
                .isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Invalid UUID
        performPut(controller, BASE + BAD_UUID,null,null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // CAS fail
        performPut(controller, BASE + UUID.randomUUID(), "\"12345\"", json)
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // If-Match precondition fail
        performPut(controller, BASE + UUID.randomUUID(), "\"246\"", json)
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Save
        performPut(controller, BASE + UUID.randomUUID(), "\"12345\"", json)
                .andExpect(status().isAccepted())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create
        performPut(controller, BASE + UUID.randomUUID(), null, json)
                .andExpect(status().isCreated())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Save - null return value - fault
        performPut(controller, BASE + UUID.randomUUID(), "\"12345\"", json)
                .andExpect(status().isInternalServerError())
                .andReturn();
        // @formatter:on

        verify(fortuneService, times(4)).load(any());
        verify(fortuneService, times(4)).save(any(), any(), any());
    }

    @Test
    public void postFortune() throws Exception {
        Long version = 789L;
        when(fortuneService.create(any(Fortune.class), any(Optional.class))).thenThrow(DuplicateKeyException.class).thenReturn(new ModelVersion<>(fortuneWithUUID, Optional.of
                (12345L))).thenAnswer(inv -> new ModelVersion<>(fortuneWithUUID, (Optional) inv.getArguments()[1])).thenReturn(null);

        // @formatter:off
        // No body
       performPost(controller, "/sync/fortune/" + API_VERSION,null,null)
                .andExpect(status()
                .isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Already exists
        performPost(controller, "/sync/fortune/" + API_VERSION , "\"12345\"", json)
                .andExpect(status().isConflict())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create - no eTag
        performPost(controller, "/sync/fortune/" + API_VERSION , null, json2)
                .andExpect(status().isCreated())
                .andExpect(content().json(json2))
                .andExpect(header().string(HttpHeaders.ETAG, "\"12345\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create with eTag
        performPost(controller, "/sync/fortune/" + API_VERSION , "\""+version+"\"", json2)
                .andExpect(status().isCreated())
                .andExpect(content().json(json2))
                .andExpect(header().string(HttpHeaders.ETAG, "\""+version+"\""))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create with eTag - null return value - fault
        performPost(controller, "/sync/fortune/" + API_VERSION , "\""+version+"\"", json)
                .andExpect(status().isInternalServerError())
                .andReturn();
        // @formatter:on

        verify(fortuneService, times(4)).create(any(), any());
    }

    @Test
    public void deleteFortune() throws Exception {
        when(fortuneService.load(any())).thenReturn(Optional.of(modelVersion));

        // @formatter:off
        // Invalid UUID
        performDelete(controller, BASE + BAD_UUID, "\"12345\"")
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Delete
        performDelete(controller, BASE + UUID.randomUUID(), null)
                .andExpect(status().isNoContent())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Delete
        performDelete(controller, BASE + UUID.randomUUID(), "\"12345\"")
                .andExpect(status().isNoContent())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Delete - loaded etag doesn't match
        performDelete(controller, BASE + UUID.randomUUID(), "\"246\"")
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        // @formatter:on

        verify(fortuneService, times(2)).load(any());
        verify(fortuneService, times(2)).delete(any());
    }

    @Test
    public void optionsFortune() throws Exception {

        // @formatter:off
        // Save
        performOptions(controller, "/sync/fortune/" + API_VERSION)
                .andExpect(status().isOk())
                .andExpect(extra().options("GET, HEAD, POST, PUT, PATCH, DELETE"))
                .andReturn();
        // @formatter:on
    }
}


