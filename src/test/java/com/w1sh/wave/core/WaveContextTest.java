package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.DuplicateCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static com.w1sh.wave.core.builder.ContextBuilder.singleton;
import static org.junit.jupiter.api.Assertions.*;

class WaveContextTest {

    private final WaveContext waveContext = new WaveContext();

    @Test
    void should_returnNoArgConstructor_whenNoConstructorIsAvailable(){
        final Constructor<?> constructor = waveContext.findInjectAnnotatedConstructor(BetterCalculatorServiceImpl.class);

        assertNotNull(constructor);
        assertEquals(0, constructor.getParameterCount());
    }

    @Test
    void should_returnInjectedAnnotatedConstructor_whenOneIsPresent(){
        final Constructor<?> constructor = waveContext.findInjectAnnotatedConstructor(CalculatorServiceImpl.class);

        assertNotNull(constructor);
        assertTrue(constructor.isAnnotationPresent(Inject.class));
    }

    @Test
    void should_returnObjectProvider_whenGivenClassIsValid(){
        final ObjectProvider<BetterCalculatorServiceImpl> provider = waveContext.createObjectProvider(BetterCalculatorServiceImpl.class);

        assertNotNull(provider);
        assertNotNull(provider.singletonInstance());
    }

    @Test
    void test(){
        waveContext.context(() -> {
            singleton("bean", BetterCalculatorServiceImpl.class);
            singleton(DuplicateCalculatorServiceImpl.class);
        });

        assertNotNull(waveContext.instance(BetterCalculatorServiceImpl.class));
        assertNotNull(waveContext.instance(DuplicateCalculatorServiceImpl.class));
        assertNull(waveContext.instance(CalculatorServiceImpl.class));
    }
}