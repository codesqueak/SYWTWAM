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
package com.codingrodent.microservice.template.entity;

import com.couchbase.client.java.repository.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.*;

/**
 * Contact persistence class
 */
@Document
public class FortuneEntity {

    @Id
    private final UUID id;
    @Version
    private Long version;
    @Field
    private final String text;
    @Field
    private final String author;

    @JsonCreator
    public FortuneEntity(@JsonProperty("id") final String id, @JsonProperty("text") final String text, @JsonProperty("author") final Optional<String> author, @JsonProperty
            ("version") final Long version) {
        this.id = UUID.fromString(id);
        this.text = text;
        this.author = author.orElse("");
        this.version = version;
    }

    public String getText() {
        return text;
    }

    public Optional<String> getAuthor() {
        return (author.equals("")) ? Optional.empty() : Optional.of(author);
    }

    public Long getVersion() {
        return version;
    }

    public UUID getId() {
        return id;
    }

}
