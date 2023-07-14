package com.w1sh.aperture.core;

import com.w1sh.aperture.core.annotation.Inject;
import com.w1sh.aperture.core.annotation.Required;
import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.core.exception.ProviderInitializationException;
import com.w1sh.aperture.example.service.CalculatorService;
import com.w1sh.aperture.example.service.MerchantService;
import com.w1sh.aperture.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.aperture.example.service.impl.DuplicateCalculatorServiceImpl;
import com.w1sh.aperture.example.service.impl.MerchantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProviderFactoryTest {

    private ProviderFactory factory;
    private DefaultProviderRegistry registry;

    @BeforeEach
    void setUp() {
        registry = spy(new DefaultProviderRegistry());
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

    @Test
    void should_returnProviderWithInstanceWithNullVariable_whenConstructorInitializationContextOfClass() {
        final var context = new ConstructorInitializationContext<>(BetterCalculatorServiceImpl.class, Options.empty());

        ObjectProvider<?> provider = factory.create(context);

        assertNotNull(provider);
        assertNotNull(provider.singletonInstance());
        assertNull(((BetterCalculatorServiceImpl) provider.singletonInstance()).getMerchantService());
    }

    @Test
    void should_returnProviderWithInstanceWithNonNullVariable_whenConstructorInitializationContextOfClass() {
        final var merchantContext = new ConstructorInitializationContext<>(MerchantServiceImpl.class, Options.empty());
        final var calculatorContext = new ConstructorInitializationContext<>(BetterCalculatorServiceImpl.class, Options.empty());

        ObjectProvider<?> merchantProvider = factory.create(merchantContext);
        registry.register(merchantProvider, MerchantServiceImpl.class, "");
        ObjectProvider<?> calculatorProvider = factory.create(calculatorContext);

        assertNotNull(calculatorProvider);
        assertNotNull(calculatorProvider.singletonInstance());
        assertNotNull(((BetterCalculatorServiceImpl) calculatorProvider.singletonInstance()).getMerchantService());
        verify(registry, times(1)).provider(MerchantService.class);
    }

    @Test
    void should_throwInitializationException_whenConstructorInitializationContextOfClassWithRequiredParameter() {
        final var context = new ConstructorInitializationContext<>(RequiredParameterCalculatorService.class, Options.empty());

        assertThrows(ProviderInitializationException.class, () -> factory.create(context));
    }

    private static class RequiredParameterCalculatorService implements CalculatorService {

        @Inject
        private RequiredParameterCalculatorService(@Required MerchantService merchantService) {}
    }
}