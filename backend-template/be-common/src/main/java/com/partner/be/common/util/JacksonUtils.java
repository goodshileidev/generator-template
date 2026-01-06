package com.partner.be.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.google.common.collect.Lists;
import com.partner.be.common.ApiConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * JacksonUtils: Jackson JSON operation utility class
 *
 * Provides utility methods for JSON serialization and deserialization using Jackson library.
 * Includes methods for converting objects to JSON strings, parsing JSON to objects/maps,
 * and handling JSON nodes.
 *
 * @author ${author}
 * @time 2017-10-31 19:43:57
 */
@Slf4j
public class JacksonUtils {

    /**
     * ObjectMapper instance for JSON operations - thread-safe for mapping only
     */
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonFactory jsonFactory = new JsonFactory();

    static {
        // Do not write null values
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        // Entity property mapping does not need to be one-to-one
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Null properties do not participate in serialization
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // objectMapper.getDeserializationConfig().with(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        objectMapper.getSerializationConfig().with(new SimpleDateFormat(ApiConstants.Formats.YYYYMMDD_HHMMSS));
        objectMapper.getSerializationConfig().without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat(ApiConstants.Formats.YYYYMMDD_HHMMSS));
    }

    /**
     * Convert JSON string to object
     *
     * @param jsonAsString JSON string to parse
     * @param pojoClass target class type
     * @return <T> parsed object of specified type
     * @throws JsonMappingException if JSON mapping fails
     * @throws JsonParseException if JSON parsing fails
     * @throws IOException if I/O error occurs
     * @author: ${author}
     * @time 2017-11-01 10:42:19
     */
    public static <T> T fromJson(String jsonAsString, Class<T> pojoClass) {
        try {
            return objectMapper.readValue(jsonAsString, pojoClass);
        } catch (Exception e) {
            log.error("JSON conversion exception in fromJson: jsonAsString = " + jsonAsString, e);
            return null;
        }
    }

    /**
     * Convert JSON data stream to object
     *
     * @param fr FileReader containing JSON data
     * @param pojoClass target class type
     * @return <T> parsed object of specified type
     * @throws JsonMappingException if JSON mapping fails
     * @throws JsonParseException if JSON parsing fails
     * @throws IOException if I/O error occurs
     * @author: ${author}
     * @time 2017-11-01 10:42:19
     */
    public static <T> T fromJson(FileReader fr, Class<T> pojoClass) throws JsonParseException, IOException {
        return objectMapper.readValue(fr, pojoClass);
    }

    /**
     * Convert object to JSON string
     *
     * @param pojo the object to convert
     * @return JSON string representation of the object
     * @throws JsonMappingException if JSON mapping fails
     * @throws JsonGenerationException if JSON generation fails
     * @throws IOException if I/O error occurs
     * @author: ${author}
     * @time 2017-11-01 10:41:53
     */
    public static String toJson(Object pojo) {
        String jsonStr = "";
        try {
            jsonStr = toJson(pojo, false);
        } catch (Exception e) {
            log.error("JSON conversion exception", e);
        }
        return jsonStr;
    }

    public static String toJson(Object pojo, boolean prettyPrint) throws JsonMappingException, JsonGenerationException,
            IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
        if (prettyPrint) {
            jg.useDefaultPrettyPrinter();
        }
        objectMapper.writeValue(jg, pojo);
        return sw.toString();
    }

    public static void toJson(Object pojo, FileWriter fw, boolean prettyPrint) throws JsonMappingException,
            JsonGenerationException, IOException {
        JsonGenerator jg = jsonFactory.createJsonGenerator(fw);
        if (prettyPrint) {
            jg.useDefaultPrettyPrinter();
        }
        objectMapper.writeValue(jg, pojo);
    }

    /**
     * Convert JSON string to Map
     *
     * @param jsonStr JSON string to parse
     * @return Map containing the parsed JSON data
     * @throws IOException if I/O error occurs
     * @author: ${author}
     * @time 2018-04-03 10:41:25
     */
    public static Map<String, Object> parseMap(String jsonStr) {
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(jsonStr, Map.class);
        } catch (Exception e) {
            log.error("JSON conversion exception in parseMap, jsonStr = " + jsonStr, e);
        }
        return map;
    }

    /**
     * Convert JSON string to JsonNode
     *
     * @param jsonStr JSON string to parse
     * @return JsonNode representing the parsed JSON structure
     * @throws IOException if I/O error occurs
     * @author: ${author}
     * @time 2018-04-03 10:41:25
     */
    public static JsonNode parse(String jsonStr) {
        JsonNode node = null;
        try {
            node = objectMapper.readTree(jsonStr);
        } catch (Exception e) {
            log.error("JSON conversion exception in parse, jsonStr = " + jsonStr, e);
        }
        return node;
    }


    /**
     * Get string value from JsonNode
     *
     * @param node JsonNode to extract value from
     * @return String value of the node, or null if node is null
     * @author: ${author}
     * @time 2017-11-02 18:41:25
     */
    public static String getStrVal(JsonNode node) {
        if (node == null) {
            return null;
        }
        return node.asText();
    }

    /**
     * Convert JSON to List
     *
     * @param jsonInput JSON string to parse
     * @param tClass class type for list elements
     * @return List of objects parsed from JSON
     */
    public static <T> List<T> fromJsonToList(String jsonInput, Class<T> tClass) {
        List<T> objects = Lists.newArrayList();
        try {
            objects = objectMapper.readValue(jsonInput, objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
        } catch (IOException e) {
            log.error("JSON conversion exception in fromJsonToList, jsonStr = " + jsonInput, e);
        }
        return objects;
    }

    /**
     * Get ObjectMapper instance
     *
     * @return The shared ObjectMapper instance configured for this utility
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
