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

package com.codingrodent.microservice.template.config.advice;

import com.codingrodent.microservice.template.exception.*;
import com.codingrodent.microservice.template.utility.RestCallErrorInfo;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Class to handle all REST exception processing
 * <p>
 * Add exception(s) to be handled here and translate into required HTTP response
 */
@ControllerAdvice
public class RestAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseBody
    public ResponseEntity<RestCallErrorInfo> fault(HttpServletRequest req, UnsupportedOperationException ex) {
        return getResponseEntity(req, ex, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    @ResponseBody
    public ResponseEntity<RestCallErrorInfo> fault(HttpServletRequest req, DocumentNotFoundException ex) {
        return getResponseEntity(req, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseBody
    public ResponseEntity<RestCallErrorInfo> fault(HttpServletRequest req, ConflictException ex) {
        return getResponseEntity(req, ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    @ResponseBody
    public ResponseEntity<RestCallErrorInfo> fault(HttpServletRequest req, HystrixRuntimeException ex) {
        return getResponseEntity(req, ex, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<RestCallErrorInfo> fault(HttpServletRequest req, IllegalArgumentException ex) {
        return getResponseEntity(req, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<RestCallErrorInfo> fault(HttpServletRequest req, MethodArgumentTypeMismatchException ex) {
        return getResponseEntity(req, ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Fallback for all unhandled esceptions. Generates a 500 repsonse
     *
     * @param req HTTP eequest
     * @param ex  Exception being handled
     * @return 500 error reponse
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<RestCallErrorInfo> fault(HttpServletRequest req, RuntimeException ex) {
        ex.printStackTrace();
        return getResponseEntity(req, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Customize the response for HttpMessageNotReadableException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest
            request) {

        return handleExceptionInternal(null, null, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Helper method to make response entity
     *
     * @param request Servlet request
     * @param ex      Exception
     * @param status  Resulting HTTP status code
     * @return Reponse entity wrapping standard error data structure
     */
    private ResponseEntity<RestCallErrorInfo> getResponseEntity(HttpServletRequest request, Exception ex, HttpStatus status) {
        return new ResponseEntity<>(new RestCallErrorInfo(ex.getMessage(), status, request.getRequestURI()), status);
    }

}
