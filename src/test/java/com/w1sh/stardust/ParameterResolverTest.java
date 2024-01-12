package com.w1sh.stardust;

import com.w1sh.stardust.binding.*;
import com.w1sh.stardust.example.CustomBinding;
import com.w1sh.stardust.example.controller.impl.BindingDependantControllerImpl;
import com.w1sh.stardust.example.controller.impl.CollectionDependantControllerImpl;
import com.w1sh.stardust.example.controller.impl.RequiredDependantControllerImpl;
import com.w1sh.stardust.example.service.MerchantService;
import com.w1sh.stardust.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.stardust.example.service.impl.MerchantServiceImpl;
import com.w1sh.stardust.exception.ComponentCreationException;
import com.w1sh.stardust.exception.ProviderInitializationException;
import com.w1sh.stardust.util.Constructors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParameterResolverTest {

    private ProviderContainerImpl container;
    private ParameterResolver resolver;

    @BeforeEach
    void setUp() {
        container = spy(new ProviderContainerImpl());
        resolver = new ParameterResolver(container);
        resolver.addBindingResolver(Lazy.class, LazyBinding::of);
        resolver.addBindingResolver(Provider.class, ProviderBinding::of);
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
    void should_throwProviderInitializationException_whenParameterIsRequiredAndNotResolvable() {
        final var injectConstructor = Constructors.getInjectConstructor(RequiredDependantControllerImpl.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        assertThrows(ProviderInitializationException.class, () -> resolver.resolve(parameter));
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
    void should_throwComponentCreationException_whenParameterIsBindingButNotResolvable() throws NoSuchMethodException {
        final var injectConstructor = BindingDependantControllerImpl.class.getConstructor(CustomBinding.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        assertThrows(ComponentCreationException.class, () -> resolver.resolve(parameter));
    }

    @Test
    void should_returnEmptyArray_whenParameterIsArrayAndNotResolvable() throws NoSuchMethodException {
        final var injectConstructor = CollectionDependantControllerImpl.class.getConstructor(MerchantService[].class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        Object resolved = resolver.resolve(parameter);

        assertNotNull(resolved);
        assertTrue(resolved.getClass().isArray());
        assertEquals(0, ((Object[]) resolved).length);
        verify(container, times(1)).instances(MerchantService.class);
    }

    @Test
    void should_returnArray_whenParameterIsArrayAndResolvable() throws NoSuchMethodException {
        final var injectConstructor = CollectionDependantControllerImpl.class.getConstructor(MerchantService[].class);
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
    void should_throwUnsupportedOperationException_whenParameterIsCollectionButNotSupported() throws NoSuchMethodException {
        final var injectConstructor = CollectionDependantControllerImpl.class.getConstructor(Queue.class);
        ResolvableConstructorImpl<?> constructor = new ResolvableConstructorImpl<>(injectConstructor);
        ResolvableParameter<?> parameter = (ResolvableParameter<?>) constructor.getParameters().toArray()[0];

        assertThrows(UnsupportedOperationException.class, () -> resolver.resolve(parameter));
    }
}