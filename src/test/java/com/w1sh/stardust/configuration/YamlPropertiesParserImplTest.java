package com.w1sh.stardust.configuration;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YamlPropertiesParserImplTest {

    private final PropertiesParser parser = new YamlPropertiesParserImpl();

    @Test
    void should_returnKeyValue_whenKeyIsPresentInYamlFile() {
        InputStream file = this.getClass().getResourceAsStream("/application.yaml");
        Map<String, String> parsed = parser.parse(file);

        assertNotNull(parsed);
        assertEquals("test", parsed.get("application.name"));
    }

    @Test
    void should_returnNull_whenKeyIsNotPresentInYamlFile() {
        InputStream file = this.getClass().getResourceAsStream("/application.yaml");
        Map<String, String> parsed = parser.parse(file);

        assertNotNull(parsed);
        assertNull(parsed.get("application.version"));
    }
}