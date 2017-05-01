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
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Specific implementation methods for the fortune service
 */
public interface IFortune<K, V> extends IREST<K, V> {

    // Collections

    /**
     * GET - Requests data from a specified resource
     *
     * @param page Data page to read
     * @param size Size of page
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @RequestMapping(path = "/list/named", params = {"page", "size"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read all entities", notes = "Retrieve all entities in a paged manner if required", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "OK, response entity in body"), //
            @ApiResponse(code = 304, message = "Not modified"), //
            @ApiResponse(code = 410, message = "No matching entity exists"), //
            @ApiResponse(code = 412, message = "Precondition Failed")})
    default ResponseEntity<List<Resource<V>>> listNamed(@ApiParam(name = "page", value = "Page to retrieve", required = true) @RequestParam int page, //
                                                        @ApiParam(name = "size", value = "Items per page", required = true) @RequestParam int size) {
        throw new UnsupportedOperationException("List named not implemented");
    }

    /**
     * GET - Requests data from a specified resource
     *
     * @param page Data page to read
     * @param size Size of page
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @RequestMapping(path = "/list/anon", params = {"page", "size"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read all entities", notes = "Retrieve all entities in a paged manner if required", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "OK, response entity in body"), //
            @ApiResponse(code = 304, message = "Not modified"), //
            @ApiResponse(code = 410, message = "No matching entity exists"), //
            @ApiResponse(code = 412, message = "Precondition Failed")})
    default ResponseEntity<List<Resource<V>>> listAnon(@ApiParam(name = "page", value = "Page to retrieve", required = true) @RequestParam int page, //
                                                       @ApiParam(name = "size", value = "Items per page", required = true) @RequestParam int size) {
        throw new UnsupportedOperationException("List anonymous not implemented");
    }

}
