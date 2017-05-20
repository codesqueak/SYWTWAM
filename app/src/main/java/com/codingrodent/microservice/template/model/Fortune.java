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
package com.codingrodent.microservice.template.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.*;

import javax.validation.constraints.*;
import java.util.*;

/**
 * Fortune model class
 */
@ApiModel(description = "Sample fortune model item")
public class Fortune extends ModelBase {

    @JsonProperty("text")
    @NotNull
    @Size(min = 5, max = 250)
    private final String text;

    @JsonProperty("author")
    private final Optional<String> author;

    @JsonCreator
    public Fortune(@JsonProperty("text") final String text, @JsonProperty("author") final Optional<String> author) {
        this(text, author, Optional.empty());
    }

    public Fortune(final String text, final Optional<String> author, final Optional<UUID> uuid) {
        super(uuid);
        this.text = text;
        this.author = author;
    }

    @ApiModelProperty(required = true, value = "Fortune cookie text")
    public String getText() {
        return text;
    }

    @ApiModelProperty(required = true, value = "Author (if available)")
    public Optional<String> getAuthor() {
        return author;
    }

}
