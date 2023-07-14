package com.w1sh.aperture.core;

import com.w1sh.aperture.core.exception.ProviderRegistrationException;
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

class DefaultProviderRegistryTest {

    private DefaultProviderRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DefaultProviderRegistry();
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

    @Test
    void should_throwRegistrationException_whenTryingToRegisterClassTwiceButOverrideNotAllowed() {
        final var provider1 = new SingletonObjectProvider<>(new CalculatorControllerImpl());
        final var provider2 = new SingletonObjectProvider<>(new CalculatorControllerImpl());
        registry.setOverrideStrategy(ProviderRegistry.OverrideStrategy.NOT_ALLOWED);
        registry.register(provider1, CalculatorControllerImpl.class, "CalculatorControllerImpl");

        assertThrows(ProviderRegistrationException.class, () ->
                registry.register(provider2, CalculatorControllerImpl.class, "CalculatorControllerImpl"));
    }

    @Test
    void should_throwRegistrationException_whenTryingToRegisterWithSameNameTwiceButOverrideNotAllowed() {
        final var provider1 = new SingletonObjectProvider<>(new CalculatorControllerImpl());
        final var provider2 = new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl());
        registry.setOverrideStrategy(ProviderRegistry.OverrideStrategy.NOT_ALLOWED);
        registry.register(provider1, CalculatorControllerImpl.class, "CalculatorControllerImpl");

        assertThrows(ProviderRegistrationException.class, () ->
                registry.register(provider2, DuplicateCalculatorServiceImpl.class, "CalculatorControllerImpl"));
    }
}