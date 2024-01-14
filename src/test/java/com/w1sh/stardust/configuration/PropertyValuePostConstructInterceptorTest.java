package com.w1sh.stardust.configuration;

import com.w1sh.stardust.annotation.Property;
import com.w1sh.stardust.annotation.Required;
import com.w1sh.stardust.exception.PropertyValueSettingException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyValuePostConstructInterceptorTest {

    private final PropertiesRegistry registry = Mockito.spy(new PropertiesRegistryImpl());
    private final PropertyValuePostConstructInterceptor interceptor = new PropertyValuePostConstructInterceptor(registry);

    @Test
    void should_returnKeyValue_whenKeyIsPresentInRegistry() {
        PropertyClass propertyClass = new PropertyClass();
        when(registry.getProperty("application.name")).thenReturn("test");
        when(registry.getProperty("application.version")).thenReturn("1");

        assertNull(propertyClass.name);
        interceptor.intercept(propertyClass);

        assertEquals("test", propertyClass.name);
        verify(registry, times(1)).getProperty("application.name");
    }

    @Test
    void should_throwPropertyValueSettingException_whenKeyIsNotPresentInRegistryButIsMarkedAsRequired() {
        PropertyClass propertyClass = new PropertyClass();

        assertNull(propertyClass.version);
        assertThrows(PropertyValueSettingException.class, () -> interceptor.intercept(propertyClass));
    }

    public static class PropertyClass {

        @Property("application.name")
        private String name;

        @Required
        @Property("application.version")
        private String version;
    }
}