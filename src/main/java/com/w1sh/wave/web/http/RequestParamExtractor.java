package com.w1sh.wave.web.http;

import com.w1sh.wave.web.http.converter.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestParamExtractor {

    private static final Logger logger = LoggerFactory.getLogger(RequestParamExtractor.class);

    private static final Map<Class<?>, Converter<?>> CONVERTERS = new HashMap<>();

    static {
        CONVERTERS.put(Long.class, Long::parseLong);
        CONVERTERS.put(Integer.class, Integer::parseInt);
        CONVERTERS.put(Boolean.class, Boolean::parseBoolean);
        CONVERTERS.put(Float.class, Float::parseFloat);
        CONVERTERS.put(Double.class, Double::parseDouble);
        CONVERTERS.put(Short.class, Short::parseShort);
        CONVERTERS.put(Byte.class, Byte::parseByte);
    }

    private RequestParamExtractor(){}

    public static Map<String, Object> fromURI(String uri, Map<String, Class<?>> expectedParams) {
        final var convertedParams = new HashMap<String, Object>();
        String params = uri.substring(uri.indexOf("?") + 1);
        if (!params.isBlank()) {
            final var requestParams = params.split("&");
            for (String requestParam : requestParams) {
                final var param = requestParam.split("=");
                final var paramKey = param[0];
                final var paramValue = param[1];
                Class<?> expectedValueClass = expectedParams.get(paramKey);
                if (expectedValueClass != null) {
                    Object convertedValue = CONVERTERS.get(expectedValueClass).convert(paramValue);
                    convertedParams.put(paramKey, convertedValue);
                } else {
                    logger.warn("Request contained parameter {} which is not expected", paramKey);
                }
            }
        }
        return convertedParams;
    }
}
