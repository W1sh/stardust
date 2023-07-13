package com.w1sh.aperture.core;

import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.example.service.impl.DuplicateCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProviderFactoryTest {

    private ProviderFactory factory;

    @BeforeEach
    void setUp() {
        ProviderRegistry registry = new ProviderRegistry();
        ProviderPostConstructorProcessor postConstructorProcessor = new ProviderPostConstructorProcessor();
        factory = new ProviderFactory(registry, postConstructorProcessor);
    }


    @Test
    void should_returnProvider_whenGivenValidConstructorInitializationContext() {
        final var context = new ConstructorInitializationContext<>(DuplicateCalculatorServiceImpl.class, Options.empty());

        ObjectProvider<?> provider = factory.create(context);

        assertNotNull(provider);
        assertNotNull(provider.singletonInstance());
    }

    @Test
    void should_returnProvider_whenGivenValidInstanceInitializationContext() {
        final var context = new InstanceInitializationContext<>(new DuplicateCalculatorServiceImpl(), Options.empty());

        ObjectProvider<?> provider = factory.create(context);

        assertNotNull(provider);
        assertNotNull(provider.singletonInstance());
    }

    @Test
    void should_returnProvider_whenGivenValidSupplierInitializationContext() {
        final var context = new SupplierInitializationContext<>(DuplicateCalculatorServiceImpl.class, Options.empty(),
                DuplicateCalculatorServiceImpl::new);

        ObjectProvider<?> provider = factory.create(context);

        assertNotNull(provider);
        assertNotNull(provider.singletonInstance());
    }
}