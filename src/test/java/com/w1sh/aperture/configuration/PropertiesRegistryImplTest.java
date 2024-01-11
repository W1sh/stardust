package com.w1sh.aperture.configuration;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesRegistryImplTest {

    private final PropertiesRegistry registry = new PropertiesRegistryImpl();

    @Test
    void should_returnKeyValue_whenKeyIsPresentInYamlFile() {
        InputStream file = this.getClass().getResourceAsStream("/application.properties");
        registry.register(file);
        String nameProperty = registry.getProperty("application.name");

        assertNotNull(nameProperty);
        assertEquals("test", nameProperty);
    }

    @Test
    void should_returnNull_whenKeyIsNotPresentInYamlFile() {
        InputStream file = this.getClass().getResourceAsStream("/application.properties");
        registry.register(file);
        String nameProperty = registry.getProperty("application.version");

        assertNull(nameProperty);
    }
}