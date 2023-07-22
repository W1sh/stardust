package com.w1sh.aperture.condition;

import com.w1sh.aperture.Metadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemPropertyConditionRegistryTest {

    private SystemPropertyConditionFactory factory;

    @BeforeEach
    void setUp() {
        factory = new SystemPropertyConditionFactory();
    }

    @Test
    void should_returnSystemPropertyCondition_whenGivenOptionsWithRequiredSystemProperties() {
        final var options = Metadata.builder()
                .requiredSystemProperty("test", "test")
                .build();

        var condition = factory.create(options);

        assertNotNull(condition);
        assertEquals(SystemPropertyCondition.class, condition.getClass());
    }

    @Test
    void should_returnNull_whenGivenOptionsWithNoActiveProfiles() {
        final var options = Metadata.empty();

        var condition = factory.create(options);

        assertNull(condition);
    }
}