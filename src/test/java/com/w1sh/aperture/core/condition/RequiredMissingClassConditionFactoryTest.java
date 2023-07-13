package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.example.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequiredMissingClassConditionFactoryTest {

    private RequiredMissingClassConditionFactory factory;

    @BeforeEach
    void setUp() {
        factory = new RequiredMissingClassConditionFactory();
    }

    @Test
    void should_returnRequiredMissingClassCondition_whenGivenOptionsWithRequiredMissingClass() {
        final var options = Options.builder()
                .conditionalOnMissing(CalculatorService.class)
                .build();

        var condition = factory.create(options);

        assertNotNull(condition);
        assertEquals(RequiresMissingClassCondition.class, condition.getClass());
    }

    @Test
    void should_returnNull_whenGivenOptionsWithNoActiveProfiles() {
        final var options = Options.builder().build();

        var condition = factory.create(options);

        assertNull(condition);
    }
}