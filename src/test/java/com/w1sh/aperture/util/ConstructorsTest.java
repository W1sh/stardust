package com.w1sh.aperture.util;

import com.w1sh.aperture.annotation.Inject;
import com.w1sh.aperture.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.aperture.example.service.impl.CalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstructorsTest {

    @Test
    void should_returnNoArgConstructor_whenNoConstructorIsAvailable() {
        final var constructor = Constructors.findInjectAnnotatedConstructor(EmptyCalculatorControllerImpl.class);

        assertNotNull(constructor);
        assertEquals(0, constructor.getParameterCount());
    }

    @Test
    void should_returnInjectedAnnotatedConstructor_whenOneIsPresent() {
        final var constructor = Constructors.findInjectAnnotatedConstructor(CalculatorServiceImpl.class);

        assertNotNull(constructor);
        assertTrue(constructor.isAnnotationPresent(Inject.class));
    }
}