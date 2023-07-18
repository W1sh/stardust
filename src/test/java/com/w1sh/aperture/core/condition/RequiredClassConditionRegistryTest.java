package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Metadata;
import com.w1sh.aperture.example.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequiredClassConditionRegistryTest {

    private RequiredClassConditionFactory factory;

    @BeforeEach
    void setUp() {
        factory = new RequiredClassConditionFactory();
    }

    @Test
    void should_returnRequiredClassCondition_whenGivenOptionsWithRequiredClass() {
        final var options = Metadata.builder()
                .conditionalOn(CalculatorService.class)
                .build();

        var condition = factory.create(options);

        assertNotNull(condition);
        assertEquals(RequiresClassCondition.class, condition.getClass());
    }

    @Test
    void should_returnNull_whenGivenOptionsWithNoActiveProfiles() {
        final var options = Metadata.empty();

        var condition = factory.create(options);

        assertNull(condition);
    }
}