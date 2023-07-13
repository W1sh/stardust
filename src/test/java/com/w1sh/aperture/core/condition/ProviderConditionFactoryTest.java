package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProviderConditionFactoryTest {

    private ProviderConditionFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ProviderConditionFactory();
    }

    @Test
    void should_returnNoConditions_whenGivenDefaultOptions() {
        final var options = Options.empty();

        var conditions = factory.create(options);

        assertNotNull(conditions);
        assertEquals(0, conditions.size());
    }
}