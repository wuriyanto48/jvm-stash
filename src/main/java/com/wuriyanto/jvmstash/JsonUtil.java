package com.wuriyanto.jvmstash;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/*
 * JsonUtil
 * helper class for Marshal and Unmarshal POJO class
 */
public class JsonUtil {

    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // ignore this, to avoid sending data as partial divide by line
            //mapper.enable(SerializationFeature.INDENT_OUTPUT);

            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOEXception while mapping object (" + data + ") to JSON");
        }
    }

    public static <T> T jsonToData(Class<T> valueType, byte[] input) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        T t = mapper.readValue(input, valueType);
        return t;
    }
}
