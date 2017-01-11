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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContactTest {
    @Test
    public void basicTest() {
        String firstName = "first";
        String lastName = "last";
        Integer age = 100;
        String phone = "1-2-3-4-5";
        String mobile = "6-7-8-9";
        String country = "US";
        Contact contactEntity = new Contact(firstName, lastName, age, phone, mobile, country);
        //
        assertEquals(firstName, contactEntity.getFirstName());
        assertEquals(lastName, contactEntity.getLastName());
        assertEquals(age, contactEntity.getAge());
        assertEquals(phone, contactEntity.getPhone());
        assertEquals(mobile, contactEntity.getMobile());
        assertEquals(country, contactEntity.getCountry());
    }
}