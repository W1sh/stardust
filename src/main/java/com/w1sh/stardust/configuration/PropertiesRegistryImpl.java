package com.w1sh.stardust.configuration;

import com.w1sh.stardust.exception.PropertyValueSettingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesRegistryImpl implements PropertiesRegistry {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesRegistryImpl.class);
    private final Map<String, String> properties = new ConcurrentHashMap<>();

    @Override
    public void set(Map<String, String> otherProperties) {
        if (isOverridingAllowed()) {
            if (!isOverridingIgnored()) warnOfPropertyOverride(otherProperties);
            properties.putAll(otherProperties);
        } else {
            otherProperties.forEach((key, value) -> {
                if (properties.containsKey(key)) {
                    throw PropertyValueSettingException.alreadySet(key);
                } else properties.put(key, value);
            });
        }
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }

    public boolean isOverridingAllowed() {
        boolean overridingAllowed = Boolean.parseBoolean(getProperty("stardust.properties.allow-override", "false"));
        return Boolean.TRUE.equals(overridingAllowed);
    }

    public boolean isOverridingIgnored() {
        boolean overridingAllowed = Boolean.parseBoolean(getProperty("stardust.properties.ignore-override", "false"));
        return Boolean.TRUE.equals(overridingAllowed);
    }

    private void warnOfPropertyOverride(Map<String, String> otherProperties) {
        otherProperties.forEach((key, value) -> {
            if (properties.containsKey(key)) {
                logger.warn("Property with key {} and value {} is already present, value will be set to {}",
                        key, properties.get(key), value);
                logger.warn("If you would like the framework to fail on these kind of scenarios, set the property \"stardust.properties.allow-override\" to false");
                logger.warn("If you would like to ignore warnings on these kind of scenarios, set the property \"stardust.properties.ignore-override\" to true");
            }
        });
    }
}
