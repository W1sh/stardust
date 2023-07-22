package com.w1sh.aperture;

import com.w1sh.aperture.annotation.Primary;
import com.w1sh.aperture.condition.*;
import com.w1sh.aperture.exception.ProviderCandidatesException;
import com.w1sh.aperture.exception.ProviderRegistrationException;
import com.w1sh.aperture.example.controller.CalculatorController;
import com.w1sh.aperture.example.controller.impl.CalculatorControllerImpl;
import com.w1sh.aperture.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.aperture.example.service.CalculatorService;
import com.w1sh.aperture.example.service.impl.DuplicateCalculatorServiceImpl;
import com.w1sh.aperture.util.Tests;
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

        List<Class<?>> classes = registry.getAllAnnotatedWith(Primary.class);

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

        assertEquals(ProviderRegistry.OverrideStrategy.NOT_ALLOWED, registry.getOverrideStrategy());
        assertThrows(ProviderRegistrationException.class, () ->
                registry.register(provider2, DuplicateCalculatorServiceImpl.class, "CalculatorControllerImpl"));
    }

    @Test
    void should_returnPrimaryInstanceOrProvider_whenMultipleProvidersAreRegisteredAndOneIsPrimary() {
        final var definition1 = Tests.definition(CalculatorControllerImpl.class, Metadata.builder()
                .primary(true)
                .build());
        final var definition2 = Tests.definition(DuplicateCalculatorServiceImpl.class);
        final var provider1 = new SingletonObjectProvider<>(new CalculatorControllerImpl());
        final var provider2 = new SingletonObjectProvider<>(new DuplicateCalculatorServiceImpl());
        registry.register(provider1, definition1);
        registry.register(provider2, definition2);

        CalculatorController instance = registry.primaryInstance(CalculatorController.class);
        ObjectProvider<CalculatorController> provider = registry.primaryProvider(CalculatorController.class);

        assertNotNull(provider);
        assertEquals(CalculatorControllerImpl.class, provider.singletonInstance().getClass());
        assertNotNull(instance);
        assertEquals(CalculatorControllerImpl.class, instance.getClass());
    }

    @Test
    void should_throwProviderCandidatesException_whenMultiplePrimaryProvidersAreRegistered() {
        final var definition1 = Tests.definition(CalculatorControllerImpl.class, Metadata.builder()
                .primary(true)
                .build());
        final var definition2 = Tests.definition(EmptyCalculatorControllerImpl.class, Metadata.builder()
                .primary(true)
                .build());
        final var provider1 = new SingletonObjectProvider<>(new CalculatorControllerImpl());
        final var provider2 = new SingletonObjectProvider<>(new EmptyCalculatorControllerImpl());
        registry.register(provider1, definition1);
        registry.register(provider2, definition2);

        assertThrows(ProviderCandidatesException.class, () -> registry.primaryInstance(CalculatorController.class));
        assertThrows(ProviderCandidatesException.class, () -> registry.primaryProvider(CalculatorController.class));
    }

    @Test
    void should_throwProviderCandidatesException_whenNoPrimaryProvidersAreRegistered() {
        final var definition1 = Tests.definition(CalculatorControllerImpl.class);
        final var definition2 = Tests.definition(EmptyCalculatorControllerImpl.class);
        final var provider1 = new SingletonObjectProvider<>(new CalculatorControllerImpl());
        final var provider2 = new SingletonObjectProvider<>(new EmptyCalculatorControllerImpl());
        registry.register(provider1, definition1);
        registry.register(provider2, definition2);

        assertThrows(ProviderCandidatesException.class, () -> registry.primaryInstance(CalculatorController.class));
        assertThrows(ProviderCandidatesException.class, () -> registry.primaryProvider(CalculatorController.class));
    }

    @Test
    void should_returnAllInstances_whenMultipleProvidersAreRegisteredForClass() {
        final var provider1 = new SingletonObjectProvider<>(new CalculatorControllerImpl());
        final var provider2 = new SingletonObjectProvider<>(new EmptyCalculatorControllerImpl());
        registry.register(provider1, CalculatorControllerImpl.class, "CalculatorControllerImpl");
        registry.register(provider2, EmptyCalculatorControllerImpl.class, "EmptyCalculatorControllerImpl");

        List<CalculatorController> instances = registry.instances(CalculatorController.class);
        List<ObjectProvider<CalculatorController>> providers = registry.providers(CalculatorController.class);

        assertNotNull(instances);
        assertNotNull(providers);
        assertEquals(2, instances.size());
        assertEquals(2, providers.size());
    }

    @Test
    void should_returnTrue_whenThereIsAtLeastOneProviderRegisteredForClass() {
        final var provider1 = new SingletonObjectProvider<>(new CalculatorControllerImpl());
        final var provider2 = new SingletonObjectProvider<>(new EmptyCalculatorControllerImpl());
        registry.register(provider1, CalculatorControllerImpl.class, "CalculatorControllerImpl");
        registry.register(provider2, EmptyCalculatorControllerImpl.class, "EmptyCalculatorControllerImpl");

        boolean containsClass = registry.contains(CalculatorController.class);
        boolean containsName = registry.contains("CalculatorControllerImpl");

        assertTrue(containsClass);
        assertTrue(containsName);
    }

    @Test
    void should_returnAllInstances_whenMultipleProvidersAreRegisteredForTypeReference() {
        final var provider1 = new SingletonObjectProvider<>(new ActiveProfileConditionFactory());
        final var provider2 = new SingletonObjectProvider<>(new SystemPropertyConditionFactory());
        registry.register(provider1, ActiveProfileConditionFactory.class, "ActiveProfileConditionFactory");
        registry.register(provider2, SystemPropertyConditionFactory.class, "SystemPropertyConditionFactory");

        List<MetadataConditionFactory<ActiveProfileCondition>> instances1 = registry.instances(new TypeReference<>() {});
        List<MetadataConditionFactory<Condition>> instances2 = registry.instances(new TypeReference<>() {});

        assertNotNull(instances1);
        assertEquals(1, instances1.size());
        assertNotNull(instances2);
        assertEquals(2, instances2.size());
    }
}