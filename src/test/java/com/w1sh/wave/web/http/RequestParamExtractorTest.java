package com.w1sh.wave.web.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestParamExtractorTest {

    @Test
    void should_returnIntegerValue_whenEndpointHasIntegerAsExpectedParameter() {
        Map<String, Class<?>> expectedParams = new HashMap<>();
        expectedParams.put("value", Integer.class);

        Map<String, Object> stringObjectMap = RequestParamExtractor.fromURI("http://localhost:8080/api?value=21", expectedParams);

        Assertions.assertNotNull(stringObjectMap);
        Assertions.assertEquals(21, stringObjectMap.get("value"));
    }

    @Test
    void should_returnLongValue_whenEndpointHasLongAsExpectedParameter() {
        Map<String, Class<?>> expectedParams = new HashMap<>();
        expectedParams.put("value", Long.class);

        Map<String, Object> stringObjectMap = RequestParamExtractor.fromURI("http://localhost:8080/api?value=21312", expectedParams);

        Assertions.assertNotNull(stringObjectMap);
        Assertions.assertEquals(21312L, stringObjectMap.get("value"));
    }

    @Test
    void should_returnBooleanValue_whenEndpointHasBooleanAsExpectedParameter() {
        Map<String, Class<?>> expectedParams = new HashMap<>();
        expectedParams.put("value", Boolean.class);

        Map<String, Object> stringObjectMap = RequestParamExtractor.fromURI("http://localhost:8080/api?value=true", expectedParams);

        Assertions.assertNotNull(stringObjectMap);
        Assertions.assertEquals(true, stringObjectMap.get("value"));
    }

    @Test
    void should_returnFloatValue_whenEndpointHasFloatAsExpectedParameter() {
        Map<String, Class<?>> expectedParams = new HashMap<>();
        expectedParams.put("value", Float.class);

        Map<String, Object> stringObjectMap = RequestParamExtractor.fromURI("http://localhost:8080/api?value=2.21", expectedParams);

        Assertions.assertNotNull(stringObjectMap);
        Assertions.assertEquals(2.21F, stringObjectMap.get("value"));
    }

    @Test
    void should_returnDoubleValue_whenEndpointHasDoubleAsExpectedParameter() {
        Map<String, Class<?>> expectedParams = new HashMap<>();
        expectedParams.put("value", Double.class);

        Map<String, Object> stringObjectMap = RequestParamExtractor.fromURI("http://localhost:8080/api?value=2.21", expectedParams);

        Assertions.assertNotNull(stringObjectMap);
        Assertions.assertEquals(2.21, stringObjectMap.get("value"));
    }

    @Test
    void should_returnShortValue_whenEndpointHasShortAsExpectedParameter() {
        Map<String, Class<?>> expectedParams = new HashMap<>();
        expectedParams.put("value", Short.class);

        Map<String, Object> stringObjectMap = RequestParamExtractor.fromURI("http://localhost:8080/api?value=221", expectedParams);

        Assertions.assertNotNull(stringObjectMap);
        Assertions.assertEquals((short) 221, stringObjectMap.get("value"));
    }

    @Test
    void should_returnByteValue_whenEndpointHasByteAsExpectedParameter() {
        Map<String, Class<?>> expectedParams = new HashMap<>();
        expectedParams.put("value", Byte.class);

        Map<String, Object> stringObjectMap = RequestParamExtractor.fromURI("http://localhost:8080/api?value=1", expectedParams);

        Assertions.assertNotNull(stringObjectMap);
        Assertions.assertEquals((byte) 1, stringObjectMap.get("value"));
    }
}