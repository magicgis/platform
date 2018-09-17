package com.junl.wpwx.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.expression.ParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;



public class CustomJsonDateDeserializer extends JsonDeserializer<Date> {  
  
    @Override  
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                String date = jp.getText();  
                try {  
                    return format.parse(date);  
                } catch (ParseException | java.text.ParseException e) {  
                    throw new RuntimeException(e);  
                }  
    }  
  
}  