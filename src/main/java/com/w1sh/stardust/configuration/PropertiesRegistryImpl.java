package com.w1sh.stardust.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesRegistryImpl implements PropertiesRegistry {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesRegistryImpl.class);
    private final Map<String, String> properties = new ConcurrentHashMap<>();

    @Override
    public void set(Map<String, String> otherProperties) {
        warnOfPropertyOverride(otherProperties);
        properties.putAll(otherProperties);
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }

    private void warnOfPropertyOverride(Map<String, String> otherProperties) {
        otherProperties.forEach((key, value) -> {
            if (properties.containsKey(key)) {
                logger.warn("Property with key {} and value {} is already present, value will be set to {}",
                        key, properties.get(key), value);
            }
        });
    }
}
