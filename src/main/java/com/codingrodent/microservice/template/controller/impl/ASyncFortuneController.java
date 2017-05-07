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

import com.codingrodent.microservice.template.controller.api.IAsyncREST;
import com.codingrodent.microservice.template.model.Fortune;
import com.codingrodent.microservice.template.service.api.IFortuneService;
import io.swagger.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.inject.Inject;
import java.util.UUID;

import static com.codingrodent.microservice.template.constants.SystemConstants.API_VERSION;

/**
 * Simple async REST controller
 */
@RestController
@Api(tags = "async", value = "asyncfortune", description = "Endpoint for fortune management (async)")
@RequestMapping("/async/fortune/" + API_VERSION)
public class ASyncFortuneController implements IAsyncREST<UUID, Fortune> {

    private final IFortuneService<Fortune> fortuneService;

    @Inject
    public ASyncFortuneController(final IFortuneService<Fortune> fortuneService) {
        this.fortuneService = fortuneService;
    }

    // GET (200)
    @Override
    public DeferredResult<ResponseEntity<Fortune>> read(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID uuid) {
        DeferredResult<ResponseEntity<Fortune>> result = new DeferredResult<>();
        fortuneService.loadAsync(uuid).subscribe(c -> result.setResult(new ResponseEntity<>(c, HttpStatus.OK)));
        return result;
    }

    // PUT - Create (201) or Update  (200)
    @Override
    public DeferredResult<ResponseEntity<Void>> upsert(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID uuid, @RequestBody
    final Fortune fortune) {
        DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();
        fortuneService.saveAsync(uuid, fortune).subscribe(c -> result.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
        return result;
    }

    // POST - Create (201) - Return URL in location header
    @Override
    public DeferredResult<ResponseEntity<Void>> create(@RequestBody final Fortune value) {
        DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();
        result.setResult(new ResponseEntity<>(HttpStatus.OK));
        return result;
    }

    // DELETE (200)
    @Override
    public DeferredResult<ResponseEntity<Void>> delete(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID uuid) {
        System.out.println("Delete: " + uuid);
        DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();
        result.setResult(new ResponseEntity<>(HttpStatus.OK));
        return result;
    }

}
