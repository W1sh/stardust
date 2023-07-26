package com.w1sh.aperture;

import com.w1sh.aperture.binding.Binding;
import com.w1sh.aperture.example.controller.impl.BindingDependantControllerImpl;
import com.w1sh.aperture.example.service.MerchantService;
import com.w1sh.aperture.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.aperture.example.service.impl.MerchantServiceImpl;
import com.w1sh.aperture.util.Constructors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParameterResolverTest {

    private ProviderContainerImpl container;
    private ParameterResolver resolver;

    @BeforeEach
    void setUp() {
        container = spy(new ProviderContainerImpl());
        resolver = new ParameterResolver(container);
    }

    @Test
    void should_returnObject_whenParameterIsClassAndResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(BetterCalculatorServiceImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);

        when(container.provider(MerchantService.class)).thenReturn(new SingletonObjectProvider<>(new MerchantServiceImpl()));
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];
        Object resolved = resolver.resolve(parameter);

        assertNotNull(resolved);
        verify(container, times(1)).provider(MerchantService.class);
    }

    @Test
    void should_returnObject_whenParameterIsBindingAndResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(BindingDependantControllerImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);

        when(container.provider(MerchantService.class)).thenReturn(new SingletonObjectProvider<>(new MerchantServiceImpl()));
        constructor.getParameters().forEach(resolvableParameter -> {
            Object resolved = resolver.resolve(resolvableParameter);
            assertNotNull(resolved);
            assertTrue(resolved instanceof Binding<?>);
        });

        verify(container, times(2)).provider(MerchantService.class);
    }

    @Test
    void should_returnNull_whenParameterIsBindingAndNotResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(BindingDependantControllerImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);

        constructor.getParameters().forEach(resolvableParameter -> {
            Object resolved = resolver.resolve(resolvableParameter);
            assertNull(resolved);
        });

        verify(container, times(2)).provider(MerchantService.class);
    }

    @Test
    void should_returnNull_whenParameterIsClassAndNotResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(BetterCalculatorServiceImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);

        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];
        Object resolved = resolver.resolve(parameter);

        assertNull(resolved);
        verify(container, times(1)).provider(MerchantService.class);
    }
}