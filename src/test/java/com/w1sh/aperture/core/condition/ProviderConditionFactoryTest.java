package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.builder.Options;
import com.w1sh.aperture.example.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        final var options = Options.builder().build();

        List<Condition> conditions = factory.create(options);

        assertNotNull(conditions);
        assertEquals(0, conditions.size());
    }

    @Test
    void should_returnActiveProfileCondition_whenGivenOptionsWithActiveProfiles() {
        final var options = Options.builder()
                .profiles("test")
                .build();

        List<Condition> conditions = factory.create(options);

        assertNotNull(conditions);
        assertEquals(1, conditions.size());
        assertEquals(ActiveProfileCondition.class, conditions.get(0).getClass());
    }

    @Test
    void should_returnRequiredClassCondition_whenGivenOptionsWithRequiredClass() {
        final var options = Options.builder()
                .conditionalOn(CalculatorService.class)
                .build();

        List<Condition> conditions = factory.create(options);

        assertNotNull(conditions);
        assertEquals(1, conditions.size());
        assertEquals(RequiresClassCondition.class, conditions.get(0).getClass());
    }

    @Test
    void should_returnRequiredMissingClassCondition_whenGivenOptionsWithRequiredMissingClass() {
        final var options = Options.builder()
                .conditionalOnMissing(CalculatorService.class)
                .build();

        List<Condition> conditions = factory.create(options);

        assertNotNull(conditions);
        assertEquals(1, conditions.size());
        assertEquals(RequiresMissingClassCondition.class, conditions.get(0).getClass());
    }

}