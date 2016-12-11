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

import com.codingrodent.microservice.template.controller.spec.ICRUD;
import com.codingrodent.microservice.template.model.Name;
import com.codingrodent.microservice.template.service.spec.INameService;
import io.swagger.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Simple sync REST controller
 */
@RestController
@Api(value = "syncname", description = "Endpoint for name management")
@RequestMapping("/syncname")
public class SyncNameController implements ICRUD<UUID, Name> {

    private INameService nameService;

    @Inject
    public SyncNameController(final INameService nameService) {
        this.nameService = nameService;
    }

    // GET (200)
    @Override
    public ResponseEntity<Name> read(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID uuid) {
        Name name = nameService.load(uuid);
        if (null == name)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(name, HttpStatus.OK);
    }

    // PUT - Create (201) or Update  (200)
    @Override
    public ResponseEntity<Void> upsert(@RequestBody final Name name) {
        nameService.save(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // POST - Create (201) - Return URL in location header
    @Override
    public ResponseEntity<Void> create(@RequestBody final Name value) {
        throw new UnsupportedOperationException("Get an entity not implemented");
    }

    // DELETE = Delete (200)
    @Override
    public ResponseEntity<Void> delete(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID uuid) {
        throw new UnsupportedOperationException("Get an entity not implemented");
    }
}
