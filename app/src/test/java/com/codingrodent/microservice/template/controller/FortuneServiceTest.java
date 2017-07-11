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
    private final static String jsonWithUUID1 = "{\"text\":\"A fortune with uuid\",\"author\":\"An author with uuid\"}";
    private final static String jsonWithUUID2 = "{\"text\":\"A fortune with uuid 2\",\"author\":\"An author with uuid 2\"}";
    private final static String BASE = "/sync/fortune/" + API_VERSION + "/";
    private final Random random = new Random();
    //
    private ModelVersion<Fortune> modelVersion;
    private FortuneController controller;
    private Fortune fortuneWithUUID1, fortuneWithUUID2;
    private long cas;
    private UUID randomUUID;
    @Mock
    private IFortuneService<Fortune> fortuneService;

    @Before
    public void init() throws JsonProcessingException {
        Fortune fortune = new Fortune("A fortune", Optional.of("An author"));
        fortuneWithUUID1 = new Fortune("A fortune with uuid", Optional.of("An author with uuid"), Optional.of(UUID.randomUUID()));
        fortuneWithUUID2 = new Fortune("A fortune with uuid 2", Optional.of("An author with uuid 2"), Optional.of(UUID.randomUUID()));
        cas = random.nextLong();
        modelVersion = new ModelVersion<>(fortune, Optional.of(cas));
        controller = new FortuneController(fortuneService);
        randomUUID = UUID.randomUUID();
    }

    @Test
    public void getFortune() throws Exception {

        when(fortuneService.load(any(String.class))).thenReturn(Optional.of(modelVersion), Optional.of(modelVersion), Optional.of(modelVersion), Optional.empty());

        String url = BASE + randomUUID;
        String etag = "\"" + Etag.encodEtag(cas, url) + "\"";

        // @formatter:off
        // Invalid UUID
        performGet(controller, BASE + BAD_UUID, null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, etag))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - non matching eTag
        performGet(controller, url, "\"2468\"")
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, etag))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - matching eTag
        performGet(controller, url, etag)
                .andExpect(status().isNotModified())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - not found
        performGet(controller,url, null)
                .andExpect(status().isGone())
                .andReturn();
        // @formatter:on
        verify(fortuneService, times(4)).load(any());
    }

    @Test
    public void headFortune() throws Exception {
        when(fortuneService.load(any(String.class))).thenReturn(Optional.of(modelVersion), Optional.of(modelVersion), Optional.of(modelVersion), Optional.empty());
        String url = BASE + randomUUID;
        String etag = "\"" + Etag.encodEtag(cas, url) + "\"";
        // @formatter:off
        // Invalid UUID
        performHead(controller, BASE + BAD_UUID, null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found
        performHead(controller, url, null)
                .andExpect(status().isNoContent())
                .andExpect(content().string("")) // Must be an empty body even if it is found
                .andExpect(header().string(HttpHeaders.ETAG, etag))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - non matching eTag
        performHead(controller, url, "\"2468\"")
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.ETAG, etag))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // load - found - matching eTag
        performHead(controller, url, etag)
                .andExpect(status().isNotModified())
                .andReturn();

        // @formatter:on
        // @formatter:off
        // load - not found
        performHead(controller,url, null)
                .andExpect(status().isGone())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on
        verify(fortuneService, times(4)).load(any());
    }

    @Test
    public void putFortune() throws Exception {
        when(fortuneService.load(any(String.class))).thenReturn(Optional.empty(), Optional.of(modelVersion), Optional.of(modelVersion), Optional.of(modelVersion));
        when(fortuneService.save(any(String.class), any(Fortune.class), any(Optional.class))).thenThrow(OptimisticLockingFailureException.class).thenReturn(modelVersion,
                                                                                                                                                            modelVersion, null);
        String url = BASE + randomUUID;
        String etag = "\"" + Etag.encodEtag(cas, url) + "\"";

        // @formatter:off
        // No body
       performPut(controller, url,null,null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Invalid UUID
        performPut(controller, BASE + BAD_UUID,null,null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // If-Match precondition fail
        performPut(controller, url, "\"246\"", json)
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Save - fail optimistic locking
        performPut(controller, url, etag, json)
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Save
        performPut(controller, url, etag, json)
                .andExpect(status().isAccepted())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, etag))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create
        performPut(controller, url, null, json)
                .andExpect(status().isCreated())
                .andExpect(content().json(json))
                .andExpect(header().string(HttpHeaders.ETAG, etag))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Save - null return value - fault
        performPut(controller, url, etag, json)
                .andExpect(status().isInternalServerError())
                .andReturn();
        // @formatter:on

        verify(fortuneService, times(4)).load(any());
        verify(fortuneService, times(4)).save(any(), any(), any());
    }

    @Test
    public void postFortune() throws Exception {
        when(fortuneService.create(any(Fortune.class), any(Optional.class))).thenThrow(DuplicateKeyException.class).thenReturn(new ModelVersion<>(fortuneWithUUID1, Optional.of
                (cas))).thenAnswer(inv -> new ModelVersion<>(fortuneWithUUID2, (Optional) inv.getArguments()[1])).thenReturn(null);

        String url = BASE;
        String etag = "\"" + Etag.encodEtag(cas, url) + "\"";

        // @formatter:off
        // No body
       performPost(controller, url,null,null)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Already exists
        performPost(controller, url , etag, json)
                .andExpect(status().isConflict())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create - no eTag
        performPost(controller, url , null, jsonWithUUID1)
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonWithUUID1))
                .andExpect(header().string(HttpHeaders.ETAG, etag))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create with eTag
        performPost(controller, url , etag, jsonWithUUID2)
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonWithUUID2))
                .andExpect(header().string(HttpHeaders.ETAG, etag))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Create with eTag - null return value - fault
        performPost(controller, url , etag, json)
                .andExpect(status().isInternalServerError())
                .andReturn();
        // @formatter:on

        verify(fortuneService, times(4)).create(any(), any());
    }

    @Test
    public void deleteFortune() throws Exception {
        when(fortuneService.load(any())).thenReturn(Optional.of(modelVersion));
        String url = BASE + randomUUID;
        String etag = "\"" + Etag.encodEtag(cas, url) + "\"";

        // @formatter:off
        // Invalid UUID
        performDelete(controller, BASE + BAD_UUID, etag)
                .andExpect(status().isBadRequest())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Delete
        performDelete(controller, url, null)
                .andExpect(status().isNoContent())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Delete
        performDelete(controller, url, etag)
                .andExpect(status().isNoContent())
                .andReturn();
        // @formatter:on

        // @formatter:off
        // Delete - loaded etag doesn't match
        performDelete(controller, url, "\"246\"")
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

    @Test
    public void getAllFortunes() throws Exception {

        List<Fortune> list1 = new LinkedList<>();
        list1.add(fortuneWithUUID1);
        List<Fortune> list2 = new LinkedList<>();
        list2.addAll(list1);
        list2.add(fortuneWithUUID2);
        List<Fortune> list3 = new LinkedList<>();

        when(fortuneService.listAll(anyInt(), anyInt())).thenReturn(list1, list2, list3);

        String url = BASE + "/list?page=0&size=99";

        // @formatter:off
        // one return value
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json("["+jsonWithUUID1+"]"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // two return values
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json("["+jsonWithUUID1+","+jsonWithUUID2+"]"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // no return values
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on
    }

    @Test
    public void getNamedFortunes() throws Exception {

        List<Fortune> list1 = new LinkedList<>();
        list1.add(fortuneWithUUID1);
        List<Fortune> list2 = new LinkedList<>();
        list2.addAll(list1);
        list2.add(fortuneWithUUID2);
        List<Fortune> list3 = new LinkedList<>();

        when(fortuneService.listNamed(anyInt(), anyInt())).thenReturn(list1, list2, list3);

        String url = BASE + "/list/named?page=0&size=0";

        // @formatter:off
        // one return value
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json("["+jsonWithUUID1+"]"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // two return values
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json("["+jsonWithUUID1+","+jsonWithUUID2+"]"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // no return values
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

    }

    @Test
    public void getAnonFortunes() throws Exception {

        List<Fortune> list1 = new LinkedList<>();
        list1.add(fortuneWithUUID1);
        List<Fortune> list2 = new LinkedList<>();
        list2.addAll(list1);
        list2.add(fortuneWithUUID2);
        List<Fortune> list3 = new LinkedList<>();

        when(fortuneService.listAnon(anyInt(), anyInt())).thenReturn(list1, list2, list3);

        String url = BASE + "/list/anon?page=0&size=99";

        // @formatter:off
        // one return value
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json("["+jsonWithUUID1+"]"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // two return values
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json("["+jsonWithUUID1+","+jsonWithUUID2+"]"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

        // @formatter:off
        // no return values
        performGet(controller, url, null)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE ))
                .andReturn();
        // @formatter:on

    }

}


