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
package com.codingrodent.microservice.template.controller.impl;

import com.codingrodent.microservice.template.controller.api.IREST;
import com.codingrodent.microservice.template.exception.*;
import com.codingrodent.microservice.template.model.Contact;
import com.codingrodent.microservice.template.model.*;
import com.codingrodent.microservice.template.service.api.IContactService;
import io.swagger.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.*;

import static com.codingrodent.microservice.template.constants.SystemConstants.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import static org.springframework.http.HttpMethod.*;

/**
 * Simple sync REST controller
 */
@RestController
@Api(tags = "sync", value = "synccontact", description = "Endpoint for contact management")
@RequestMapping("/syncname/" + API_VERSION)
public class SyncContactController implements IREST<UUID, Contact> {

    private final IContactService<Contact> contactService;
    private final static Set<HttpMethod> ALLOWED_OPTIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(GET, HEAD, POST, PUT, PATCH, DELETE)));

    @Inject
    public SyncContactController(final IContactService<Contact> contactService) {
        this.contactService = contactService;
    }

    // GET
    @Override
    public ResponseEntity<Contact> read(@ApiParam(name = "uuid", value = "Unique contact UUID", required = true) @PathVariable final UUID uuid, //
                                        @ApiParam(name = "If-None-Match", value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required =
                                                false) Optional<String> version) {
        Optional<ModelVersion<Contact>> modelVersion = contactService.load(uuid);
        if (isNotModified(version, modelVersion)) {
            return new ResponseEntity<>(getContent(), HttpStatus.NOT_MODIFIED);
        } else {
            return modelVersion.map(mv -> new ResponseEntity<>(mv.getModel(), getETag(mv), HttpStatus.OK)).orElseThrow(() -> new DocumentNeverFoundException());
        }
    }

    // HEAD
    @Override
    public ResponseEntity<Void> head(@ApiParam(name = "uuid", value = "Unique contact UUID", required = true) @PathVariable final UUID uuid, @ApiParam(name =
            "If-None-Match", value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) Optional<String> version) {
        Optional<ModelVersion<Contact>> modelVersion = contactService.load(uuid);
        if (isNotModified(version, modelVersion)) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else {
            return modelVersion.map(mv -> new ResponseEntity<Void>(getETag(mv, HttpHeaders.CONTENT_TYPE, CONTENT_TYPE), HttpStatus.NO_CONTENT)).orElseThrow(
                    () -> new DocumentNeverFoundException());
        }
    }

    // PUT - Create or Update
    @Override
    public ResponseEntity<Contact> upsert(@ApiParam(name = "uuid", value = "Unique contact UUID", required = true) @PathVariable final UUID uuid, //
                                          @ApiParam(name = "If-None-Match", value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required =
                                                  false) Optional<String> version, //
                                          @Valid @RequestBody final Contact contact) {
        Optional<ModelVersion<Contact>> modelVersion = contactService.save(uuid, contact, version.map(extractETag));
        //
        // The spring data couchbase component doesn't expose 'replace()' so that we can't tell if a document already exists. Calling exists() doesn't help
        // doesn't solve this as we are not in a transactional system and the database may change
        // As a workaround we will take the version to signify if this is a create or update
        return modelVersion.map(mv -> new ResponseEntity<>(mv.getModel(), getETag(mv), version.map(e -> HttpStatus.ACCEPTED).orElse(HttpStatus.CREATED)))
                .orElseThrow(() -> new ApplicationFaultException("PUT failed to return a document"));
    }

    // POST - Create - Return URL in location header
    @Override
    public ResponseEntity<Contact> create(@ApiParam(name = "ETag", value = "CAS Value") @RequestHeader(value = HttpHeaders.ETAG, required = false)
                                                      Optional<String> version, //
                                          @Valid @RequestBody final Contact contact) {
        Optional<ModelVersion<Contact>> modelVersion = contactService.create(contact, version.map(extractETag));
        return modelVersion.map(mv -> {
            HttpHeaders headers = getETag(mv);
            headers.setLocation(linkTo(methodOn(SyncContactController.class).read(UUID.randomUUID(), Optional.empty())).toUri());
            return new ResponseEntity<>(mv.getModel(), headers, HttpStatus.CREATED);
        }).orElseThrow(() -> new ApplicationFaultException("POST failed to return a document"));
    }

    // DELETE = Delete (200)
    @Override
    public ResponseEntity<Void> delete(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID uuid) {
        contactService.delete(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get all allowed methods for the RESTful interface
     *
     * @return Set of allowed HTTP methods
     */
    public Set<HttpMethod> getOptions() {
        return ALLOWED_OPTIONS;
    }

}
