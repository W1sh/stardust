package com.w1sh.aperture.configuration;

import com.w1sh.aperture.annotation.Property;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PropertyValuePostConstructInterceptorTest {

    private final PropertiesRegistry registry = Mockito.spy(new YamlPropertiesRegistryImpl());
    private final PropertyValuePostConstructInterceptor interceptor = new PropertyValuePostConstructInterceptor(registry);

    @Test
    void should_returnKeyValue_whenKeyIsPresentInYamlFile() {
        InputStream file = this.getClass().getResourceAsStream("/application.yaml");
        registry.register(file);
        PropertyClass propertyClass = new PropertyClass();

        assertNull(propertyClass.name);
        interceptor.intercept(propertyClass);

        assertEquals("test", propertyClass.name);
        verify(registry, times(1)).getProperty("application.name");
    }

    public static class PropertyClass {

        @Property("application.name")
        private String name;
    }
}