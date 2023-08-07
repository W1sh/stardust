package com.w1sh.aperture.condition;

import com.w1sh.aperture.Metadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActiveProfileConditionRegistryTest {

    private ActiveProfileConditionFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ActiveProfileConditionFactory();
    }

    @Test
    void should_returnActiveProfileCondition_whenGivenOptionsWithActiveProfiles() {
        final var options = Metadata.builder()
                .profiles("test")
                .build();

        var condition = factory.create(options);

        assertNotNull(condition);
        assertEquals(ActiveProfileCondition.class, condition.getClass());
    }

    @Test
    void should_returnNull_whenGivenOptionsWithNoActiveProfiles() {
        final var options = Metadata.empty();

        var condition = factory.create(options);

        assertNull(condition);
    }
}