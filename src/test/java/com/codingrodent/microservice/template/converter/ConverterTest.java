package com.codingrodent.microservice.template.converter;

import com.codingrodent.microservice.template.entity.ContactEntity;
import com.codingrodent.microservice.template.model.Contact;
import org.junit.Test;

import java.util.*;

import static com.codingrodent.microservice.template.converter.Converter.*;
import static org.junit.Assert.*;

public class ConverterTest {

    @Test
    public void contactTest() {
        UUID id = UUID.randomUUID();
        Long version = 12345L;
        String firstName = "first";
        String lastName = "last";
        Integer age = 100;
        String phone = "1-2-3-4-5";
        String mobile = "6-7-8-9";
        String country = "US";
        ContactEntity contactEntity = new ContactEntity(id.toString(), firstName, lastName, age, phone, mobile, country, version);
        //
        // Convert to model and check
        Contact model = toNameModel.convert(contactEntity);
        assertEquals(firstName, model.getFirstName());
        assertEquals(lastName, model.getLastName());
        assertEquals(age, model.getAge());
        assertEquals(phone, model.getPhone());
        assertEquals(mobile, model.getMobile());
        assertEquals(country, model.getCountry());
        //
        // Convert back to entity and check
        contactEntity = toNameEntity.convert(id, model, Optional.of(version));
        assertEquals(id, contactEntity.getId());
        assertEquals(version, contactEntity.getVersion());
        assertEquals(firstName, contactEntity.getFirstName());
        assertEquals(lastName, contactEntity.getLastName());
        assertEquals(age, contactEntity.getAge());
        assertEquals(phone, contactEntity.getPhone());
        assertEquals(mobile, contactEntity.getMobile());
        assertEquals(country, contactEntity.getCountry());
        //
        // Convert back to entity and check (No version information)
        contactEntity = toNameEntity.convert(id, model, Optional.empty());
        assertEquals(id, contactEntity.getId());
        assertNull(contactEntity.getVersion());
        assertEquals(firstName, contactEntity.getFirstName());
        assertEquals(lastName, contactEntity.getLastName());
        assertEquals(age, contactEntity.getAge());
        assertEquals(phone, contactEntity.getPhone());
        assertEquals(mobile, contactEntity.getMobile());
        assertEquals(country, contactEntity.getCountry());
    }

}