package com.w1sh.aperture.dependency;

import com.w1sh.aperture.Environment;
import com.w1sh.aperture.ProviderContainerImpl;
import com.w1sh.aperture.annotation.Profile;
import com.w1sh.aperture.annotation.Provide;
import com.w1sh.aperture.example.controller.impl.EmptyCalculatorControllerImpl;
import com.w1sh.aperture.example.controller.impl.PrimaryControllerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActiveProfileDependencyResolverTest {

    private ActiveProfileDependencyResolver resolver;
    private Environment environment;

    @BeforeEach
    void setUp() {
        environment = new Environment(new ProviderContainerImpl(), Set.of("test"));
        resolver = new ActiveProfileDependencyResolver();
    }

    @Test
    void should_returnTrue_whenProfileIsPresentAndMatchesEnvironment() {
        var condition = resolver.matches(DependantClass.class, environment);

        assertTrue(condition);
    }

    @Test
    void should_returnTrue_whenProfileIsPresentAndDoesNotMatchEnvironment() {
        var condition = resolver.matches(AnotherDependantClass.class, environment);

        assertFalse(condition);
    }

    @Provide
    @Profile("test")
    private static class DependantClass {}

    @Provide
    @Profile("dev")
    private static class AnotherDependantClass {}
}