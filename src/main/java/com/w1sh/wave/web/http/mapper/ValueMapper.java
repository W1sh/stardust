package com.w1sh.wave.web.http.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ValueMapper {

    private static final Logger logger = LoggerFactory.getLogger(ValueMapper.class);
    private static final Map<Class<?>, Mapper<?>> CLASS_VALUE_MAPPER_MAP = new HashMap<>();

    static {
        CLASS_VALUE_MAPPER_MAP.put(Long.class, Long::parseLong);
        CLASS_VALUE_MAPPER_MAP.put(Integer.class, Integer::parseInt);
        CLASS_VALUE_MAPPER_MAP.put(Boolean.class, Boolean::parseBoolean);
        CLASS_VALUE_MAPPER_MAP.put(Float.class, Float::parseFloat);
        CLASS_VALUE_MAPPER_MAP.put(Double.class, Double::parseDouble);
        CLASS_VALUE_MAPPER_MAP.put(Short.class, Short::parseShort);
        CLASS_VALUE_MAPPER_MAP.put(Byte.class, Byte::parseByte);
    }

    private ValueMapper() {
    }

    public static Object fromString(String value, Class<?> expectedClass) {
        if (hasMapper(expectedClass)) {
            return CLASS_VALUE_MAPPER_MAP.get(expectedClass).map(value);
        } else {
            logger.error("Class {} does not have a mapper defined", expectedClass);
            return null;
        }
    }

    public static <T> void addMapper(Mapper<T> mapper, Class<T> clazz) {
        CLASS_VALUE_MAPPER_MAP.put(clazz, mapper);
    }

    public static boolean hasMapper(Class<?> clazz) {
        return CLASS_VALUE_MAPPER_MAP.containsKey(clazz);
    }
}
