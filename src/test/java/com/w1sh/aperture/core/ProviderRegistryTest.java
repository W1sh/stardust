package com.w1sh.aperture.core;

import com.w1sh.aperture.core.condition.Condition;
import com.w1sh.aperture.core.condition.MetadataConditionFactory;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.aperture.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.aperture.example.service.CalculatorService;
import com.w1sh.aperture.example.service.impl.DuplicateCalculatorServiceImpl;
import jakarta.ws.rs.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProviderRegistryTest {

    private ProviderRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistry();
    }

    @Test
    void should_returnInstance_whenProviderOfClassIsRegistered() {
        final var provider = new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl());
        registry.register(provider, DuplicateCalculatorServiceImpl.class, "duplicate");

        DuplicateCalculatorServiceImpl instance = registry.instance(DuplicateCalculatorServiceImpl.class);

        assertNotNull(instance);
        assertEquals(provider.singletonInstance(), instance);
    }

    @Test
    void should_returnInstance_whenProviderWithNameIsRegistered() {
        final var provider = new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl());
        registry.register(provider, DuplicateCalculatorServiceImpl.class, "duplicate");

        DuplicateCalculatorServiceImpl instance = registry.instance("duplicate");

        assertNotNull(instance);
        assertEquals(provider.singletonInstance(), instance);
    }

    @Test
    void should_returnNull_whenProviderWithNameIsNotRegistered() {
        final var provider = new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl());
        registry.register(provider, DuplicateCalculatorServiceImpl.class, "duplicate");

        Object instance = registry.instance("calculator");

        assertNull(instance);
    }

    @Test
    void should_returnNull_whenProviderOfClassIsNotRegistered() {
        final var provider = new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl());
        registry.register(provider, DuplicateCalculatorServiceImpl.class, "duplicate");

        Object instance = registry.instance(CalculatorController.class);

        assertNull(instance);
    }

    @Test
    void should_returnInstance_whenProviderOfSubclassIsRegistered() {
        final var provider = new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl());
        registry.register(provider, DuplicateCalculatorServiceImpl.class, "duplicate");

        CalculatorService instance = registry.instance(CalculatorService.class);

        assertNotNull(instance);
        assertEquals(provider.singletonInstance(), instance);
    }

    @Test
    void should_returnAllClassesRegisteredAnnotated_whenGivenAnnotation() {
        final var provider1 = new SingletonObjectProvider<>(new CalculatorControllerImpl());
        final var provider2 = new SingletonObjectProvider<>(new EmptyCalculatorControllerImpl());
        registry.register(provider1, CalculatorControllerImpl.class, "CalculatorControllerImpl");
        registry.register(provider2, EmptyCalculatorControllerImpl.class, "EmptyCalculatorControllerImpl");

        List<Class<?>> classes = registry.getAllAnnotatedWith(Path.class);

        assertNotNull(classes);
        assertEquals(1, classes.size());
        assertEquals(CalculatorControllerImpl.class, classes.get(0));
    }
}