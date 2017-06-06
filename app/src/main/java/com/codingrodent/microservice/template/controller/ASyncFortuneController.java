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

import com.codingrodent.microservice.template.api.IAsyncFortune;
import com.codingrodent.microservice.template.model.Fortune;
import com.codingrodent.microservice.template.service.api.IAsyncFortuneService;
import io.swagger.annotations.Api;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import javax.inject.Inject;
import java.util.*;

import static com.codingrodent.microservice.template.constants.SystemConstants.API_VERSION;

/**
 * Simple async REST controller
 */
@RestController
@Api(tags = "async", value = "asyncfortune", description = "Endpoint for fortune management (async)")
@RequestMapping("/async/fortune/" + API_VERSION)
public class ASyncFortuneController implements IAsyncFortune<UUID, Fortune> {

    private final IAsyncFortuneService<Fortune> fortuneService;

    @Inject
    public ASyncFortuneController(final IAsyncFortuneService<Fortune> fortuneService) {
        this.fortuneService = fortuneService;
    }

    /**
     * GET - Requests data from a specified resource
     *
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @Override
    public DeferredResult<List<Resource<Fortune>>> listAll() {
        DeferredResult<List<Resource<Fortune>>> result = new DeferredResult<>();
        Observable<LinkedList<Resource<Fortune>>> o = fortuneService.findAllAsync().map(f -> new Resource<>(f)).collect(() -> new LinkedList<Resource<Fortune>>(),
                                                                                                                        LinkedList::add).first();
        o.subscribe(result::setResult);
        return result;
    }

    /**
     * GET - Requests data from a specified resource
     *
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @Override
    public DeferredResult<List<Resource<Fortune>>> listAnon() {
        DeferredResult<List<Resource<Fortune>>> result = new DeferredResult<>();
        Observable<LinkedList<Resource<Fortune>>> o = fortuneService.findAnonAsync().map(f -> new Resource<>(f)).collect(() -> new LinkedList<Resource<Fortune>>(),
                                                                                                                         LinkedList::add).first();
        o.subscribe(result::setResult);
        return result;
    }

    /**
     * GET - Requests data from a specified resource
     *
     * @return Return selected entity or 'Not Modified' if version matched
     */
    @Override
    public DeferredResult<List<Resource<Fortune>>> listNamed() {
        DeferredResult<List<Resource<Fortune>>> result = new DeferredResult<>();
        Observable<LinkedList<Resource<Fortune>>> o = fortuneService.findNamedAsync().map(f -> new Resource<>(f)).collect(() -> new LinkedList<Resource<Fortune>>(),
                                                                                                                          LinkedList::add).first();
        o.subscribe(result::setResult);
        return result;
    }
}
