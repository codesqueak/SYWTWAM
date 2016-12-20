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
import com.codingrodent.microservice.template.model.Contact;
import com.codingrodent.microservice.template.service.api.IContactService;
import io.swagger.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.inject.Inject;
import java.util.UUID;

import static com.codingrodent.microservice.template.constants.SystemConstants.API_1;

/**
 * Simple async REST controller
 */
@RestController
@Api(value = "asynccontact", description = "Endpoint for contact management (async)")
@RequestMapping("/asynccontact" + API_1)
public class ASyncContactController implements IAsyncREST<UUID, Contact> {

    private IContactService contactService;

    @Inject
    public ASyncContactController(final IContactService contactService) {
        this.contactService = contactService;
    }

    // GET (200)
    @Override
    public DeferredResult<ResponseEntity<Contact>> read(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID
                                                                    uuid) {
        DeferredResult<ResponseEntity<Contact>> result = new DeferredResult<>();
        contactService.loadAsync(uuid).subscribe(e -> result.setResult(new ResponseEntity<>(e, HttpStatus.OK)));
        return result;
    }

    // PUT - Create (201) or Update  (200)
    @Override
    public DeferredResult<ResponseEntity<Void>> upsert(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID
                                                                   uuid, @RequestBody final Contact contact) {
        DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();
        contactService.saveAsync(contact).subscribe(e -> result.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
        return result;
    }

    // POST - Create (201) - Return URL in location header
    @Override
    public DeferredResult<ResponseEntity<Void>> create(@RequestBody final Contact value) {
        DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();
        result.setResult(new ResponseEntity<>(HttpStatus.OK));
        return result;
    }

    // DELETE (200)
    @Override
    public DeferredResult<ResponseEntity<Void>> delete(@ApiParam(name = "uuid", value = "Unique identifier UUID", required = true) @PathVariable final UUID
                                                                   uuid) {
        System.out.println("Delete: " + uuid);
        DeferredResult<ResponseEntity<Void>> result = new DeferredResult<>();
        result.setResult(new ResponseEntity<>(HttpStatus.OK));
        return result;
    }

}
