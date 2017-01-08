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
import com.codingrodent.microservice.template.exception.ApplicationFaultException;
import com.codingrodent.microservice.template.model.Contact;
import com.codingrodent.microservice.template.model.*;
import com.codingrodent.microservice.template.service.api.IContactService;
import io.swagger.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.*;

import static com.codingrodent.microservice.template.constants.SystemConstants.API_VERSION;
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

    private final static Set<HttpMethod> ALLOWED_OPTIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS)));

    @Inject
    public SyncContactController(final IContactService<Contact> contactService) {
        this.contactService = contactService;
    }

    // GET (200)
    @Override
    public ResponseEntity<Contact> read(@ApiParam(name = "uuid", value = "Unique contact UUID", required = true) @PathVariable final UUID uuid) {
        Optional<ModelVersion<Contact>> modelVersion = contactService.load(uuid);

        return modelVersion.map(mv -> new ResponseEntity<>(mv.getModel(), getETag(mv), HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // PUT - Create (201) or Update  (200)
    @Override
    public ResponseEntity<Optional<Contact>> upsert(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID
                                                                uuid, //
                                                    @ApiParam(name = "ETag", value = "CAS Value") @RequestHeader(value = HttpHeaders.ETAG, required = false)
                                                            Optional<String> version, //
                                                    @Valid @RequestBody final Contact contact) {
        Optional<ModelVersion<Contact>> modelVersion = contactService.save(uuid, contact, version.map(v -> Long.parseLong(v.replace("\"", ""))));
        //
        // The spring data couchbase component doesn't expose 'replace()' so that we can't tell if a document already exists. Calling exists() doesn't help
        // doesn't solve this as we are not in a transactional system and the database may change
        // As a workaround we will take the version to signify if this is a create or update
        return modelVersion.map(mv -> new ResponseEntity<Optional<Contact>>(Optional.empty(), getETag(mv), version.map(e -> HttpStatus.OK)
                .orElse(HttpStatus.CREATED))).orElseThrow(() -> new ApplicationFaultException("PUT failed to return a document"));
    }

    // POST - Create (201) - Return URL in location header
    @Override
    public ResponseEntity<Optional<Contact>> create(@Valid @RequestBody final Contact contact) {
        Optional<ModelVersion<Contact>> modelVersion = contactService.create(contact);
        return modelVersion.map(mv -> {
            HttpHeaders headers = getETag(mv);
            headers.setLocation(linkTo(methodOn(SyncContactController.class).read(UUID.randomUUID())).toUri());
            return new ResponseEntity<Optional<Contact>>(Optional.empty(), headers, HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new ApplicationFaultException("POST failed to return a document"));
    }

    // DELETE = Delete (200)
    @Override
    public ResponseEntity<Optional<Void>> delete(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID uuid) {
        contactService.delete(uuid);
        return new ResponseEntity<>(Optional.empty(), HttpStatus.NO_CONTENT);
    }

    // HEAD (200)
    @Override
    public ResponseEntity<Optional<Contact>> head(@ApiParam(name = "uuid", value = "Unique contact UUID", required = true) @PathVariable final UUID uuid) {
        Optional<ModelVersion<Contact>> modelVersion = contactService.load(uuid);
        return modelVersion.map(mv -> new ResponseEntity<Optional<Contact>>(Optional.empty(), getETag(mv), HttpStatus.OK)).orElse(new ResponseEntity<>(Optional.empty(), HttpStatus.GONE));
    }

    /**
     * Get all allowed methods for the RESTful interface
     *
     * @return Set of allowed HTTP methods
     */
    public Set<HttpMethod> getHeaders() {
        return ALLOWED_OPTIONS;
    }

}
