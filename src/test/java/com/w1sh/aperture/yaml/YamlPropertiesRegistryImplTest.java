package com.w1sh.aperture.yaml;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class YamlPropertiesRegistryImplTest {

    private final YamlPropertiesRegistry yamlPropertiesRegistry = new YamlPropertiesRegistryImpl();

    @Test
    void should_returnKeyValue_whenKeyIsPresentInYamlFile() {
        InputStream file = this.getClass().getResourceAsStream("/application.yaml");
        yamlPropertiesRegistry.register(file);
        String nameProperty = yamlPropertiesRegistry.getProperty("application.name");

        assertNotNull(nameProperty);
        assertEquals("test", nameProperty);
    }

    @Test
    void should_returnNull_whenKeyIsNotPresentInYamlFile() {
        InputStream file = this.getClass().getResourceAsStream("/application.yaml");
        yamlPropertiesRegistry.register(file);
        String nameProperty = yamlPropertiesRegistry.getProperty("application.version");

        assertNull(nameProperty);
    }
}