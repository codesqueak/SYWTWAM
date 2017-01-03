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
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
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
    public ResponseEntity<Object> fault(HttpServletRequest req, UnsupportedOperationException ex) {
        return getResponseEntity(req, ex, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<Object> fault(HttpServletRequest req, DocumentNotFoundException ex) {
        return getResponseEntity(req, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> fault(HttpServletRequest req, ConflictException ex) {
        return getResponseEntity(req, ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public ResponseEntity<Object> fault(HttpServletRequest req, HystrixRuntimeException ex) {
        return getResponseEntity(req, ex, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> fault(HttpServletRequest req, IllegalArgumentException ex) {
        return getResponseEntity(req, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> fault(HttpServletRequest req, MethodArgumentTypeMismatchException ex) {
        return getResponseEntity(req, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Object> fault(HttpServletRequest req, OptimisticLockingFailureException ex) {
        return getResponseEntity(req, ex, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * Fallback for all unhandled exceptions. Generates a 500 response
     *
     * @param req HTTP request
     * @param ex  Exception being handled
     * @return 500 error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> fault(HttpServletRequest req, RuntimeException ex) {
        return getResponseEntity(req, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Customized the response for HttpMessageNotReadableException. Usually arises from an invalid parameter being passed into a RESTful interface
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response (Not used)
     * @param status  the selected response status (Not used)
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest
            request) {
        return getResponseEntity(request, ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Customized the response for MethodArgumentNotValid.  Usually arises from JSR303 validation failure.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response (Not used)
     * @param status  the selected response status (Not used)
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest
            request) {
        return getResponseEntity(request, ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Generate a standard response if the request is a ServletWebRequest else generate a custom response
     * <p>
     * param request the current request
     *
     * @param ex     the exception
     * @param status the selected response status
     * @return a {@code ResponseEntity} instance
     */
    private ResponseEntity<Object> getResponseEntity(WebRequest request, Exception ex, HttpStatus status) {
        if (request instanceof ServletWebRequest) {
            // Should be this type
            return getResponseEntity(((ServletWebRequest) request).getRequest(), ex, status);
        } else {
            // but just in case it isn't, fall back to this
            return new ResponseEntity<>(new RestCallErrorInfo(ex.getMessage(), status, ""), status);
        }
    }

    /**
     * Helper method to make response entity
     *
     * @param request Servlet request
     * @param ex      Exception
     * @param status  Resulting HTTP status code
     * @return Response entity wrapping standard error data structure
     */
    private ResponseEntity<Object> getResponseEntity(HttpServletRequest request, Exception ex, HttpStatus status) {
        return new ResponseEntity<>(new RestCallErrorInfo(ex.getMessage(), status, request.getRequestURI()), status);
    }

}
