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
package com.codingrodent.microservice.template.api;

import io.swagger.annotations.*;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Simple sync REST controller default implementation
 * <p>
 * https://en.wikipedia.org/wiki/Representational_state_transfer
 */
public interface IREST<K, V> {

    /**
     * GET - Requests data from a specified resource
     *
     * @param uuid    Identifier of entity to fetch
     * @param version Entity version identifier
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @RequestMapping(path = "/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read an entity", notes = "Retrieve an entity identified by the Request-URI", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "OK, response entity in body"), //
            @ApiResponse(code = 304, message = "Not modified"), //
            @ApiResponse(code = 410, message = "No matching entity exists"), //
            @ApiResponse(code = 412, message = "Precondition Failed")})
    default ResponseEntity<Resource<V>> read(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid, //
                                             @ApiParam(name = HttpHeaders.IF_NONE_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false)
                                           Optional<String> version) {
        throw new UnsupportedOperationException("Get an entity not implemented");
    }

    /**
     * HEAD - As per get but no body returned
     *
     * @param uuid    Identifier of entity to fetch
     * @param version Entity version identifier
     * @return Return Empty result or 'Not Modified' if version matched
     */
    @RequestMapping(path = "/{uuid}", method = RequestMethod.HEAD, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find Out When a Resource Was Last Modified", notes = "As per GET but do not return body", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { //
            @ApiResponse(code = 204, message = "No content"), //
            @ApiResponse(code = 304, message = "Not modified"), //
            @ApiResponse(code = 410, message = "No matching entity exists"), //
            @ApiResponse(code = 412, message = "Precondition Failed")})
    default ResponseEntity<Optional> head(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid, //
                                          @ApiParam(name = HttpHeaders.IF_NONE_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false)
                                                  Optional<String> version) {
        throw new UnsupportedOperationException("Entity last modified not implemented");
    }

    /**
     * POST - Submits data to be processed to a specified resource (Return URL in location header etag)
     * <p>
     * Note: Not usually used in REST applications
     *
     * @param version Entity version identifier. Match forces overwrite else create (if entity doesn't exist)
     * @param value   Entity to write
     * @return Written entity
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create an entity", notes = "New / modified entity enclosed in the request as identified by the Request-URI", consumes = MediaType
            .APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "OK, response entity in body"), //
            @ApiResponse(code = 201, message = "Created, response entity in body"), //
            @ApiResponse(code = 409, message = "Conflict, can't update"), //
            @ApiResponse(code = 412, message = "Precondition Failed")})
    default ResponseEntity<Resource<V>> create(@ApiParam(name = HttpHeaders.IF_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) Optional<String>
                                                 version, //
                                               @ApiParam(name = "Entity", value = "Entity Value", required = true) @RequestBody V value) {
        throw new UnsupportedOperationException("Create an entity not implemented");
    }

    /**
     * PUT - Create or update an entity
     *
     * @param uuid    Identifier of entity to write
     * @param version Entity version identifier
     * @param value   Entity to write
     * @return Written entity
     */
    @RequestMapping(path = "/{uuid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update or create an entity", notes = "New / modified entity be stored under the supplied Request-URI", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "OK, response entity in body"), //
            @ApiResponse(code = 201, message = "Created, response entity in body"), //
            @ApiResponse(code = 409, message = "Conflict, can't update"), //
            @ApiResponse(code = 412, message = "Precondition Failed")})
    default ResponseEntity<Resource<V>> upsert(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid, //
                                               @ApiParam(name = HttpHeaders.IF_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) Optional<String>
                                             version, //
                                               @ApiParam(name = "Entity", value = "Entity Value", required = true) @RequestBody V value) {
        throw new UnsupportedOperationException("Update  or create an entity not implemented");
    }

    /**
     * PATCH- Modify an existing entity
     * <p>
     * https://tools.ietf.org/html/rfc6902 and https://tools.ietf.org/html/rfc7396
     *
     * @param uuid    Identifier of entity to update
     * @param version Entity version identifier
     * @param value   Entity to write
     * @return Written entity
     */
    @RequestMapping(path = "/{uuid}", method = RequestMethod.PATCH, consumes = {"application/json-patch+json", "application/merge-patch+json"}, produces =
            {"application/json-patch+json", "application/merge-patch+json"})
    @ApiOperation(value = "Request to modify an existing entity", notes = "Request to modify an existing entity")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "OK, response entity in body"), //
            @ApiResponse(code = 409, message = "Conflict, can't update"), //
            @ApiResponse(code = 412, message = "Precondition Failed")})
    default ResponseEntity<Void> patch(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid, //
                                       @ApiParam(name = HttpHeaders.IF_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_MATCH, required = false)
                                               Optional<String> version, //
                                       @ApiParam(name = "Entity", value = "Entity Value", required = true) @RequestBody V value) {
        throw new UnsupportedOperationException("Available options not implemented");
    }

    /**
     * DELETE - Delete an existing entity
     *
     * @param uuid    Identifier of entity to delete
     * @param version Entity version identifier
     * @return No body
     */
    @RequestMapping(path = "/{uuid}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete an entity", notes = "Requests that the server delete the resource identified by the Request-URI")
    @ApiResponses(value = { //
            @ApiResponse(code = 204, message = "No content"), //
            @ApiResponse(code = 412, message = "Precondition Failed")})
    default ResponseEntity<Void> delete(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid, //
                                        @ApiParam(name = HttpHeaders.IF_MATCH, value = "CAS Value") @RequestHeader(value = HttpHeaders.IF_MATCH, required = false)
                                                Optional<String> version) {
        throw new UnsupportedOperationException("Delete an entity not implemented");
    }

    /**
     * OPTIONS - Querying the Available Operations on a Resource
     *
     * @return Options listAll for this resource
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    @ApiOperation(value = "Querying the Available Operations on a Resource", notes = "Request for information about the communication options available")
    default ResponseEntity<Void> options() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAllow(getOptions());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    // Collections

    /**
     * GET - Requests data from a specified resource
     *
     * @param page Data page to read
     * @param size Size of page
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @RequestMapping(path = "/list", params = {"page", "size"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read all entities", notes = "Retrieve all entities in a paged manner if required", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "OK, response entity in body"), //
            @ApiResponse(code = 304, message = "Not modified"), //
            @ApiResponse(code = 410, message = "No matching entity exists"), //
            @ApiResponse(code = 412, message = "Precondition Failed")})
    default ResponseEntity<List<Resource<V>>> listAll(@ApiParam(name = "page", value = "Page to retrieve", required = true) @RequestParam int page, //
                                                      @ApiParam(name = "size", value = "Items per page", required = true) @RequestParam int size) {
        throw new UnsupportedOperationException("List not implemented");
    }

    /**
     * Get all allowed methods for the RESTful interface
     *
     * @return Set of allowed HTTP methods
     */
    Set<HttpMethod> getOptions();

}
