/*
 * MIT License
 *
 * Copyright (c) 2017
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

import org.springframework.web.context.request.*;
import rx.*;

/**
 * Operator to save away request attributes so they can be restored to a different thread
 * <p>
 * This is required for HATEOAS to work as the thread at response generation is unlikely to be that on which the request arrived
 * <p>
 * Use this inside a lift() operator
 */
public class SaveStateOperator<T> implements Observable.Operator<T, T> {

    private final RequestAttributes requestAttributes;

    /**
     * Grab the state of the request attributes at construction time.  This makes them available for restoration at any point in the future on whatever thread is calling the
     * onNext() method
     */
    public SaveStateOperator() {
        requestAttributes = RequestContextHolder.currentRequestAttributes();
    }

    @Override
    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override
            public void onNext(T t) {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                subscriber.onNext(t);
            }
        };
    }
}