package com.w1sh.stardust.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesParserImpl implements PropertiesParser {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesParserImpl.class);

    @Override
    public Map<String, String> parse(InputStream inputStream) {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            Map<String, String> propertiesMap = new HashMap<>();
            for (Map.Entry<Object, Object> e : properties.entrySet()) {
                if (e.getKey() instanceof String key && e.getValue() instanceof String value) {
                    propertiesMap.put(key, value);
                }
            }
            return propertiesMap;
        } catch (IOException e) {
            logger.error("Failed to load and register properties file", e);
        }
        return new HashMap<>();
    }
}
