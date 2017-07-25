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

import com.codingrodent.microservice.template.metrics.api.ITemplateMetrics;
import io.swagger.annotations.*;
import org.springframework.core.SpringVersion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.*;

import static com.codingrodent.microservice.template.constants.SystemConstants.*;
import static org.springframework.http.HttpMethod.*;

/**
 * Simple sync REST controller to return version information
 */

@RestController
@Api(tags = "version", value = "version", description = "Endpoint for version Information - Don't do this in a production system as it gives away too much information")
@RequestMapping("/version/" + API_VERSION)
public class VersionController {

    private final static Set<HttpMethod> ALLOWED_OPTIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(GET, OPTIONS)));

    private final static Map<String, String> versions;
    private final static ResponseEntity<Map<String, String>> getResponse;
    private final static ResponseEntity<Void> optionsResponse;

    static {
        versions = new HashMap<>();
        versions.put("Java", System.getProperty("java.version"));
        versions.put("Spring", SpringVersion.getVersion());
        versions.put("Undertow", io.undertow.Version.getFullVersionString());
        // Default response
        getResponse = new ResponseEntity<>(versions, HttpStatus.OK);
        // Default http headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAllow(ALLOWED_OPTIONS);
        optionsResponse = new ResponseEntity<>(headers, HttpStatus.OK);
    }

    private final ITemplateMetrics metrics;

    @Inject
    public VersionController(final ITemplateMetrics metrics) {
        this.metrics = metrics;
    }

    // GET (200) - Recover version information
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read version information", notes = "Version Information - For Demo Only")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Version information in body")})
    public ResponseEntity<Map<String, String>> read() {
        metrics.inc(METRIC_VERSION_GET);
        return getResponse;
    }

    // OPTIONS (200)
    @RequestMapping(method = RequestMethod.OPTIONS)
    @ApiOperation(value = "Querying the Available Operations", notes = "Request for information about the communication options available")
    public ResponseEntity<Void> options() {
        metrics.inc(METRIC_VERSION_OPTIONS);
        return optionsResponse;
    }
}
