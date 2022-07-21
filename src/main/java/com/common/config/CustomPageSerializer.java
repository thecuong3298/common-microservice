package com.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

@JsonComponent
public class CustomPageSerializer extends JsonSerializer<Page<?>> {

    @Override
    public void serialize(Page page, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("number", page.getNumber());
        jsonGenerator.writeNumberField("size", page.getSize());
        jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
        jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
        jsonGenerator.writeArrayFieldStart("content");
        for (Object object : page.getContent()) {
            jsonGenerator.writeObject(object);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }

}
