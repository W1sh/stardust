package com.w1sh.stardust.configuration;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesParserImplTest {

    private final PropertiesParser parser = new PropertiesParserImpl();

    @Test
    void should_returnKeyValue_whenKeyIsPresentInPropertiesFile() {
        InputStream file = this.getClass().getResourceAsStream("/application.properties");
        Map<String, String> parsed = parser.parse(file);

        assertNotNull(parsed);
        assertEquals("test", parsed.get("application.name"));
    }

    @Test
    void should_returnNull_whenKeyIsNotPresentInPropertiesFile() {
        InputStream file = this.getClass().getResourceAsStream("/application.properties");
        Map<String, String> parsed = parser.parse(file);

        assertNotNull(parsed);
        assertNull(parsed.get("application.version"));
    }
}