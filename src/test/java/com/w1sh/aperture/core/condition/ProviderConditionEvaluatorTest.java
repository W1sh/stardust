package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Environment;
import com.w1sh.aperture.core.ProviderRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

class ProviderConditionEvaluatorTest {

    private ProviderRegistry registry;
    private ProviderConditionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        registry = spy(new ProviderRegistry());
        Environment environment = new Environment(Set.of("test"));
        evaluator = new ProviderConditionEvaluator(registry, environment);
    }

    @Test
    void should_returnTrue_whenEvaluatingNoConditions() {
        boolean shouldSkip = evaluator.shouldSkip(new ArrayList<>());

        assertFalse(shouldSkip);
    }

    @Test
    void should_returnTrue_whenEvaluatingActiveProfileConditionAndProfileIsActive() {
        ActiveProfileCondition condition = new ActiveProfileCondition(List.of("test"));

        boolean shouldSkip = evaluator.shouldSkip(List.of(condition));

        assertFalse(shouldSkip);
    }

    @Test
    void should_returnFalse_whenEvaluatingActiveProfileConditionAndProfileIsNotActive() {
        ActiveProfileCondition condition = new ActiveProfileCondition(List.of("dev"));

        boolean shouldSkip = evaluator.shouldSkip(List.of(condition));

        assertTrue(shouldSkip);
    }
}