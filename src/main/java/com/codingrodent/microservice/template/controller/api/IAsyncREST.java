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
package com.codingrodent.microservice.template.controller.api;

import io.swagger.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

/**
 * Simple async REST controller default implementation
 * <p>
 * https://en.wikipedia.org/wiki/Representational_state_transfer
 */
public interface IAsyncREST<K, V> extends RESTBase {

    // GET (200)
    @RequestMapping(path = "/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read an entity", notes = "Retrieve en entity identified by the Request-URI")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Response entity in body"), //
            @ApiResponse(code = 404, message = "No matching entity exists")})
    default DeferredResult<ResponseEntity<V>> read(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid) {
        throw new UnsupportedOperationException("Get an entity not implemented");
    }

    // PUT - Create (201) or Update (200)
    @RequestMapping(path = "/{uuid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update or create an entity", notes = "Requests that the enclosed entity be stored under the supplied Request-URI", produces =
            MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Response entity in body"), //
            @ApiResponse(code = 201, message = "Entity body is the resource that was created"), //
            @ApiResponse(code = 204, message = "Response entity body is empty"), //
            @ApiResponse(code = 404, message = "No matching entity exists")})
    default DeferredResult<ResponseEntity<Void>> upsert(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid,
                                                        @RequestBody V value) {
        throw new UnsupportedOperationException("Update an entity not implemented");
    }

    // POST - Create (201) - Return URL in location header
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create an entity", notes = "New / modified entity enclosed in the request as identified by the Request-URI", produces = MediaType
            .APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { //
            @ApiResponse(code = 201, message = "Entity body is the resource that was created"), //
            @ApiResponse(code = 204, message = "Response entity body is empty"), //
            @ApiResponse(code = 409, message = "The resource already exists")})
    default DeferredResult<ResponseEntity<Void>> create(@RequestBody V value) {
        throw new UnsupportedOperationException("Create an entity not implemented");
    }

    // DELETE (200)
    @RequestMapping(path = "/{uuid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete an entity", notes = "Requests that the server delete the resource identified by the Request-URI")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Response entity in body"), //
            @ApiResponse(code = 204, message = "Response entity body is empty"), //
            @ApiResponse(code = 404, message = "No matching entity exists"), //
            @ApiResponse(code = 410, message = "No matching entity exists (Permanent)")})
    default DeferredResult<ResponseEntity<Void>> delete(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid) {
        throw new UnsupportedOperationException("Delete an entity not implemented");
    }

    // HEAD (200) - Find Out When a Resource Was Last Modified
    @RequestMapping(path = "/{uuid}", method = RequestMethod.HEAD)
    @ApiOperation(value = "Find Out When a Resource Was Last Modified", notes = "As per GET but do not return body")
    @ApiResponses(value = { //
            @ApiResponse(code = 204, message = "Response entity body is empty"), //
            @ApiResponse(code = 404, message = "No matching entity exists")})
    default DeferredResult<ResponseEntity<Void>> head(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable UUID uuid) {
        throw new UnsupportedOperationException("Entity last modified not implemented");
    }

    // OPTIONS (200) - Querying the Available Operations on a Resource
    @RequestMapping(method = RequestMethod.OPTIONS)
    @ApiOperation(value = "Querying the Available Operations on a Resource", notes = "Request for information about the communication options available")
    default DeferredResult<ResponseEntity<Void>> options() {
        throw new UnsupportedOperationException("Available options not implemented");
    }

    // PATCH (200) - https://tools.ietf.org/html/rfc6902 and https://tools.ietf.org/html/rfc7396
    @RequestMapping(path = "/{uuid}", method = RequestMethod.PATCH, consumes = {"application/json-patch+json", "application/merge-patch+json"})
    @ApiOperation(value = "Request to modify an existing entity", notes = "Request to modify an existing entity")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Response entity in body"), //
            @ApiResponse(code = 204, message = "Response entity body is empty"), //
            @ApiResponse(code = 404, message = "No matching entity exists")})
    default DeferredResult<ResponseEntity<Void>> patch(@RequestBody V value) {
        throw new UnsupportedOperationException("Patch not implemented");
    }

}
