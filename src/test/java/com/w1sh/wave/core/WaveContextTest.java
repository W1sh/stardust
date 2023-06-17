package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.core.builder.Options;
import com.w1sh.wave.core.exception.CircularDependencyException;
import com.w1sh.wave.core.exception.UnsatisfiedComponentException;
import com.w1sh.wave.example.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import static com.w1sh.wave.core.builder.ContextBuilder.provider;
import static com.w1sh.wave.core.builder.ContextBuilder.singleton;
import static com.w1sh.wave.web.RouteBuilder.get;
import static org.junit.jupiter.api.Assertions.*;

class WaveContextTest {

    private WaveContext waveContext;

    @BeforeEach
    void setUp() {
        waveContext = new WaveContext();
    }

    @Test
    void should_returnNull_whenNoComponentIsDefined() {
        waveContext.context(() -> singleton(BetterCalculatorServiceImpl.class));

        final Object instance = waveContext.instanceOrNull(DuplicateCalculatorServiceImpl.class);
        final Object namedInstance = waveContext.instanceOrNull("MissingComponent");

        assertNull(instance);
        assertNull(namedInstance);
    }

    @Test
    void should_throwException_whenNoComponentIsDefined() {
        waveContext.context(() -> singleton(BetterCalculatorServiceImpl.class));

        assertThrows(UnsatisfiedComponentException.class, () -> waveContext.instance(DuplicateCalculatorServiceImpl.class));
        assertThrows(UnsatisfiedComponentException.class, () -> waveContext.instance("MissingComponent"));
    }

    @Test
    void should_haveSimpleNamingStrategy_whenNamingStrategyIsDefined() {
        final SimpleNamingStrategy namingStrategy = new SimpleNamingStrategy();
        waveContext.namingStrategy(namingStrategy);

        assertEquals(waveContext.getNamingStrategy(), namingStrategy);
    }

    @Test
    void should_returnNoArgConstructor_whenNoConstructorIsAvailable() {
        final Constructor<?> constructor = waveContext.findInjectAnnotatedConstructor(BetterCalculatorServiceImpl.class);

        assertNotNull(constructor);
        assertEquals(0, constructor.getParameterCount());
    }

    @Test
    void should_returnInjectedAnnotatedConstructor_whenOneIsPresent() {
        final Constructor<?> constructor = waveContext.findInjectAnnotatedConstructor(CalculatorServiceImpl.class);

        assertNotNull(constructor);
        assertTrue(constructor.isAnnotationPresent(Inject.class));
    }

    @Test
    void should_returnObjectProvider_whenGivenClassIsValid() {
        final ObjectProvider<BetterCalculatorServiceImpl> provider = waveContext.createObjectProvider(BetterCalculatorServiceImpl.class, new HashSet<>());

        assertNotNull(provider);
        assertNotNull(provider.singletonInstance());
    }

    @Test
    void should_registerSingleton_whenGivenClassIsValid() {
        waveContext.namingStrategy(new SimpleNamingStrategy())
                .context(() -> singleton(BetterCalculatorServiceImpl.class));

        final Object namedInstance = waveContext.instance("betterCalculatorServiceImpl");
        final BetterCalculatorServiceImpl instance = waveContext.instance(BetterCalculatorServiceImpl.class);
        assertNotNull(namedInstance);
        assertNotNull(instance);
        assertEquals(instance, namedInstance);
    }

    @Test
    void should_registerSingleton_whenGivenNameAndClassAreValid() {
        waveContext.context(() -> singleton(BetterCalculatorServiceImpl.class, Options.builder()
                .withName("bean")
                .build()));

        final Object namedInstance = waveContext.instance("bean");
        final BetterCalculatorServiceImpl instance = waveContext.instance(BetterCalculatorServiceImpl.class);
        assertNotNull(namedInstance);
        assertNotNull(instance);
        assertEquals(instance, namedInstance);
    }

    @Test
    void should_registerProvider_whenGivenClassesAreValid() {
        waveContext.context(() -> provider(BetterCalculatorServiceImpl.class));

        final BetterCalculatorServiceImpl actualBCalcInstance = waveContext.instance(BetterCalculatorServiceImpl.class);
        final ObjectProvider<BetterCalculatorServiceImpl> actualBCalcProvider = waveContext.getProvider(BetterCalculatorServiceImpl.class, false);
        assertNotNull(actualBCalcInstance);
        assertEquals(SimpleObjectProvider.class, actualBCalcProvider.getClass());
        assertNotEquals(actualBCalcInstance, actualBCalcProvider.newInstance());
        assertEquals(2, actualBCalcProvider.instances().size());
    }

