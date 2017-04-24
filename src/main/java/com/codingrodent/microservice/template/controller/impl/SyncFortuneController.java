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

import com.codingrodent.microservice.template.controller.api.IFortune;
import com.codingrodent.microservice.template.exception.*;
import com.codingrodent.microservice.template.model.*;
import com.codingrodent.microservice.template.service.api.IFortuneService;
import io.swagger.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.*;

import static com.codingrodent.microservice.template.constants.SystemConstants.API_VERSION;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import static org.springframework.http.HttpMethod.*;

/**
 * Simple sync REST controller for the Fortune resource
 */

@RestController
@Api(tags = "sync", value = "syncfortune", description = "Endpoint for fortune management")
@RequestMapping("/sync/fortune/" + API_VERSION)

public class SyncFortuneController extends RestBase<Fortune> implements IFortune<UUID, Fortune> {

    private final IFortuneService<Fortune> fortuneService;
    private final static Set<HttpMethod> ALLOWED_OPTIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(GET, HEAD, POST, PUT, PATCH, DELETE)));

    @Inject
    public SyncFortuneController(final IFortuneService<Fortune> fortuneService) {
        this.fortuneService = fortuneService;
    }

    /**
     * GET - Requests data from a the Fortune resource
     *
     * @param uuid    Identifier of fortune to fetch
     * @param version Fortune version identifier
     * @return Return selected fortune or 'Not Modified' if version matched
     */
    @Override
    public ResponseEntity<Fortune> read(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid, //
                                        @ApiParam(name = HttpHeaders.IF_NONE_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false)
                                                Optional<String> version) {
        Optional<ModelVersion<Fortune>> modelVersion = fortuneService.load(uuid.toString());
        if (!version.isPresent() || ifNoneMatch(version, modelVersion)) {
            // Exec
            return modelVersion.map(mv -> new ResponseEntity<>(mv.getModel(), getETag(mv), HttpStatus.OK)).orElseThrow(DocumentNeverFoundException::new);
        } else {
            return new ResponseEntity<>(getContent(), HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * HEAD - As per get but no body returned
     *
     * @param uuid    Identifier of fortune to fetch
     * @param version Fortune version identifier
     * @return Return Empty result or 'Not Modified' if version matched
     */
    @Override
    public ResponseEntity<Optional> head(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid, //
                                         @ApiParam(name = HttpHeaders.IF_NONE_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false)
                                                 Optional<String> version) {
        Optional<ModelVersion<Fortune>> modelVersion = fortuneService.load(uuid.toString());
        if (!version.isPresent() || ifNoneMatch(version, modelVersion)) {
            // Exec
            return modelVersion.map(mv -> new ResponseEntity<Optional>(Optional.empty(), getETag(mv), HttpStatus.NO_CONTENT)).orElseThrow(DocumentNeverFoundException::new);
        } else {
            return new ResponseEntity<>(getContent(), HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * PUT - Create or update a fortune
     *
     * @param uuid    Identifier of fortune to write
     * @param version Fortune version identifier
     * @param fortune Fortune to write
     * @return Written fortune
     */
    @Override
    public ResponseEntity<Fortune> upsert(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid, //
                                          @ApiParam(name = HttpHeaders.IF_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_MATCH, required = false)
                                                  Optional<String> version, //
                                          @ApiParam(name = "Entity", value = "Fortune Value", required = true) @RequestBody Fortune fortune) {

        if (version.isPresent()) {
            // Only need  to check if record exists when doing an If-Match
            Optional<ModelVersion<Fortune>> modelVersion = fortuneService.load(uuid.toString());
            if (!ifMatch(version, modelVersion))
                throw new PreconditionFailedException("PUT If-Match");
        }
        ModelVersion<Fortune> written = fortuneService.save(uuid, fortune, version.map(extractETag));
        // As a workaround we will take the version to signify if this is a create or update
        if (null == written) {
            throw new ApplicationFaultException("PUT failed to return a document");
        } else {
            return new ResponseEntity<>(written.getModel(), getETag(written), version.map(e -> HttpStatus.ACCEPTED).orElse(HttpStatus.CREATED));
        }
    }

    /**
     * POST - Submits fortune data to be processed to a specified resource (Return URL in location header etag)
     * <p>
     * Note: Not usually used in REST applications
     *
     * @param version Fortune version identifier. Match forces overwrite else create (if fortune doesn't exist)
     * @param fortune Fortune to write
     * @return Fortune entity
     */
    @Override
    public ResponseEntity<Fortune> create(@ApiParam(name = HttpHeaders.IF_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_MATCH, required = false)
                                                      Optional<String> version, //
                                          @ApiParam(name = "Entity", value = "Fortune Value", required = true) @RequestBody Fortune fortune) {
        // Not doing If-Match test as we can guarantee record does not exists due to uuid creation
        ModelVersion<Fortune> written = fortuneService.create(fortune, version.map(extractETag));
        if (null == written) {
            throw new ApplicationFaultException("POST failed to return a document");
        } else {
            String location = linkTo(methodOn(SyncFortuneController.class).read(UUID.randomUUID(), Optional.empty())).toUri().toASCIIString();
            return new ResponseEntity<>(written.getModel(), getETag(written, HttpHeaders.LOCATION, location), HttpStatus.CREATED);
        }
    }

    /**
     * DELETE - Delete an existing fortune
     *
     * @param uuid    Identifier of fortune to delete
     * @param version Fortune version identifier
     * @return No body
     */
    @Override
    public ResponseEntity<Void> delete(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid, //
                                       @ApiParam(name = HttpHeaders.IF_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_MATCH, required = false)
                                               Optional<String> version) {

        if (version.isPresent()) {
            // Only need  to check if record exists when doing an If-Match
            Optional<ModelVersion<Fortune>> modelVersion = fortuneService.load(uuid.toString());
            if (!ifMatch(version, modelVersion))
                throw new PreconditionFailedException("DELETE If-Match");
        }
        fortuneService.delete(uuid.toString());
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

    // Collections

    /**
     * GET - Requests data from a specified resource
     *
     * @param page Data page to read
     * @param size Size of page
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @Override
    public ResponseEntity<List<Fortune>> listAll(@ApiParam(name = "page", value = "Page to retrieve", required = true) @RequestParam int page, //
                                                 @ApiParam(name = "size", value = "Items per page", required = true) @RequestParam int size) {
        return new ResponseEntity<>(fortuneService.listAll(page, size), HttpStatus.OK);
    }

    /**
     * GET - Requests data from a specified resource
     *
     * @param page Data page to read
     * @param size Size of page
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @Override
    public ResponseEntity<List<Fortune>> listNamed(@ApiParam(name = "page", value = "Page to retrieve", required = true) @RequestParam int page, //
                                                   @ApiParam(name = "size", value = "Items per page", required = true) @RequestParam int size) {
        return new ResponseEntity<>(fortuneService.listNamed(page, size), HttpStatus.OK);
    }

    /**
     * GET - Requests data from a specified resource
     *
     * @param page Data page to read
     * @param size Size of page
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @Override
    public ResponseEntity<List<Fortune>> listAnon(@ApiParam(name = "page", value = "Page to retrieve", required = true) @RequestParam int page, //
                                                  @ApiParam(name = "size", value = "Items per page", required = true) @RequestParam int size) {
        return new ResponseEntity<>(fortuneService.listAnon(page, size), HttpStatus.OK);
    }

}
