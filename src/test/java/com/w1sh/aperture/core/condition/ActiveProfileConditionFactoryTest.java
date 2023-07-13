package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActiveProfileConditionFactoryTest {

    private ActiveProfileConditionFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ActiveProfileConditionFactory();
    }

    @Test
    void should_returnActiveProfileCondition_whenGivenOptionsWithActiveProfiles() {
        final var options = Options.builder()
                .profiles("test")
                .build();

        var condition = factory.create(options);

        assertNotNull(condition);
        assertEquals(ActiveProfileCondition.class, condition.getClass());
    }

    @Test
    void should_returnNull_whenGivenOptionsWithNoActiveProfiles() {
        final var options = Options.builder().build();

        var condition = factory.create(options);

        assertNull(condition);
    }
}