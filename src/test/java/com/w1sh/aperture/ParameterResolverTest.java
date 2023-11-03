package com.w1sh.aperture;

import com.w1sh.aperture.binding.Binding;
import com.w1sh.aperture.example.controller.impl.*;
import com.w1sh.aperture.example.service.MerchantService;
import com.w1sh.aperture.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.aperture.example.service.impl.MerchantServiceImpl;
import com.w1sh.aperture.exception.ProviderInitializationException;
import com.w1sh.aperture.util.Constructors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

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
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        when(container.provider(MerchantService.class)).thenReturn(new SingletonObjectProvider<>(new MerchantServiceImpl()));
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
            assertInstanceOf(Binding.class, resolved);
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

    @Test
    void should_throwProviderInitializationException_whenParameterIsRequiredAndNotResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(RequiredDependantControllerImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        assertThrows(ProviderInitializationException.class, () -> resolver.resolve(parameter));
    }

    @Test
    void should_returnEmptyArray_whenParameterIsArrayAndNotResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(ArrayDependantControllerImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        Object resolved = resolver.resolve(parameter);

        assertNotNull(resolved);
        assertTrue(resolved.getClass().isArray());
        assertEquals(0, ((Object[]) resolved).length);
        verify(container, times(1)).instances(MerchantService.class);
    }

    @Test
    void should_returnArray_whenParameterIsArrayAndResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(ArrayDependantControllerImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        when(container.instances(MerchantService.class)).thenReturn(List.of(new MerchantServiceImpl(), new MerchantServiceImpl()));
        Object resolved = resolver.resolve(parameter);

        assertNotNull(resolved);
        assertTrue(resolved.getClass().isArray());
        assertEquals(2, ((Object[]) resolved).length);
        verify(container, times(1)).instances(MerchantService.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_returnEmptyCollection_whenParameterIsCollectionAndNotResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(CollectionDependantControllerImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        Object resolved = resolver.resolve(parameter);

        assertNotNull(resolved);
        assertInstanceOf(Collection.class, resolved);
        assertEquals(0, ((Collection<Object>) resolved).size());
        verify(container, times(1)).instances(MerchantService.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_returnCollection_whenParameterIsCollectionAndResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(CollectionDependantControllerImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        when(container.instances(MerchantService.class)).thenReturn(List.of(new MerchantServiceImpl(), new MerchantServiceImpl()));
        Object resolved = resolver.resolve(parameter);

        assertNotNull(resolved);
        assertInstanceOf(Collection.class, resolved);
        assertEquals(2, ((Collection<Object>) resolved).size());
        verify(container, times(1)).instances(MerchantService.class);
    }

    @Test
    void should_throwUnsupportedOperationException_whenParameterIsCollectionButNotSupported() {
        final var injectConstructor = Constructors.getInjectConstructor(UnsupportedCollectionDependantControllerImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        assertThrows(UnsupportedOperationException.class, () -> resolver.resolve(parameter));
    }
}