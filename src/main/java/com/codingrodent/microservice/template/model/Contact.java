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

/**
 * Contact model class
 */
@ApiModel(description = "Sample contact model item")
public class Contact {

    @JsonProperty("firstname")
    @NotNull
    @Size(min = 5, max = 10)
    private final String firstName;
    @JsonProperty("lastname")
    @NotNull
    @Size(min = 5, max = 10)
    private final String lastName;
    @JsonProperty("age")
    @NotNull
    private final Integer age;
    @JsonProperty("phone")
    private final String phone;
    @JsonProperty("mobile")
    private final String mobile;
    @JsonProperty("country")
    @NotNull
    private final String country;

    @JsonCreator
    public Contact(@JsonProperty("firstname") final String firstName, @JsonProperty("lastname") final String lastName, @JsonProperty("age") final Integer
            age, @JsonProperty("phone") final String phone, @JsonProperty("mobile") final String mobile, @JsonProperty("country") final String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phone = phone;
        this.mobile = mobile;
        this.country = country;
    }

    @ApiModelProperty(required = true, value = "Contact first name")
    public String getFirstName() {
        return firstName;
    }

    @ApiModelProperty(required = true, value = "Contact last name")
    public String getLastName() {
        return lastName;
    }

    @ApiModelProperty(required = true, value = "Age")
    public Integer getAge() {
        return age;
    }

    @ApiModelProperty(value = "Phone number (landline)")
    public String getPhone() {
        return phone;
    }

    @ApiModelProperty(value = "Phone number (mobile)")
    public String getMobile() {
        return mobile;
    }

    @ApiModelProperty(required = true, value = "Country of residence")
    public String getCountry() {
        return country;
    }

}
