package com.codingrodent.microservice.template.converter;

import com.codingrodent.microservice.template.entity.FortuneEntity;
import com.codingrodent.microservice.template.model.Fortune;
import org.junit.Test;

import java.util.*;

import static com.codingrodent.microservice.template.converter.Converter.*;
import static org.junit.Assert.*;

public class ConverterTest {

    @Test
    public void fortuneTest() {
        UUID id = UUID.randomUUID();
        Long version = 12345L;
        String text = "A fortune";
        Optional<String> author = Optional.of("An author");

        FortuneEntity fortuneEntity = new FortuneEntity(id.toString(), text, author.orElse(""));
        //
        // Convert to model and check
        Fortune model = toNameModel.convert(fortuneEntity);
        assertEquals(text, model.getText());
        assertEquals(author, model.getAuthor());

        //
        // Convert back to entity and check
        fortuneEntity = toNameEntity.convert(id, model, Optional.of(version));
        assertEquals(id, fortuneEntity.getId());
        assertEquals(version.longValue(), fortuneEntity.getVersion());
        assertEquals(text, fortuneEntity.getText());
        assertEquals(author, fortuneEntity.getAuthor());

        //
        // Convert back to entity and check (No version information)
        fortuneEntity = toNameEntity.convert(id, model, Optional.empty());
        assertEquals(id, fortuneEntity.getId());
        assertNull(fortuneEntity.getVersion());
        assertEquals(text, fortuneEntity.getText());
        assertEquals(author, fortuneEntity.getAuthor());
    }

}