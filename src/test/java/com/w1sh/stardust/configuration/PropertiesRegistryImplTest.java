package com.w1sh.stardust.configuration;

import com.w1sh.stardust.exception.PropertyValueSettingException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesRegistryImplTest {

    private final PropertiesRegistry registry = new PropertiesRegistryImpl();

    @Test
    void should_overrideProperty_whenKeyIsAlreadyPresentInRegistry() {
        Map<String, String> properties = new ConcurrentHashMap<>();
        properties.put("stardust.properties.allow-override", "true");
        properties.put("application.name", "test");

        Map<String, String> otherProperties = new ConcurrentHashMap<>();
        otherProperties.put("application.name", "test2");

        registry.set(properties);
        assertEquals("test", registry.getProperty("application.name"));
        registry.set(otherProperties);
        assertEquals("test2", registry.getProperty("application.name"));
    }

    @Test
    void should_returnDefaultValue_whenKeyIsNotPresentInRegistry() {
        Map<String, String> properties = new ConcurrentHashMap<>();
        properties.put("application.name", "test");

        registry.set(properties);
        assertEquals("1", registry.getProperty("application.version", "1"));
    }

    @Test
    void should_throwPropertyValueSettingException_whenKeyIsPresentInRegistryAndOverrideIsAttempted() {
        Map<String, String> properties = new ConcurrentHashMap<>();
        properties.put("application.name", "test");

        Map<String, String> otherProperties = new ConcurrentHashMap<>();
        otherProperties.put("application.name", "test2");

        registry.set(properties);
        assertEquals("test", registry.getProperty("application.name"));
        assertThrows(PropertyValueSettingException.class, () -> registry.set(otherProperties));
    }
}