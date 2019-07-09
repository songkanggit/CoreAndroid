package com.guohe.corecenter.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by kousou on 2018/12/13.
 */

public class JacksonUtil {
    public static final <T>T readValue(String from, Class<T> toValueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(DateTimeUtil.YYYY_MM_DD_HH_MM_SS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(from, toValueType);
    }

    public static final <T>T convertValue(Object from, Class<T> toValueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(DateTimeUtil.YYYY_MM_DD_HH_MM_SS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(from, toValueType);
    }

    public static final <T>List<T> convertStringToList(final String value, Class<T> toValueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(value, mapper.getTypeFactory().constructType(List.class, toValueType));
    }

    public static final String convertObjectToString(final Object object, boolean add) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
//        if(add) {
//            return "'" + mapper.writeValueAsString(object) + "'";
//        } else {
//
//        }
        return mapper.writeValueAsString(object);
    }
}
