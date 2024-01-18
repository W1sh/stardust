package com.w1sh.stardust.dependency;

import com.w1sh.stardust.Environment;
import com.w1sh.stardust.ProviderContainer;
import com.w1sh.stardust.AbstractProviderContainer;
import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.configuration.PropertiesRegistry;
import com.w1sh.stardust.configuration.PropertiesRegistryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class PropertyDependencyResolverTest {

    private final PropertyDependencyResolver resolver = new PropertyDependencyResolver();
    private final PropertiesRegistry propertiesRegistry = spy(PropertiesRegistryImpl.class);
    private ProviderContainer container;
    private Environment environment;

    @BeforeEach
    void setUp() {
        container = spy(AbstractProviderContainer.base());
        environment = new Environment(container, Set.of("test"));
    }

    @Test
    void should_returnTrue_whenSystemPropertyIsPresentAndMatchesSystem() {
        when(container.instance(PropertiesRegistry.class)).thenReturn(propertiesRegistry);
        when(propertiesRegistry.getProperty("test")).thenReturn("true");
        var condition = resolver.matches(PropertyDependencyResolverTest.DependantClass.class, environment);

        assertTrue(condition);
    }

    @Test
    void should_returnFalse_whenSystemPropertyIsPresentAndDoesNotMatchSystem() {
        when(container.instance(PropertiesRegistry.class)).thenReturn(propertiesRegistry);
        when(propertiesRegistry.getProperty("test")).thenReturn("false");
        var condition = resolver.matches(PropertyDependencyResolverTest.DependantClass.class, environment);

        assertFalse(condition);
    }

    @Provide
    @DependsOnProperty(value = "test", expectedValue = "true")
    private static class DependantClass {}

}