    @Test
    void should_registerProvider_whenGivenClassAndSupplierAreValid() {
        waveContext.context(() -> provider(DuplicateCalculatorServiceImpl.class));

        final DuplicateCalculatorServiceImpl actualDCalcInstance = waveContext.instance(DuplicateCalculatorServiceImpl.class);
        final ObjectProvider<DuplicateCalculatorServiceImpl> actualDCalcProvider = waveContext.getProvider(DuplicateCalculatorServiceImpl.class, false);
        assertNotNull(actualDCalcInstance);
        assertEquals(SimpleObjectProvider.class, actualDCalcProvider.getClass());
        assertNotEquals(actualDCalcInstance, actualDCalcProvider.newInstance());
        assertEquals(2, actualDCalcProvider.instances().size());
    }

    @Test
    void should_throwCircularDependencyInjection_whenAttemptingMultipleInitializationsOfSameClass() {
        final Set<Class<?>> initializationChain = new HashSet<>();
        initializationChain.add(CircularDependantClass.class);
        final ObjectProvider<CircularDependantClass> objectProvider = waveContext.createObjectProvider(CircularDependantClass.class, initializationChain);

        assertThrows(CircularDependencyException.class, objectProvider::newInstance);
    }

    @Test
    void should_notRegisterSingleton_whenNotInsertedInActiveProfiles() {
        waveContext.activeProfiles("Test")
                .context(() -> singleton(DuplicateCalculatorServiceImpl.class));

        final DuplicateCalculatorServiceImpl dCalcInstance = waveContext.instanceOrNull(DuplicateCalculatorServiceImpl.class);
        assertNull(dCalcInstance);
    }

    @Test
    void should_registerSingleton_whenInsertedInActiveProfiles() {
        waveContext.activeProfiles("Test")
                .context(() -> singleton(DuplicateCalculatorServiceImpl.class, Options.builder()
                        .profiles("Test")
                        .build()));

        final DuplicateCalculatorServiceImpl dCalcInstance = waveContext.instance(DuplicateCalculatorServiceImpl.class);
        assertNotNull(dCalcInstance);
    }

    @Test
    void should_registerSingleton_whenClassRequiresAnotherRegisteredComponent() {
        waveContext.context(() -> {
            singleton(DuplicateCalculatorServiceImpl.class);
            singleton(TestClass.class, Options.builder()
                    .conditionalOn(DuplicateCalculatorServiceImpl.class)
                    .build());
        });

        final DuplicateCalculatorServiceImpl dCalcInstance = waveContext.instance(DuplicateCalculatorServiceImpl.class);
        final TestClass testInstance = waveContext.instance(TestClass.class);
        assertNotNull(dCalcInstance);
        assertNotNull(testInstance);
    }

    @Test
    void should_notRegisterSingleton_whenClassRequiresMissingComponent() {
        waveContext.context(() -> singleton(TestClass.class, Options.builder()
                .conditionalOn(DuplicateCalculatorServiceImpl.class)
                .build()));

        final DuplicateCalculatorServiceImpl dCalcInstance = waveContext.instanceOrNull(DuplicateCalculatorServiceImpl.class);
        final TestClass testInstance = waveContext.instanceOrNull(TestClass.class);
        assertNull(dCalcInstance);
        assertNull(testInstance);
    }

    @Test
    void should_registerSingleton_whenClassRequiresComponentToBeMissing() {
        waveContext.context(() -> singleton(TestClass.class, Options.builder()
                .conditionalOnMissing(DuplicateCalculatorServiceImpl.class)
                .build()));

        final DuplicateCalculatorServiceImpl dCalcInstance = waveContext.instanceOrNull(DuplicateCalculatorServiceImpl.class);
        final TestClass testInstance = waveContext.instance(TestClass.class);
        assertNull(dCalcInstance);
        assertNotNull(testInstance);
    }

    @Test
    void should_notRegisterSingleton_whenClassRequiresComponentToBeMissing() {
        waveContext.context(() -> {
            singleton(DuplicateCalculatorServiceImpl.class);
            singleton(TestClass.class, Options.builder()
                    .conditionalOnMissing(DuplicateCalculatorServiceImpl.class)
                    .build());
        });

        final DuplicateCalculatorServiceImpl dCalcInstance = waveContext.instanceOrNull(DuplicateCalculatorServiceImpl.class);
        final TestClass testInstance = waveContext.instanceOrNull(TestClass.class);
        assertNotNull(dCalcInstance);
        assertNull(testInstance);
    }
}