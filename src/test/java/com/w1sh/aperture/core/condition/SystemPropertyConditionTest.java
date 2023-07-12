package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Environment;
import com.w1sh.aperture.core.ProviderRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SystemPropertyConditionTest {

    private ProviderConditionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        ProviderRegistry registry = new ProviderRegistry();
        evaluator = new ProviderConditionEvaluator(registry, Environment.builder().build());
    }

    @Test
    void should_returnTrue_whenGivenSystemPropertyConditionMatches() {
        SystemPropertyCondition condition = new SystemPropertyCondition(Map.entry("java.version", "19.0.2"));

        boolean shouldSkip = evaluator.shouldSkip(List.of(condition));

        assertFalse(shouldSkip);
    }

}