package com.w1sh.wave.web.http.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueMapperTest {

    @Test
    void should_returnIntegerValue_whenEndpointHasIntegerAsExpectedParameter() {
        Object object = ValueMapper.fromString("21", Integer.class);

        assertNotNull(object);
        assertEquals(Integer.class, object.getClass());
        assertEquals(21, object);
    }

    @Test
    void should_returnLongValue_whenEndpointHasLongAsExpectedParameter() {
        Object object = ValueMapper.fromString("21", Long.class);

        assertNotNull(object);
        assertEquals(Long.class, object.getClass());
        assertEquals(21L, object);
    }

    @Test
    void should_returnBooleanValue_whenEndpointHasBooleanAsExpectedParameter() {
        Object object = ValueMapper.fromString("true", Boolean.class);

        assertNotNull(object);
        assertEquals(Boolean.class, object.getClass());
        assertEquals(true, object);
    }

    @Test
    void should_returnFloatValue_whenEndpointHasFloatAsExpectedParameter() {
        Object object = ValueMapper.fromString("21.1", Float.class);

        assertNotNull(object);
        assertEquals(Float.class, object.getClass());
        assertEquals(21.1F, object);
    }

    @Test
    void should_returnDoubleValue_whenEndpointHasDoubleAsExpectedParameter() {
        Object object = ValueMapper.fromString("21.1", Double.class);

        assertNotNull(object);
        assertEquals(Double.class, object.getClass());
        assertEquals(21.1, object);
    }

    @Test
    void should_returnShortValue_whenEndpointHasShortAsExpectedParameter() {
        Object object = ValueMapper.fromString("211", Short.class);

        assertNotNull(object);
        assertEquals(Short.class, object.getClass());
        assertEquals((short) 211, object);
    }

    @Test
    void should_returnByteValue_whenEndpointHasByteAsExpectedParameter() {
        Object object = ValueMapper.fromString("1", Byte.class);

        assertNotNull(object);
        assertEquals(Byte.class, object.getClass());
        assertEquals((byte) 1, object);
    }

    @Test
    void should_returnNull_whenNoMapperExistsForClass() {
        Object object = ValueMapper.fromString("1", NoMapperTestValue.class);

        assertNull(object);
    }

    @Test
    void should_addMapper_whenProvidedMapperIsNotNull() {
        Mapper<TestValue> testMapper = TestValue::new;
        ValueMapper.addMapper(testMapper, TestValue.class);

        Object object = ValueMapper.fromString("value", TestValue.class);

        assertNotNull(object);
        assertEquals(TestValue.class, object.getClass());
        assertEquals("value", ((TestValue) object).value);
    }

    @Test
    void should_returnTrue_whenMapperExistsForClass() {
        boolean exists = ValueMapper.hasMapper(Byte.class);

        assertTrue(exists);
    }

    private record TestValue(String value) {
    }

    private record NoMapperTestValue(String value) {
    }
}