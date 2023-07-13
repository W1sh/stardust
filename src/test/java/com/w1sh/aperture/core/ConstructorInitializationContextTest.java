package com.w1sh.aperture.core;

import com.w1sh.aperture.core.annotation.Inject;
import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.aperture.example.service.impl.CalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstructorInitializationContextTest {

    @Test
    void should_returnNoArgConstructor_whenNoConstructorIsAvailable() {
        final var context = new ConstructorInitializationContext<>(BetterCalculatorServiceImpl.class, Options.empty());

        assertNotNull(context.getConstructor());
        assertEquals(0, context.getConstructor().getParameterCount());
    }

    @Test
    void should_returnInjectedAnnotatedConstructor_whenOneIsPresent() {
        final var context = new ConstructorInitializationContext<>(CalculatorServiceImpl.class, Options.empty());

        assertNotNull(context.getConstructor());
        assertTrue(context.getConstructor().isAnnotationPresent(Inject.class));
    }
}