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
package com.codingrodent.microservice.template.matchers;

import org.hamcrest.*;

import java.util.regex.Pattern;

/**
 * Custom matcher to check for strong etags with numeric only content (used as optimistic locking version)
 */
public class ETagNumericMatcher extends TypeSafeDiagnosingMatcher<String> {
    private final Pattern pattern = Pattern.compile("\"-?+\\d++\"");

    @Override
    protected boolean matchesSafely(String etag, Description description) {
        return pattern.matcher(etag).matches();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("A numeric only ETag");
    }

    public static ETagNumericMatcher isETagNumeric() {
        return new ETagNumericMatcher();
    }
}