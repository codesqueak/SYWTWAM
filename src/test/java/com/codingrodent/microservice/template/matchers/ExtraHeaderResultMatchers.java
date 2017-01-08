package com.codingrodent.microservice.template.matchers;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;

import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 *
 */
public class ExtraHeaderResultMatchers {

    /**
     * Access to extra HTTP response matchers.
     */
    public static ExtraHeaderResultMatchers extra() {
        return new ExtraHeaderResultMatchers();
    }

    /**
     * Assert the returned allowed options match those expected
     */
    public ResultMatcher options(final String required) {
        return (result) -> {
            assertEquals("Allowed header", pack(required), pack(result.getResponse().getHeader(HttpHeaders.ALLOW)));
        };
    }

    private String pack(String options) {
        StringBuilder sb = new StringBuilder(32);
        Arrays.stream(options.split(",")).map(String::trim).sorted().forEach(p -> sb.append(p).append(' '));
        return sb.toString();
    }

}
