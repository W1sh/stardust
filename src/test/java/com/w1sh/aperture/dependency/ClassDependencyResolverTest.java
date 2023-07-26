package com.w1sh.aperture.dependency;

import com.w1sh.aperture.Environment;
import com.w1sh.aperture.ProviderContainer;
import com.w1sh.aperture.ProviderContainerImpl;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.aperture.example.controller.impl.PrimaryControllerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class ClassDependencyResolverTest {

    private ClassDependencyResolver resolver;
    private Environment environment;
    private ProviderContainer container;

    @BeforeEach
    void setUp() {
        container = spy(new ProviderContainerImpl());
        environment = new Environment(container, Set.of("test"));
        resolver = new ClassDependencyResolver();
    }

    @Test
    void should_returnTrue_whenRequiredClassIsPresentInContainer() {
        when(container.contains(PrimaryControllerImpl.class)).thenReturn(true);
        var condition = resolver.matches(DependantClass.class, environment);

        assertTrue(condition);
    }

    @Test
    void should_returnFalse_whenRequiredClassIsNotPresentInContainer() {
        var condition = resolver.matches(DependantClass.class, environment);

        assertFalse(condition);
    }

    @Provide
    @DependsOnClass({PrimaryControllerImpl.class})
    private static class DependantClass {}
}