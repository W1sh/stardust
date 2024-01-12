package com.w1sh.stardust.dependency;

import com.w1sh.stardust.Environment;
import com.w1sh.stardust.ProviderContainerImpl;
import com.w1sh.stardust.annotation.Provide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemPropertyDependencyResolverTest {

    private SystemPropertyDependencyResolver resolver;
    private Environment environment;

    @BeforeEach
    void setUp() {
        environment = new Environment(new ProviderContainerImpl(), Set.of("test"));
        resolver = new SystemPropertyDependencyResolver();
        System.setProperty("test-flag", "true");
    }

    @AfterEach
    public void tearDown() {
        System.clearProperty("test-flag");
    }

    @Test
    void should_returnTrue_whenSystemPropertyIsPresentAndMatchesSystem() {
        var condition = resolver.matches(DependantClass.class, environment);

        assertTrue(condition);
    }

    @Test
    void should_returnFalse_whenSystemPropertyIsPresentAndDoesNotMatchSystem() {
        var condition = resolver.matches(AnotherDependantClass.class, environment);

        assertFalse(condition);
    }

    @Provide
    @DependsOnSystemProperty(value = "test-flag", expectedValue = "true")
    private static class DependantClass {}

    @Provide
    @DependsOnSystemProperty(value = "test-flag", expectedValue = "false")
    private static class AnotherDependantClass {}
}