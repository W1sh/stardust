package com.w1sh.aperture.core.condition;

import com.w1sh.aperture.core.Environment;
import com.w1sh.aperture.core.DefaultProviderRegistry;
import com.w1sh.aperture.core.builder.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SystemPropertyConditionTest {

    private ProviderConditionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        DefaultProviderRegistry registry = new DefaultProviderRegistry();
        evaluator = new ProviderConditionEvaluator(registry, Environment.builder().build());
    }

    @Test
    void should_returnTrue_whenGivenSystemPropertyConditionMatches() {
        var option = Options.builder().requiredSystemProperty("java.version", "19.0.2").build();
        var condition = new SystemPropertyCondition(option.requiredSystemProperties());

        boolean shouldSkip = evaluator.shouldSkip(List.of(condition));

        assertFalse(shouldSkip);
    }

}