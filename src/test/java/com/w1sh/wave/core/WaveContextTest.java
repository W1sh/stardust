package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.DuplicateCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static com.w1sh.wave.core.builder.ContextBuilder.*;
import static com.w1sh.wave.core.condition.Conditions.ifNotPresent;
import static org.junit.jupiter.api.Assertions.*;

class WaveContextTest {

    private WaveContext waveContext;

    @BeforeEach
    void setUp() {
        waveContext = new WaveContext();
    }

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
    void should_registerSingletons_whenGivenClassesAreValid(){
        waveContext.context(() -> {
            singleton("bean", BetterCalculatorServiceImpl.class);
            singleton(DuplicateCalculatorServiceImpl.class);
            singletonIf(CalculatorServiceImpl.class, ifNotPresent(BetterCalculatorServiceImpl.class));
        });

        assertNotNull(waveContext.instance(BetterCalculatorServiceImpl.class));
        assertNotNull(waveContext.instance(DuplicateCalculatorServiceImpl.class));
        assertNull(waveContext.instance(CalculatorServiceImpl.class));
    }

    @Test
    void should_registerSingleton_whenGivenNameAndClassAreValid(){
        waveContext.context(() -> singleton("bean", BetterCalculatorServiceImpl.class));

        final Object namedInstance = waveContext.instance("bean");
        final BetterCalculatorServiceImpl instance = waveContext.instance(BetterCalculatorServiceImpl.class);
        assertNotNull(namedInstance);
        assertNotNull(instance);
        assertEquals(instance, namedInstance);
    }

    @Test
    void should_registerProvider_whenGivenClassesAreValid(){
        waveContext.context(() -> provider(BetterCalculatorServiceImpl.class));

        final BetterCalculatorServiceImpl actualBCalcInstance = waveContext.instance(BetterCalculatorServiceImpl.class);
        final ObjectProvider<BetterCalculatorServiceImpl> actualBCalcProvider = waveContext.getProvider(BetterCalculatorServiceImpl.class, false);
        assertNotNull(actualBCalcInstance);
        assertEquals(SimpleObjectProvider.class, actualBCalcProvider.getClass());
        assertNotEquals(actualBCalcInstance, actualBCalcProvider.newInstance());
        assertEquals(2, actualBCalcProvider.instances().size());
    }

    @Test
    void should_registerProvider_whenGivenClassAndSupplierAreValid(){
        waveContext.context(() -> provider(DuplicateCalculatorServiceImpl.class));

        final DuplicateCalculatorServiceImpl actualDCalcInstance = waveContext.instance(DuplicateCalculatorServiceImpl.class);
        final ObjectProvider<DuplicateCalculatorServiceImpl> actualDCalcProvider = waveContext.getProvider(DuplicateCalculatorServiceImpl.class, false);
        assertNotNull(actualDCalcInstance);
        assertEquals(SimpleObjectProvider.class, actualDCalcProvider.getClass());
        assertNotEquals(actualDCalcInstance, actualDCalcProvider.newInstance());
        assertEquals(2, actualDCalcProvider.instances().size());
    }
}